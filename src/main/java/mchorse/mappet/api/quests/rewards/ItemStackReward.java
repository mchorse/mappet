package mchorse.mappet.api.quests.rewards;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemStackReward implements IReward
{
    public List<ItemStack> stacks = new ArrayList<ItemStack>();

    public ItemStackReward(ItemStack... stacks)
    {
        for (ItemStack stack : stacks)
        {
            this.stacks.add(stack);
        }
    }

    @Override
    public void reward(EntityPlayer player)
    {
        for (ItemStack stack : this.stacks)
        {
            ItemStack copy = stack.copy();

            if (!player.addItemStackToInventory(copy) && !copy.isEmpty())
            {
                player.dropItem(copy, false);
            }
        }
    }

    @Override
    public IReward copy()
    {
        ItemStackReward reward = new ItemStackReward();

        for (ItemStack stack : this.stacks)
        {
            reward.stacks.add(stack.copy());
        }

        return reward;
    }

    @Override
    public String getType()
    {
        return "item";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList items = new NBTTagList();

        tag.setTag("Items", items);

        for (ItemStack stack : this.stacks)
        {
            if (!stack.isEmpty())
            {
                items.appendTag(stack.serializeNBT());
            }
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Items"))
        {
            NBTTagList items = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < items.tagCount(); i++)
            {
                ItemStack stack = new ItemStack(items.getCompoundTagAt(i));

                if (!stack.isEmpty())
                {
                    this.stacks.add(stack);
                }
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.stacks.add(ByteBufUtils.readItemStack(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.stacks.size());

        for (ItemStack stack : this.stacks)
        {
            ByteBufUtils.writeItemStack(buf, stack);
        }
    }
}