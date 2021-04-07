package mchorse.mappet.network;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.client.ClientHandlerCraft;
import mchorse.mappet.network.client.ClientHandlerCraftingTable;
import mchorse.mappet.network.common.PacketCraft;
import mchorse.mappet.network.common.PacketCraftingTable;
import mchorse.mappet.network.server.ServerHandlerCraft;
import mchorse.mappet.network.server.ServerHandlerCraftingTable;
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