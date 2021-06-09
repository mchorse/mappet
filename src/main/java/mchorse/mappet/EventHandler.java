package mchorse.mappet;

import mchorse.mappet.api.events.EventExecutionFork;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.IExecutable;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.CharacterProvider;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.data.CommandDataClear;
import mchorse.mappet.events.StateChangedEvent;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventPlayerHotkeys;
import mchorse.mappet.network.common.quests.PacketQuest;
import mchorse.mappet.network.common.quests.PacketQuests;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventHandler
{
    /**
     * Resource location for cosmetic capability
     */
    public static final ResourceLocation CAPABILITY = new ResourceLocation(Mappet.MOD_ID, "character");

    /**
     * Players that must be checked
     */
    private Set<EntityPlayer> playersToCheck = new HashSet<EntityPlayer>();

    /**
     * Delayed executions
     */
    private List<IExecutable> executables = new ArrayList<IExecutable>();

    /**
     * Second executables list to avoid concurrent modification
     * exceptions when adding consequent delayed executions
     */
    private List<IExecutable> secondList = new ArrayList<IExecutable>();

    /**
     * Server data context which is used by server tick global trigger
     */
    private DataContext context;

    public void addExecutables(List<IExecutable> executionForks)
    {
        this.executables.addAll(executionForks);
    }

    public void addExecutable(IExecutable executable)
    {
        this.executables.add(executable);
    }

    public void reset()
    {
        this.playersToCheck.clear();
        this.executables.clear();
        this.secondList.clear();
        this.context = null;
    }

    /* Server trigger handlers */

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event)
    {
        if (!Mappet.settings.chat.isEmpty())
        {
            DataContext context = new DataContext(event.getPlayer());

            Mappet.settings.chat.trigger(context.set("message", event.getMessage()));
        }
    }

    @SubscribeEvent
    public void onPlayerBreakBlock(BlockEvent.BreakEvent event)
    {
        if (!Mappet.settings.breakBlock.isEmpty())
        {
            DataContext context = new DataContext(event.getPlayer());
            IBlockState state = event.getState();

            Mappet.settings.breakBlock.trigger(context
                .set("block", state.getBlock().getRegistryName().toString())
                .set("meta", state.getBlock().getMetaFromState(state))
                .set("x", event.getPos().getX())
                .set("y", event.getPos().getY())
                .set("z", event.getPos().getZ()));
        }
    }

    @SubscribeEvent
    public void onPlayerPlaceBlock(BlockEvent.PlaceEvent event)
    {
        if (!Mappet.settings.placeBlock.isEmpty())
        {
            DataContext context = new DataContext(event.getPlayer());
            IBlockState state = event.getPlacedBlock();

            Mappet.settings.placeBlock.trigger(context
                .set("block", state.getBlock().getRegistryName().toString())
                .set("meta", state.getBlock().getMetaFromState(state))
                .set("x", event.getPos().getX())
                .set("y", event.getPos().getY())
                .set("z", event.getPos().getZ()));
        }
    }

    @SubscribeEvent
    public void onPlayerDamageEntity(LivingDamageEvent event)
    {
        DamageSource source = event.getSource();

        if (source.getTrueSource() instanceof EntityPlayer)
        {
            if (!Mappet.settings.damageEntity.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityLiving(), (EntityPlayer) source.getTrueSource());

                Mappet.settings.damageEntity.trigger(context.set("damage", event.getAmount()));
            }
        }
    }

    /* Other cool stuff */

    /**
     * Attach player capabilities
     */
    @SubscribeEvent
    public void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayer)
        {
            event.addCapability(CAPABILITY, new CharacterProvider());
        }
    }

    @SubscribeEvent
    public void onPlayerLogsIn(PlayerEvent.PlayerLoggedInEvent event)
    {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        ICharacter character = Character.get(player);
        Instant lastClear = Mappet.data.getLastClear();

        if (character != null)
        {
            if (character.getLastClear().isBefore(lastClear))
            {
                CommandDataClear.clear(player);

                character.updateLastClear(lastClear);
            }

            if (!character.getQuests().quests.isEmpty())
            {
                Dispatcher.sendTo(new PacketQuests(character.getQuests()), player);
            }

            if (!Mappet.settings.hotkeys.hotkeys.isEmpty())
            {
                Dispatcher.sendTo(new PacketEventPlayerHotkeys(Mappet.settings.hotkeys), player);
            }
        }

        if (!Mappet.settings.playerLogIn.isEmpty())
        {
            DataContext context = new DataContext(event.player);

            Mappet.settings.playerLogIn.trigger(context);
        }
    }

    @SubscribeEvent
    public void onPlayerPickUp(EntityItemPickupEvent event)
    {
        this.playersToCheck.add(event.getEntityPlayer());
    }

    @SubscribeEvent
    public void onMobKilled(LivingDeathEvent event)
    {
        Entity source = event.getSource().getTrueSource();

        if (!(source instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) source;
        ICharacter character = Character.get(player);

        if (character != null)
        {
            for (Quest quest : character.getQuests().quests.values())
            {
                quest.mobWasKilled(player, event.getEntity());
            }

            this.playersToCheck.add(player);
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }

        for (EntityPlayer player : this.playersToCheck)
        {
            ICharacter character = Character.get(player);

            if (character != null)
            {
                Iterator<Map.Entry<String, Quest>> it = character.getQuests().quests.entrySet().iterator();

                while (it.hasNext())
                {
                    Map.Entry<String, Quest> entry = it.next();
                    Quest quest = entry.getValue();

                    if (quest.instant && quest.rewardIfComplete(player))
                    {
                        it.remove();

                        Dispatcher.sendTo(new PacketQuest(entry.getKey(), null), (EntityPlayerMP) player);
                    }
                    else
                    {
                        Dispatcher.sendTo(new PacketQuest(entry.getKey(), entry.getValue()), (EntityPlayerMP) player);
                    }
                }
            }
        }

        this.playersToCheck.clear();

        /* This block of code might be a bit confusing, but essentially
         * what it does is prevents concurrent modification when timer nodes
         * add consequent execution forks, this way I can reliably keep track
         * of order of both the old executions which are not yet executed and
         * of new forks that were added by new timer nodes */
        if (!this.executables.isEmpty())
        {
            /* Copy original event forks to another list and clear them
             * to be ready for new forks */
            this.secondList.addAll(this.executables);
            this.executables.clear();

            /* Execute event forks (and remove those which were finished) */
            this.secondList.removeIf(IExecutable::update);

            /* Add back to the original list the remaining forks and
             * new forks that were added by consequent timer nodes */
            this.secondList.addAll(this.executables);
            this.executables.clear();
            this.executables.addAll(this.secondList);
            this.secondList.clear();
        }

        /* Execute a server tick trigger */
        if (!Mappet.settings.serverTick.isEmpty())
        {
            if (this.context == null)
            {
                this.context = new DataContext(FMLCommonHandler.instance().getMinecraftServerInstance());
            }

            Mappet.settings.serverTick.trigger(this.context);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        ICharacter character = Character.get(event.player);

        if (character != null && !event.player.world.isRemote)
        {
            character.getPositionCache().updatePlayer(event.player);
        }
    }

    @SubscribeEvent
    public void onStateChange(StateChangedEvent event)
    {
        for (EntityPlayer player : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers())
        {
            ICharacter character = Character.get(player);

            if (character != null && (event.isGlobal() || character.getStates() == event.states))
            {
                int i = 0;

                for (Quest quest : character.getQuests().quests.values())
                {
                    i += quest.stateWasUpdated(player) ? 1 : 0;
                }

                if (i > 0)
                {
                    this.playersToCheck.add(player);
                }
            }
        }
    }
}