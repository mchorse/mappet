package mchorse.mappet.network.common.ui;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.ui.UI;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketUI implements IMessage
{
    public UI ui;

    public PacketUI()
    {}

    public PacketUI(UI ui)
    {
        this.ui = ui;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.ui = new UI();
        this.ui.deserializeNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        NBTTagCompound tag = this.ui == null ? new NBTTagCompound() : this.ui.serializeNBT();

        ByteBufUtils.writeTag(buf, tag);
    }
}