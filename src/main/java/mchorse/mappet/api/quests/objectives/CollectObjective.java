package mchorse.mappet.api.quests.objectives;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class CollectObjective implements IObjective
{
    public ItemStack stack = ItemStack.EMPTY;

    public static boolean areStacksSimilar(ItemStack a, ItemStack b)
    {
        return ItemStack.areItemsEqualIgnoreDurability(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }

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
        int count = this.stack.getCount();

        for (int i = 0, c = player.inventory.getSizeInventory(); i < c; i ++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (areStacksSimilar(this.stack, stack))
            {
                if (stack.getCount() <= count)
                {
                    player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);

                    if (count == stack.getCount())
                    {
                        return;
                    }

                    count -= stack.getCount();
                }
                else
                {
                    stack.shrink(count);

                    return;
                }
            }
        }
    }

    private int countItems(EntityPlayer player)
    {
        int count = 0;

        for (int i = 0, c = player.inventory.getSizeInventory(); i < c; i ++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (areStacksSimilar(this.stack, stack))
            {
                count += stack.getCount();

                if (count >= this.stack.getCount())
                {
                    return count;
                }
            }
        }

        return count;
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