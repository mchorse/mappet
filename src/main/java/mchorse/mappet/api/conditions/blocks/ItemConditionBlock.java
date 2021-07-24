package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mappet.utils.InventoryUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemConditionBlock extends TargetConditionBlock
{
    public ItemStack stack = ItemStack.EMPTY;
    public ItemCheck check = ItemCheck.HELD;

    @Override
    public boolean evaluateBlock(DataContext context)
    {
        if (this.target.mode != TargetMode.GLOBAL)
        {
            EntityPlayer player = this.target.getPlayer(context);

            if (player != null)
            {
                if (this.check == ItemCheck.HELD)
                {
                    ItemStack main = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                    ItemStack off = player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

                    boolean a = InventoryUtils.areStacksSimilar(main, this.stack);
                    boolean b = InventoryUtils.areStacksSimilar(off, this.stack);

                    return a || b;
                }
                else if (this.check == ItemCheck.EQUIPMENT)
                {
                    return InventoryUtils.countItems(player.inventory.armorInventory, this.stack, true) >= this.stack.getCount();
                }

                return InventoryUtils.countItems(player.inventory.mainInventory, this.stack, true) >= this.stack.getCount();
            }
        }

        return false;
    }

    @Override
    protected TargetMode getDefaultTarget()
    {
        return TargetMode.SUBJECT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        String name = this.stack.getDisplayName();

        if (this.check == ItemCheck.HELD)
        {
            return I18n.format("mappet.gui.conditions.item.holds", name);
        }
        else if (this.check == ItemCheck.EQUIPMENT)
        {
            return I18n.format("mappet.gui.conditions.item.equipment", name);
        }

        return I18n.format("mappet.gui.conditions.item.inventory", name + "x" + this.stack.getCount());
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setTag("Stack", this.stack.serializeNBT());
        tag.setInteger("Check", this.check.ordinal());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.stack = new ItemStack(tag.getCompoundTag("Stack"));
        this.check = EnumUtils.getValue(tag.getInteger("Check"), ItemCheck.values(), ItemCheck.HELD);
    }

    public static enum ItemCheck
    {
        HELD, EQUIPMENT, INVENTORY
    }
}