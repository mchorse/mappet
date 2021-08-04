package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIStackComponent extends UIBaseComponent
{
    public ItemStack stack = ItemStack.EMPTY;

    @Override
    protected int getDefaultUpdateDelay()
    {
        return UIBaseComponent.DELAY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiSlotElement element = new GuiSlotElement(mc, 0, (stack) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setTag(this.id, stack.serializeNBT());
                context.dirty(this.id, this.updateDelay);
            }
        });

        element.setStack(this.stack);

        return this.apply(element, context);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setTag("Stack", this.stack.serializeNBT());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.stack = new ItemStack(tag.getCompoundTag("Stack"));
    }

    public UIStackComponent stack(ItemStack stack)
    {
        this.stack = stack.copy();

        return this;
    }
}