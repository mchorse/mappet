package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.ITextColoring;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class UILabelBaseComponent extends UIComponent
{
    public String label = "";

    private Integer color;
    private boolean textShadow = true;

    protected boolean hasBackground = true;

    /**
     * Change text color of this component by providing hex RGB.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set label's text color to toxic green
     *    uiContext.get("component").color(0x00ff00);
     * }</pre>
     */
    public UILabelBaseComponent color(int color)
    {
        return this.color(color, true);
    }

    /**
     * Change text color of this component by providing hex RGB.
     * Optionally enable text shadow.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set label's text color to black (and without text shadow)
     *    uiContext.get("component").color(0x000000, false);
     * }</pre>
     */
    public UILabelBaseComponent color(int color, boolean shadow)
    {
        this.change("Color", "TextShadow");

        this.color = color;
        this.textShadow = shadow;

        return this;
    }

    /**
     * Set label for label, toggle and text UI components, or change
     * the input value for textbox and textarea components.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Assuming that title is a label UI component
     *    uiContext.get("title").label("Application form");
     *
     *    // Assuming that prerequisites is a text UI component
     *    uiContext.get("prerequisites").label("This is an application form for enrolling into H.P. Lovecraft's book club.\n\n* - are required fields");
     *
     *    // Assuming that fullname is a textbox UI component
     *    uiContext.get("fullname").label("John Smith");
     *
     *    // Assuming that description is a textarea UI component
     *    uiContext.get("description").label("I'm John Smith, I'm from Alaska, and I like fishing.");
     *
     *    // Assuming that adult is a toggle UI component
     *    uiContext.get("adult").label("Adult");
     * }</pre>
     */
    public UILabelBaseComponent label(String label)
    {
        this.change("Label");

        this.label = label;

        return this;
    }

    @DiscardMethod
    protected String getLabel()
    {
        return TextUtils.processColoredText(this.label);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiElement apply(GuiElement element, UIContext context)
    {
        if (element instanceof ITextColoring && this.color != null)
        {
            ((ITextColoring) element).setColor(this.color, this.textShadow);
        }

        return super.apply(element, context);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        if (key.equals("Color") && element instanceof ITextColoring)
        {
            ((ITextColoring) element).setColor(this.color, this.textShadow);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Label", this.label);

        if (this.color != null)
        {
            tag.setInteger("Color", this.color);
        }

        tag.setBoolean("TextShadow", this.textShadow);

        tag.setBoolean("HasBackground", this.hasBackground);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Label"))
        {
            this.label = tag.getString("Label");
        }

        if (tag.hasKey("Color"))
        {
            this.color = tag.getInteger("Color");
        }

        if (tag.hasKey("TextShadow"))
        {
            this.textShadow = tag.getBoolean("TextShadow");
        }

        if (tag.hasKey("HasBackground"))
        {
            this.hasBackground = tag.getBoolean("HasBackground");
        }
    }
}