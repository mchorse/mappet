package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Label UI component.
 *
 * <p>This component allows you to input one line of text. You can use Minecraft's
 * formatting using "[" symbol instead of section field.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#label(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI().background();
 *        var label = ui.label("Welcome, [l" + c.getSubject().getName() + "[r!");
 *
 *        label.rxy(0.5, 0.5).wh(100, 20).anchor(0.5);
 *        label.color(0x00ee22).background(0x88000000).labelAnchor(0.5);
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
public class UILabelComponent extends UILabelBaseComponent
{
    public Integer background;
    public float anchorX;
    public float anchorY;

    public UILabelComponent()
    {}

    /**
     * Change background color of this label component by providing hex ARGB.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Add a half transparent black background
     *    uiContext.get("label").background(0x88000000);
     * }</pre>
     */
    public UILabelComponent background(int background)
    {
        this.change("Background");

        this.background = background;

        return this;
    }

    /**
     * Change text's anchor point which determines where text will be rendered
     * relative to component's frame both vertically and horizontally.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Position the label's text in the middle of its frame
     *    uiContext.get("label").labelAnchor(0.5);
     * }</pre>
     */
    public UILabelComponent labelAnchor(float anchor)
    {
        return this.labelAnchor(anchor, anchor);
    }

    /**
     * Change text's anchor point which determines where text will be rendered
     * relative to component's frame, with separate vertical and horizontal
     * anchors.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Position the label's text in the middle only vertically
     *    uiContext.get("label").labelAnchor(0, 0.5);
     * }</pre>
     */
    public UILabelComponent labelAnchor(float anchorX, float anchorY)
    {
        this.change("AnchorX", "AnchorY");

        this.anchorX = anchorX;
        this.anchorY = anchorY;

        return this;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiLabel label = Elements.label(IKey.str(this.getLabel()));

        if (this.background != null)
        {
            label.background(this.background);
        }

        label.anchor(this.anchorX, this.anchorY);

        return this.apply(label, context);
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiLabel label = (GuiLabel) element;

        if (key.equals("Label"))
        {
            label.label = IKey.str(this.getLabel());
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
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.background != null)
        {
            tag.setInteger("Background", this.background);
        }

        tag.setFloat("AnchorX", this.anchorX);
        tag.setFloat("AnchorY", this.anchorY);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

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