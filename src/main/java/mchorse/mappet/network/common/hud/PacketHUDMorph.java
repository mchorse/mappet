package mchorse.mappet.network.common.hud;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketHUDMorph implements IMessage
{
    public String id = "";
    public int index;
    public NBTTagCompound morph;

    public PacketHUDMorph()
    {}

    public PacketHUDMorph(String id, int index, NBTTagCompound morph)
    {
        this.id = id;
        this.index = index;
        this.morph = morph;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = ByteBufUtils.readUTF8String(buf);
        this.index = buf.readInt();
        this.morph = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.id);
        buf.writeInt(this.index);
        ByteBufUtils.writeTag(buf, this.morph);
    }
}