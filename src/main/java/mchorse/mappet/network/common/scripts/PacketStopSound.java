package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketStopSound implements IMessage {
    public String sound = "";
    public String soundCategory = "";

    public PacketStopSound()
    {}

    public PacketStopSound(String sound, String soundCategory) {
        this.sound = sound;
        this.soundCategory = soundCategory;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.sound = ByteBufUtils.readUTF8String(buf);
        this.soundCategory = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.sound);
        ByteBufUtils.writeUTF8String(buf, this.soundCategory);
    }
}
