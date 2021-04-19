package mchorse.mappet.network;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.client.blocks.ClientHandlerEditEmitter;
import mchorse.mappet.network.client.blocks.ClientHandlerEditRegion;
import mchorse.mappet.network.client.blocks.ClientHandlerEditTrigger;
import mchorse.mappet.network.client.content.ClientHandlerContentData;
import mchorse.mappet.network.client.content.ClientHandlerContentNames;
import mchorse.mappet.network.client.crafting.ClientHandlerCraft;
import mchorse.mappet.network.client.crafting.ClientHandlerCraftingTable;
import mchorse.mappet.network.client.dialogue.ClientHandlerDialogueFragment;
import mchorse.mappet.network.client.npc.ClientHandlerNpcMorph;
import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mappet.network.common.content.PacketContentRequestData;
import mchorse.mappet.network.common.content.PacketContentRequestNames;
import mchorse.mappet.network.common.crafting.PacketCraft;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mappet.network.common.npc.PacketNpcMorph;
import mchorse.mappet.network.server.blocks.ServerHandlerEditEmitter;
import mchorse.mappet.network.server.blocks.ServerHandlerEditRegion;
import mchorse.mappet.network.server.blocks.ServerHandlerEditTrigger;
import mchorse.mappet.network.server.content.ServerHandlerContentData;
import mchorse.mappet.network.server.content.ServerHandlerContentRequestData;
import mchorse.mappet.network.server.content.ServerHandlerContentRequestNames;
import mchorse.mappet.network.server.crafting.ServerHandlerCraft;
import mchorse.mappet.network.server.crafting.ServerHandlerCraftingTable;
import mchorse.mappet.network.server.dialogue.ServerHandlerPickReply;
import mchorse.mclib.network.AbstractDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Network dispatcher
 */
public class Dispatcher
{
    public static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(Mappet.MOD_ID)
    {
        @Override
        public void register()
        {
            /* Crafting table */
            this.register(PacketCraftingTable.class, ClientHandlerCraftingTable.class, Side.CLIENT);
            this.register(PacketCraftingTable.class, ServerHandlerCraftingTable.class, Side.SERVER);
            this.register(PacketCraft.class, ClientHandlerCraft.class, Side.CLIENT);
            this.register(PacketCraft.class, ServerHandlerCraft.class, Side.SERVER);

            /* Dialogue */
            this.register(PacketDialogueFragment.class, ClientHandlerDialogueFragment.class, Side.CLIENT);
            this.register(PacketPickReply.class, ServerHandlerPickReply.class, Side.SERVER);

            /* Blocks */
            this.register(PacketEditEmitter.class, ClientHandlerEditEmitter.class, Side.CLIENT);
            this.register(PacketEditEmitter.class, ServerHandlerEditEmitter.class, Side.SERVER);

            this.register(PacketEditTrigger.class, ClientHandlerEditTrigger.class, Side.CLIENT);
            this.register(PacketEditTrigger.class, ServerHandlerEditTrigger.class, Side.SERVER);

            this.register(PacketEditRegion.class, ClientHandlerEditRegion.class, Side.CLIENT);
            this.register(PacketEditRegion.class, ServerHandlerEditRegion.class, Side.SERVER);

            /* Creative editing */
            this.register(PacketContentRequestNames.class, ServerHandlerContentRequestNames.class, Side.SERVER);
            this.register(PacketContentRequestData.class, ServerHandlerContentRequestData.class, Side.SERVER);
            this.register(PacketContentData.class, ClientHandlerContentData.class, Side.CLIENT);
            this.register(PacketContentData.class, ServerHandlerContentData.class, Side.SERVER);
            this.register(PacketContentNames.class, ClientHandlerContentNames.class, Side.CLIENT);

            /* NPCs */
            this.register(PacketNpcMorph.class, ClientHandlerNpcMorph.class, Side.CLIENT);
        }
    };

    /**
     * Send message to players who are tracking given entity
     */
    public static void sendToTracked(Entity entity, IMessage message)
    {
        EntityTracker tracker = ((WorldServer) entity.world).getEntityTracker();

        for (EntityPlayer player : tracker.getTrackingPlayers(entity))
        {
            sendTo(message, (EntityPlayerMP) player);
        }
    }

    /**
     * Send message to given player
     */
    public static void sendTo(IMessage message, EntityPlayerMP player)
    {
        DISPATCHER.sendTo(message, player);
    }

    /**
     * Send message to the server
     */
    public static void sendToServer(IMessage message)
    {
        DISPATCHER.sendToServer(message);
    }

    /**
     * Register all the networking messages and message handlers
     */
    public static void register()
    {
        DISPATCHER.register();
    }
}