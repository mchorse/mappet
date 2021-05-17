package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEventHotkeys implements IMessage
{
    public NBTTagCompound hotkeys;

    public PacketEventHotkeys()
    {}

    public PacketEventHotkeys(NBTTagCompound hotkeys)
    {
        this.hotkeys = hotkeys;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.hotkeys = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.hotkeys);
    }
}