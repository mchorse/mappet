package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Textbox UI component.
 *
 * <p>This component allows users to input a text line (for multi-line input
 * use {@link UITextareaComponent} component). This could be used to request
 * the player to input names.</p>
 *
 * <p>The value that gets written to UI context's data (if ID is present) is
 * the string that user typed into the component.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#textbox()} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var textbox = ui.textbox().id("textbox").tooltip("Enter your name:");
 *
 *        textbox.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *        var data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "textbox")
 *        {
 *            c.getSubject().send("Your name is: " + data.getString("textbox"));
 *        }
 *    }
 * }</pre>
 */
public class UITextboxComponent extends UILabelBaseComponent
{
    public int maxLength = 32;

    /**
     * Change component's max length (how many character max can be inputted).
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("textbox").maxLength(68);
     * }</pre>
     */
    public UITextboxComponent maxLength(int maxLength)
    {
        this.change("MaxLength");

        this.maxLength = maxLength;

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

        if (key.equals("Label"))
        {
            ((GuiTextElement) element).setText(this.label);
        }
        else if (key.equals("MaxLength"))
        {
            ((GuiTextElement) element).field.setMaxStringLength(this.maxLength);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiTextElement element = new GuiTextElement(mc, this.maxLength, (t) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setString(this.id, t);
                context.dirty(this.id, this.updateDelay);
            }
        });

        element.setText(this.label);

        return this.apply(element, context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            tag.setString(this.id, this.label);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("MaxLength", this.maxLength);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("MaxLength"))
        {
            this.maxLength = tag.getInteger("MaxLength");
        }
    }
}