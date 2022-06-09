package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSound implements IMessage
{
    public String sound = "";
    public String soundCategory = "";
    public float volume = 1F;
    public float pitch = 1F;

    public PacketSound()
    {}

    public PacketSound(String sound, String soundCategory, float volume, float pitch)
    {
        this.sound = sound;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.sound = ByteBufUtils.readUTF8String(buf);
        this.soundCategory = ByteBufUtils.readUTF8String(buf);
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.sound);
        ByteBufUtils.writeUTF8String(buf, this.soundCategory);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }
}