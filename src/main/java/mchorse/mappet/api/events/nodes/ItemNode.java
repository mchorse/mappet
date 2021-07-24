package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.Target;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mappet.utils.InventoryUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNode extends EventBaseNode
{
    public Target target = new Target(TargetMode.SUBJECT);
    public ItemStack stack = ItemStack.EMPTY;
    public ItemMode mode = ItemMode.TAKE;

    public ItemNode()
    {}

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        String displayName = this.stack.getDisplayName();

        if (this.stack.getCount() > 1)
        {
            displayName += TextFormatting.GOLD + " (" + TextFormatting.GRAY + this.stack.getCount() + TextFormatting.GOLD + ")";
        }

        if (this.mode == ItemMode.GIVE)
        {
            return I18n.format("mappet.gui.nodes.item.give", displayName);
        }

        return I18n.format("mappet.gui.nodes.item.take", displayName);
    }

    @Override
    public int execute(EventContext context)
    {
        EntityPlayer player;

        if (this.stack.isEmpty() || (player = this.target.getPlayer(context.data)) == null)
        {
            return this.booleanToExecutionCode(false);
        }

        /* Give the item stack to player */
        if (this.mode == ItemMode.GIVE)
        {
            ItemStack copy = this.stack.copy();

            if (!player.addItemStackToInventory(copy) && !copy.isEmpty())
            {
                player.dropItem(copy, false);
            }

            return this.booleanToExecutionCode(true);
        }

        if (InventoryUtils.countItems(player, this.stack) >= this.stack.getCount())
        {
            player.inventory.clearMatchingItems(this.stack.getItem(), -1, this.stack.getCount(), null);

            return this.booleanToExecutionCode(true);
        }

        return this.booleanToExecutionCode(false);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setTag("Target", this.target.serializeNBT());
        tag.setTag("Stack", this.stack.serializeNBT());
        tag.setInteger("Mode", this.mode.ordinal());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Target"))
        {
            this.target.deserializeNBT(tag.getCompoundTag("Target"));
        }

        if (tag.hasKey("Stack"))
        {
            this.stack = new ItemStack(tag.getCompoundTag("Stack"));
        }

        if (tag.hasKey("Mode"))
        {
            this.mode = EnumUtils.getValue(tag.getInteger("Mode"), ItemMode.values(), ItemMode.TAKE);
        }
    }

    public static enum ItemMode
    {
        TAKE, GIVE
    }
}