package mchorse.mappet.network.common.huds;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketHUDScene implements IMessage
{
    public String id = "";
    public NBTTagCompound tag;

    public PacketHUDScene()
    {}

    public PacketHUDScene(String id, NBTTagCompound tag)
    {
        this.id = id;
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = ByteBufUtils.readUTF8String(buf);
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.id);
        ByteBufUtils.writeTag(buf, this.tag);
    }
}