package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Text area UI component.
 *
 * <p>This component allows users to input multiple lines of text (for single-line
 * input use {@link UITextboxComponent} component). This could be used to let players
 * input a multi-line description about themselves.</p>
 *
 * <p>The value that gets written to UI context's data (if ID is present) is
 * the multi-line string that user typed into the component.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#textarea(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var s = c.getSubject();
 *        var story = s.getStates().getString("story");
 *        var ui = mappet.createUI(c, "handler").background();
 *        var textarea = ui.textarea(story).id("textarea").tooltip("Tell us your story...");
 *
 *        // Don't send updates too often, only half a second after the player
 *        // stopped typing into a text area
 *        textarea.updateDelay(500);
 *        textarea.rxy(0.5, 0.5).wh(300, 200).anchor(0.5);
 *        s.openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *        var data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "textarea")
 *        {
 *            // Write down the input
 *            c.getSubject().getStates().setString("story", data.getString("textarea"));
 *        }
 *    }
 * }</pre>
 */
public class UITextareaComponent extends UILabelBaseComponent
{
    /**
     * Disable textarea's background.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("textarea").noBackground();
     * }</pre>
     */
    public UITextareaComponent noBackground()
    {
        this.hasBackground = false;

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
            ((GuiMultiTextElement) element).setText(this.label);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiMultiTextElement element = new GuiMultiTextElement(mc, (t) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setString(this.id, t);
                context.dirty(this.id, this.updateDelay);
            }
        });

        element.setText(this.label);
        element.background(this.hasBackground);

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
}