package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Consumer;

/**
 * Trackpad UI component.
 *
 * <p>This component allows users to input numerical values (integer and double),
 * with optionally a limit range between min and max. Users can also use
 * arrow buttons on the side to increment and decrement the value, and
 * type in value manually.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#trackpad()} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var trackpad = ui.trackpad(5).id("number").limit(0, 25, true);
 *
 *        trackpad.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *        var data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "number")
 *        {
 *            if (data.getInt("number") === 19)
 *            {
 *                c.getSubject().send("21");
 *            }
 *        }
 *    }
 * }</pre>
 */
public class UITrackpadComponent extends UIComponent
{
    public Double value;
    public Double min;
    public Double max;
    public boolean integer;

    /**
     * Set the value that of trackpad component.
     */
    public UITrackpadComponent value(double value)
    {
        this.change("Value");

        this.value = value;

        return this;
    }

    /**
     * Set the minimum that this trackpad component can let the user pick.
     */
    public UITrackpadComponent min(double min)
    {
        this.change("Min");

        this.min = min;

        return this;
    }

    /**
     * Set the maximum that this trackpad component can let the user pick.
     */
    public UITrackpadComponent max(double max)
    {
        this.change("Max");

        this.max = max;

        return this;
    }

    /**
     * Set this trackpad component to accept only whole numbers.
     */
    public UITrackpadComponent integer()
    {
        return this.integer(true);
    }

    /**
     * Toggle integer option, when passed <code>true</code> then this trackpad
     * component will accept only whole numbers, and when passed <code>false</code>,
     * then both whole and floating point numbers can be accepted by this trackpad.
     */
    public UITrackpadComponent integer(boolean integer)
    {
        this.change("Integer");

        this.integer = integer;

        return this;
    }

    /**
     * Convenience method that allows to set minimum and maximum, i.e. value range,
     * that this trackpad field can accept.
     */
    public UITrackpadComponent limit(double min, double max)
    {
        return this.min(min).max(max);
    }

    /**
     * Convenience method that allows to set minimum, maximum, and integer options
     * that this trackpad field can accept.
     */
    public UITrackpadComponent limit(double min, double max, boolean integer)
    {
        return this.min(min).max(max).integer(integer);
    }

    @Override
    @DiscardMethod
    protected int getDefaultUpdateDelay()
    {
        return UIComponent.DELAY;
    }

    @Override
    @DiscardMethod
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
    @DiscardMethod
    @SideOnly(Side.CLIENT)
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
    @DiscardMethod
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
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.value != null) tag.setDouble("Value", this.value);
        if (this.min != null) tag.setDouble("Min", this.min);
        if (this.max != null) tag.setDouble("Max", this.max);

        tag.setBoolean("Integer", this.integer);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Value")) this.value = tag.getDouble("Value");
        if (tag.hasKey("Min")) this.min = tag.getDouble("Min");
        if (tag.hasKey("Max")) this.max = tag.getDouble("Max");
        if (tag.hasKey("Integer")) this.integer = tag.getBoolean("Integer");
    }
}