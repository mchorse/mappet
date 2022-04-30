package mchorse.mappet;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.IExecutable;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.CharacterProvider;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.commands.data.CommandDataClear;
import mchorse.mappet.events.StateChangedEvent;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mappet.network.common.quests.PacketQuest;
import mchorse.mappet.network.common.quests.PacketQuests;
import mchorse.mappet.network.common.scripts.PacketClick;
import mchorse.mappet.network.common.scripts.PacketPlayerSkin;
import mchorse.mappet.utils.ScriptUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class EventHandler
{
    /**
     * Resource location for cosmetic capability
     */
    public static final ResourceLocation CAPABILITY = new ResourceLocation(Mappet.MOD_ID, "character");

    private static Boolean isMohist;

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

    /**
     * Set that keeps track of players that just joined (it is needed to avoid
     * triggering the player respawn trigger when player logs in)
     */
    private Set<UUID> loggedInPlayers = new HashSet<UUID>();

    private static boolean isMohist()
    {
        if (isMohist != null)
        {
            return isMohist;
        }

        try
        {
            Class.forName("com.mohistmc.MohistMC");

            isMohist = true;
        }
        catch (Exception e)
        {
            isMohist = false;
        }

        return isMohist;
    }

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

    private void trigger(Event event, Trigger trigger, DataContext context)
    {
        trigger.trigger(context);

        if (context.isCanceled())
        {
            event.setCanceled(true);
        }
    }

    /* Server trigger handlers */

    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event)
    {
        if (!Mappet.settings.playerChat.isEmpty())
        {
            DataContext context = new DataContext(event.getPlayer())
                .set("message", event.getMessage());

            this.trigger(event, Mappet.settings.playerChat, context);
        }
    }

    @SubscribeEvent
    public void onPlayerBreakBlock(BlockEvent.BreakEvent event)
    {
        if (!Mappet.settings.blockBreak.isEmpty())
        {
            IBlockState state = event.getState();
            DataContext context = new DataContext(event.getPlayer())
                .set("block", state.getBlock().getRegistryName().toString())
                .set("meta", state.getBlock().getMetaFromState(state))
                .set("x", event.getPos().getX())
                .set("y", event.getPos().getY())
                .set("z", event.getPos().getZ());

            this.trigger(event, Mappet.settings.blockBreak, context);
        }
    }

    @SubscribeEvent
    public void onPlayerPlaceBlock(BlockEvent.PlaceEvent event)
    {
        if (!Mappet.settings.blockPlace.isEmpty())
        {
            IBlockState state = event.getPlacedBlock();
            DataContext context = new DataContext(event.getPlayer())
                .set("block", state.getBlock().getRegistryName().toString())
                .set("meta", state.getBlock().getMetaFromState(state))
                .set("x", event.getPos().getX())
                .set("y", event.getPos().getY())
                .set("z", event.getPos().getZ());

            this.trigger(event, Mappet.settings.blockPlace, context);
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingDamageEvent event)
    {
        DamageSource source = event.getSource();


            if (!Mappet.settings.entityDamaged.isEmpty())
            {
                DataContext context = new DataContext(event.getEntityLiving(), source.getTrueSource())
                    .set("damage", event.getAmount());

                this.trigger(event, Mappet.settings.entityDamaged, context);
            }

    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerCloseContainer(PlayerContainerEvent.Close event)
    {
        this.playersToCheck.add(event.getEntityPlayer());

        if (Mappet.settings.playerCloseContainer.isEmpty())
        {
            return;
        }

        DataContext context = new DataContext(event.getEntityPlayer());

        if (event.getContainer() instanceof ContainerChest)
        {
            ContainerChest chest = (ContainerChest) event.getContainer();

            if (chest.getLowerChestInventory() instanceof TileEntity)
            {
                BlockPos pos = ((TileEntity) chest.getLowerChestInventory()).getPos();

                context.set("x", pos.getX());
                context.set("y", pos.getY());
                context.set("z", pos.getZ());
            }
        }

        Mappet.settings.playerCloseContainer.trigger(context);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event)
    {
        if (!event.getEntityPlayer().world.isRemote)
        {
            return;
        }

        Dispatcher.sendToServer(new PacketClick(EnumHand.MAIN_HAND));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerRightClickEmpty(PlayerInteractEvent.RightClickEmpty event)
    {
        if (!event.getEntityPlayer().world.isRemote || event.getHand() == EnumHand.OFF_HAND)
        {
            return;
        }

        Dispatcher.sendToServer(new PacketClick(EnumHand.OFF_HAND));
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote || Mappet.settings.playerItemInteract.isEmpty())
        {
            return;
        }

        DataContext context = new DataContext(player)
            .set("x", event.getPos().getX())
            .set("y", event.getPos().getY())
            .set("z", event.getPos().getZ())
            .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");

        this.trigger(event, Mappet.settings.playerItemInteract, context);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote || Mappet.settings.blockClick.isEmpty())
        {
            return;
        }

        DataContext context = new DataContext(player)
            .set("x", event.getPos().getX())
            .set("y", event.getPos().getY())
            .set("z", event.getPos().getZ())
            .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");

        this.trigger(event, Mappet.settings.blockClick, context);
    }

    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote || Mappet.settings.blockInteract.isEmpty())
        {
            return;
        }

        DataContext context = new DataContext(player)
            .set("x", event.getPos().getX())
            .set("y", event.getPos().getY())
            .set("z", event.getPos().getZ())
            .set("hand", event.getHand() == EnumHand.MAIN_HAND ? "main" : "off");

        this.trigger(event, Mappet.settings.blockInteract, context);
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
                CommandDataClear.clear(player, Mappet.data.getLastInventory());

                character.updateLastClear(lastClear);
            }

            this.syncData(player, character);
        }

        if (!Mappet.settings.playerLogIn.isEmpty())
        {
            DataContext context = new DataContext(event.player);

            Mappet.settings.playerLogIn.trigger(context);
        }

        if (ScriptUtils.copiedNashorn)
        {
            player.sendMessage(new TextComponentTranslation("mappet.nashorn_copied"));
        }
        else if (ScriptUtils.errorNashorn)
        {
            player.sendMessage(new TextComponentTranslation("mappet.nashorn_error"));
        }

        this.loggedInPlayers.add(player.getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerLogsOut(PlayerEvent.PlayerLoggedOutEvent event)
    {
        if (!Mappet.settings.playerLogOut.isEmpty())
        {
            DataContext context = new DataContext(event.player);

            Mappet.settings.playerLogOut.trigger(context);
        }

        this.loggedInPlayers.remove(event.player.getUniqueID());
    }

    /**
     * Copy data from dead player (or player returning from end) to the new player
     */
    @SubscribeEvent
    public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ICharacter character = Character.get(player);
        ICharacter oldCharacter = Character.get(event.getOriginal());

        if (!isMohist())
        {
            character.copy(oldCharacter, player);
        }
    }

    @SubscribeEvent
    public void onPlayerSpawn(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer && !event.getEntity().world.isRemote)
        {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            ICharacter character = Character.get(player);

            this.syncData(player, character);

            if (this.loggedInPlayers.contains(player.getUniqueID()) && !Mappet.settings.playerRespawn.isEmpty())
            {
                Mappet.settings.playerRespawn.trigger(new DataContext(player));
            }
        }
    }

    private void syncData(EntityPlayerMP player, ICharacter character)
    {
        if (!character.getQuests().quests.isEmpty())
        {
            character.getQuests().initiate(player);

            Dispatcher.sendTo(new PacketQuests(character.getQuests()), player);
        }

        if (!Mappet.settings.hotkeys.hotkeys.isEmpty())
        {
            Dispatcher.sendTo(new PacketEventHotkeys(Mappet.settings), player);
        }
    }

    @SubscribeEvent
    public void onPlayerPickUp(EntityItemPickupEvent event)
    {
        this.playersToCheck.add(event.getEntityPlayer());

        if (!Mappet.settings.playerItemPickup.isEmpty())
        {
            DataContext context = new DataContext(event.getEntityPlayer());

            context.getValues().put("item", ScriptItemStack.create(event.getItem().getItem()));
            this.trigger(event, Mappet.settings.playerItemPickup, context);
        }
    }

    @SubscribeEvent
    public void onMobKilled(LivingDeathEvent event)
    {
        if (event.getEntity().world.isRemote)
        {
            return;
        }

        Entity source = event.getSource().getTrueSource();
        Trigger trigger = event.getEntity() instanceof EntityPlayer
            ? Mappet.settings.playerDeath
            : Mappet.settings.entityDeath;

        if (!trigger.isEmpty())
        {
            this.trigger(event, trigger, new DataContext(event.getEntityLiving(), source));
        }

        if (source instanceof EntityPlayer)
        {
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
                Quests quests = character.getQuests();
                Iterator<Map.Entry<String, Quest>> it = quests.quests.entrySet().iterator();

                quests.iterating = true;

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

                quests.flush(player);
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
            this.context.cancel(false);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START)
        {
            return;
        }

        ICharacter character = Character.get(event.player);

        if (character != null && !event.player.world.isRemote)
        {
            character.getPositionCache().updatePlayer(event.player);
        }

        if (event.player.world.isRemote && event.player == Minecraft.getMinecraft().player)
        {
            this.onPlayerTickClient(event);
        }
    }

    @SideOnly(Side.CLIENT)
    private void onPlayerTickClient(TickEvent.PlayerTickEvent event)
    {
        RenderingHandler.update();

        if (!RegisterHandler.sentSkin)
        {
            Dispatcher.sendToServer(new PacketPlayerSkin(Minecraft.getMinecraft().player.getLocationSkin().toString()));

            RegisterHandler.sentSkin = true;
        }

        KeyboardHandler.updateHeldKeys();
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