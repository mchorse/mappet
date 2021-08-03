package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UILabelComponent extends UILabelBaseComponent
{
    public Integer color;
    public Integer background;

    public UILabelComponent()
    {}

    public UILabelComponent color(int color)
    {
        this.color = color;

        return this;
    }

    public UILabelComponent background(int color)
    {
        this.background = color;

        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiLabel label = Elements.label(IKey.str(this.label));

        if (this.color != null)
        {
            label.color(this.color);
        }

        if (this.background != null)
        {
            label.background(this.background);
        }

        return this.apply(label, context);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.color != null)
        {
            tag.setInteger("Color", this.color);
        }

        if (this.background != null)
        {
            tag.setInteger("Background", this.background);
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Color"))
        {
            this.color = tag.getInteger("Color");
        }

        if (tag.hasKey("Background"))
        {
            this.background = tag.getInteger("Background");
        }
    }
}