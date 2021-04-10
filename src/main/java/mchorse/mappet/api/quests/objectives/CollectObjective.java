package mchorse.mappet.api.quests.objectives;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.utils.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class CollectObjective implements IObjective
{
    public ItemStack stack = ItemStack.EMPTY;

    public CollectObjective()
    {}

    public CollectObjective(ItemStack stack)
    {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
    }

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.countItems(player) >= this.stack.getCount();
    }

    @Override
    public void complete(EntityPlayer player)
    {
        player.inventory.clearMatchingItems(this.stack.getItem(), -1, this.stack.getCount(), null);
    }

    private int countItems(EntityPlayer player)
    {
        return InventoryUtils.countItems(player, this.stack);
    }

    @Override
    public IObjective copy()
    {
        return new CollectObjective(this.stack.copy());
    }

    @Override
    public String stringify(EntityPlayer player)
    {
        return "Collect " + this.stack.getDisplayName() + " (" + Math.min(this.countItems(player), this.stack.getCount()) + "/" + this.stack.getCount() + ")";
    }

    @Override
    public String getType()
    {
        return "collect";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (!this.stack.isEmpty())
        {
            tag.setTag("Item", this.stack.serializeNBT());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Item"))
        {
            this.stack = new ItemStack(tag.getCompoundTag("Item"));
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, this.stack);
    }
}