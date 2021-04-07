package mchorse.mappet.network.common;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.crafting.CraftingTable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketCraftingTable implements IMessage
{
    public CraftingTable table;

    public PacketCraftingTable()
    {}

    public PacketCraftingTable(CraftingTable table)
    {
        this.table = table;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        if (buf.readBoolean())
        {
            NBTTagCompound tag = ByteBufUtils.readTag(buf);

            this.table = new CraftingTable();
            this.table.deserializeNBT(tag);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.table != null);

        if (this.table != null)
        {
            ByteBufUtils.writeTag(buf, this.table.serializeNBT());
        }
    }
}