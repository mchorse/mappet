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
    public float anchorX;
    public float anchorY;

    public UILabelComponent()
    {}

    public UILabelComponent color(int color)
    {
        this.change("Color");

        this.color = color;

        return this;
    }

    public UILabelComponent background(int color)
    {
        this.change("Background");

        this.background = color;

        return this;
    }

    public UILabelComponent labelAnchor(float anchorX, float anchorY)
    {
        this.change("AnchorX", "AnchorY");

        this.anchorX = anchorX;
        this.anchorY = anchorY;

        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiLabel label = Elements.label(IKey.str(this.getLabel()));

        if (this.color != null)
        {
            label.color(this.color);
        }

        if (this.background != null)
        {
            label.background(this.background);
        }

        label.anchor(this.anchorX, this.anchorY);

        return this.apply(label, context);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiLabel label = (GuiLabel) element;

        if (key.equals("Label"))
        {
            label.label = IKey.str(this.getLabel());
        }
        else if (key.equals("Color"))
        {
            label.color = this.color;
        }
        else if (key.equals("Background"))
        {
            label.background = this.background;
        }
        else if (key.equals("AnchorX"))
        {
            label.anchorX = this.anchorX;
        }
        else if (key.equals("AnchorY"))
        {
            label.anchorY = this.anchorY;
        }
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

        tag.setFloat("AnchorX", this.anchorX);
        tag.setFloat("AnchorY", this.anchorY);
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

        if (tag.hasKey("AnchorX"))
        {
            this.anchorX = tag.getFloat("AnchorX");
        }

        if (tag.hasKey("AnchorY"))
        {
            this.anchorY = tag.getFloat("AnchorY");
        }
    }
}