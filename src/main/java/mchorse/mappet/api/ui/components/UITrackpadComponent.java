package mchorse.mappet.api.ui.components;

import com.caoccao.javet.annotations.V8Property;
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
 * <p>The value that gets written to UI context's data (if ID is present) is
 * the number displayed in the field, if integer option is enabled, then an
 * integer will be written, or double if it's disabled.</p>
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
 *            // If integer wasn't enabled, you would use:
 *            // data.getDouble("number")
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
    @V8Property(name = "_value")
    public Double value;
    @V8Property(name = "_min")
    public Double min;
    @V8Property(name = "_max")
    public Double max;
    @V8Property(name = "_integer")
    public boolean integer;

    @V8Property(name = "_normal")
    public Double normal;
    @V8Property(name = "_weak")
    public Double weak;
    @V8Property(name = "_strong")
    public Double strong;
    @V8Property(name = "_increment")
    public Double increment;

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

    /**
     * Changes the amplitudes of this trackpad fields, i.e. how much value changes when
     * moving the cursor horizontally. Weak (<code>Alt</code> amplitude gets set 5 times
     * weaker than input value, and strong (<code>Shift</code>) amplitude gets set 5 times
     * stronger than input value.
     */
    public UITrackpadComponent amplitudes(double normal)
    {
        return this.amplitudes(normal, normal / 5, normal * 5);
    }

    /**
     * Changes the amplitudes of this trackpad fields, i.e. how much value changes when
     * moving the cursor horizontally.
     *
     * @param normal Value change per pixel when no modifiers is held.
     * @param weak Value change per pixel when alt is held.
     * @param strong Value change per pixel when shift is held.
     */
    public UITrackpadComponent amplitudes(double normal, double weak, double strong)
    {
        this.change("Normal", "Weak", "Strong");

        this.normal = normal;
        this.weak = weak;
        this.strong = strong;

        return this;
    }

    /**
     * Changes the incremental value of this trackpad fields, i.e. how much being added
     * or subtracted when user presses &lt; and &gt; buttons on the sides of the
     * trackpad value.
     *
     * @param increment Value change per click on increment buttons.
     */
    public UITrackpadComponent increment(double increment)
    {
        this.change("Increment");

        this.increment = increment;

        return this;
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
        else if (key.equals("Min") && this.min != null)
        {
            trackpad.min = this.min;
        }
        else if (key.equals("Max") && this.max != null)
        {
            trackpad.max = this.max;
        }
        else if (key.equals("Integer"))
        {
            trackpad.integer = this.integer;
        }
        else if (key.equals("Normal") && this.normal != null)
        {
            trackpad.normal = this.normal;
        }
        else if (key.equals("Weak") && this.weak != null)
        {
            trackpad.weak = this.weak;
        }
        else if (key.equals("Strong") && this.strong != null)
        {
            trackpad.strong = this.strong;
        }
        else if (key.equals("Increment") && this.increment != null)
        {
            trackpad.increment = this.increment;
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

        if (this.normal != null) element.normal = this.normal;
        if (this.weak != null) element.weak = this.weak;
        if (this.strong != null) element.strong = this.strong;
        if (this.increment != null) element.increment = this.increment;

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

        if (this.normal != null) tag.setDouble("Normal", this.normal);
        if (this.weak != null) tag.setDouble("Weak", this.weak);
        if (this.strong != null) tag.setDouble("Strong", this.strong);
        if (this.increment != null) tag.setDouble("Increment", this.increment);
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
        if (tag.hasKey("Normal")) this.normal = tag.getDouble("Normal");
        if (tag.hasKey("Weak")) this.weak = tag.getDouble("Weak");
        if (tag.hasKey("Strong")) this.strong = tag.getDouble("Strong");
        if (tag.hasKey("Increment")) this.increment = tag.getDouble("Increment");
    }
}