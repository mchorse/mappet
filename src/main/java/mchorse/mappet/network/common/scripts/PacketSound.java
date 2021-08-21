package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSound implements IMessage
{
    public String sound = "";
    public float volume = 1F;
    public float pitch = 1F;

    public PacketSound()
    {}

    public PacketSound(String sound, float volume, float pitch)
    {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.sound = ByteBufUtils.readUTF8String(buf);
        this.volume = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.sound);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }
}