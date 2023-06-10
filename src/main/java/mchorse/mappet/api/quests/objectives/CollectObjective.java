package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.utils.InventoryUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CollectObjective extends AbstractObjective
{
    public ItemStack stack = ItemStack.EMPTY;
    public boolean ignoreNBT;

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
        return InventoryUtils.countItems(player, this.stack, true, this.ignoreNBT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringifyObjective(EntityPlayer player)
    {
        String name = this.stack.getDisplayName();
        int count = Math.min(this.countItems(player), this.stack.getCount());

        if (!this.message.isEmpty())
        {
            return this.message.replace("${name}", name)
                    .replace("${count}", String.valueOf(count))
                    .replace("${total}", String.valueOf(this.stack.getCount()));
        }

        return I18n.format("mappet.gui.quests.objective_collect.string", name, count, this.stack.getCount());
    }

    @Override
    public String getType()
    {
        return "collect";
    }

    @Override
    public NBTTagCompound partialSerializeNBT()
    {
        return new NBTTagCompound();
    }

    @Override
    public void partialDeserializeNBT(NBTTagCompound tag)
    {}

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.stack.isEmpty())
        {
            tag.setTag("Item", this.stack.serializeNBT());
        }

        tag.setBoolean("IgnoreNBT", this.ignoreNBT);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Item"))
        {
            this.stack = new ItemStack(tag.getCompoundTag("Item"));
        }

        this.ignoreNBT = tag.getBoolean("IgnoreNBT");
    }
}