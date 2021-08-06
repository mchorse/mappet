package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIStackComponent extends UIComponent
{
    public ItemStack stack = ItemStack.EMPTY;

    public UIStackComponent stack(IScriptItemStack stack)
    {
        return this.stack(stack == null ? null : stack.getMinecraftItemStack());
    }

    public UIStackComponent stack(ItemStack stack)
    {
        this.change("Stack");

        this.stack = stack == null ? ItemStack.EMPTY : stack.copy();

        return this;
    }

    @Override
    protected int getDefaultUpdateDelay()
    {
        return UIComponent.DELAY;
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
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        if (key.equals("Stack"))
        {
            ((GuiSlotElement) element).setStack(this.stack);
        }
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

        if (tag.hasKey("Stack"))
        {
            this.stack = new ItemStack(tag.getCompoundTag("Stack"));
        }
    }
}