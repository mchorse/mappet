package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UITextboxComponent extends UILabelBaseComponent
{
    public int maxLength = 32;

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiTextElement element = new GuiTextElement(mc, this.maxLength, (t) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setString(this.id, t);
                context.setLast(this.id);
                context.dirty(100);
            }
        });

        element.setText(this.label);

        return this.apply(element, context);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("MaxLength", this.maxLength);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.maxLength = tag.getInteger("MaxLength");
    }
}