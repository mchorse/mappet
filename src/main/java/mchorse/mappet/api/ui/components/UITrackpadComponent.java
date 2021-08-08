package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;

public class UITrackpadComponent extends UIComponent
{
    public Double value;
    public Double min;
    public Double max;
    public boolean integer;

    public UITrackpadComponent value(double value)
    {
        this.change("Value");

        this.value = value;

        return this;
    }

    public UITrackpadComponent min(double min)
    {
        this.change("Min");

        this.min = min;

        return this;
    }

    public UITrackpadComponent max(double max)
    {
        this.change("Max");

        this.max = max;

        return this;
    }

    public UITrackpadComponent integer()
    {
        return this.integer(true);
    }

    public UITrackpadComponent integer(boolean integer)
    {
        this.change("Integer");

        this.integer = integer;

        return this;
    }

    public UITrackpadComponent limit(double min, double max)
    {
        return this.min(min).max(max);
    }

    public UITrackpadComponent limit(double min, double max, boolean integer)
    {
        return this.min(min).max(max).integer(integer);
    }

    @Override
    protected int getDefaultUpdateDelay()
    {
        return UIComponent.DELAY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiTrackpadElement trackpad = (GuiTrackpadElement) element;

        if (key.equals("Value") && this.value != null)
        {
            trackpad.setValue(this.value);
        }
        if (key.equals("Min") && this.min != null)
        {
            trackpad.min = this.min;
        }
        if (key.equals("Max") && this.max != null)
        {
            trackpad.max = this.max;
        }
        if (key.equals("Integer"))
        {
            trackpad.integer = this.integer;
        }
    }

    @Override
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiTrackpadElement element = new GuiTrackpadElement(mc, (Consumer<Double>) null);

        element.callback = (v) ->
        {
            if (!this.id.isEmpty())
            {
                if (element.integer)
                {
                    context.data.setInteger(this.id, v.intValue());
                }
                else
                {
                    context.data.setDouble(this.id, v);
                }

                context.dirty(this.id, this.updateDelay);
            }
        };

        if (this.value != null) element.setValue(this.value);
        if (this.min != null) element.min = this.min;
        if (this.max != null) element.max = this.max;

        element.integer = this.integer;

        return this.apply(element, context);
    }

    @Override
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            if (this.integer)
            {
                tag.setInteger(this.id, this.value == null ? 0 : this.value.intValue());
            }
            else
            {
                tag.setDouble(this.id, this.value == null ? 0 : this.value);
            }
        }
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.value != null) tag.setDouble("Value", this.value);
        if (this.min != null) tag.setDouble("Min", this.min);
        if (this.max != null) tag.setDouble("Max", this.max);

        tag.setBoolean("Integer", this.integer);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Value")) this.value = tag.getDouble("Value");
        if (tag.hasKey("Min")) this.min = tag.getDouble("Min");
        if (tag.hasKey("Max")) this.max = tag.getDouble("Max");
        if (tag.hasKey("Integer")) this.integer = tag.getBoolean("Integer");
    }
}