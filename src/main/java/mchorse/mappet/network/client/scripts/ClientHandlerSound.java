package mchorse.mappet.network.client.scripts;

import mchorse.mappet.network.common.scripts.PacketSound;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerSound extends ClientMessageHandler<PacketSound>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketSound message)
    {
        ResourceLocation rl = new ResourceLocation(message.sound);
        SoundCategory category = SoundCategory.getSoundCategoryNames().contains(message.soundCategory) ? SoundCategory.getByName(message.soundCategory) : SoundCategory.MASTER;

        ISound masterRecord = new PositionedSoundRecord(rl, category, message.volume, message.pitch, false, 0, ISound.AttenuationType.NONE, 0, 0, 0);
        Minecraft.getMinecraft().getSoundHandler().playSound(masterRecord);
    }
}
