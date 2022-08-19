package mchorse.mappet.network.client.scripts;

import mchorse.mappet.network.common.scripts.PacketSound;
import mchorse.mappet.network.common.scripts.PacketStopSound;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerStopSound extends ClientMessageHandler<PacketStopSound>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketStopSound message) {
        Minecraft.getMinecraft().getSoundHandler().stop(message.sound, SoundCategory.getByName(message.soundCategory));
    }
}
