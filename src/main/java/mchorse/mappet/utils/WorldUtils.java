package mchorse.mappet.utils;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
    public static TileEntity getTileEntity(World world, BlockPos pos)
    {
        if (world.isBlockLoaded(pos))
        {
            return world.getTileEntity(pos);
        }

        return null;
    }

    public static void playSound(EntityPlayerMP player, String soundEvent)
    {
        playSound(player, soundEvent, player.posX, player.posY, player.posZ, 1F, 1F);
    }

    public static void playSound(EntityPlayerMP player, String soundEvent, double x, double y, double z, float volume, float pitch)
    {
        player.connection.sendPacket(new SPacketCustomSound(soundEvent, SoundCategory.MASTER, x, y, z, volume, pitch));
    }

    public static void stopSound(EntityPlayerMP player, String soundEvent)
    {
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());

        packetbuffer.writeString("");
        packetbuffer.writeString(soundEvent);
        player.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetbuffer));
    }
}