package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

public class UITrackpadComponent extends UIBaseComponent
{
    public Double value;
    public Double min;
    public Double max;
    public boolean integer;

    public UITrackpadComponent value(double value)
    {
        this.value = value;

        return this;
    }

    public UITrackpadComponent min(double min)
    {
        this.min = min;

        return this;
    }

    public UITrackpadComponent max(double max)
    {
        this.max = max;

        return this;
    }

    public UITrackpadComponent integer()
    {
        return this.integer(true);
    }

    public UITrackpadComponent integer(boolean integer)
    {
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
        return UIBaseComponent.DELAY;
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

        this.integer = tag.getBoolean("Integer");
    }
}