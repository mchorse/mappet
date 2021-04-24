package mchorse.mappet;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.CharacterProvider;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventHandler
{
    /**
     * Resource location for cosmetic capability
     */
    public static final ResourceLocation CAPABILITY = new ResourceLocation(Mappet.MOD_ID, "character");

    /**
     * Players that must be checked
     */
    private List<EntityPlayer> playersToCheck = new ArrayList<EntityPlayer>();

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
        /* ... */
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

                    if (quest.rewardIfComplete(player))
                    {
                        Quest.complete(entry.getKey(), player);

                        player.sendMessage(new TextComponentString("Quest '" + entry.getKey() + "' was completed! Here is your reward!"));
                        it.remove();

                        // Dispatcher.sendTo(new PacketCompleteQuest(quest.getId()), (EntityPlayerMP) player);
                    }
                    else
                    {
                        // Dispatcher.sendTo(new PacketQuest(quest), (EntityPlayerMP) player);
                    }
                }
            }
        }

        this.playersToCheck.clear();
    }
}