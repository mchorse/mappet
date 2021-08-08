package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Clickable button component.
 *
 * <p>This component displays a flat colored box with a label on top, which
 * allows users to trigger the handler script. The value that gets written
 * to UI context's data (if ID is present) is how many times the button
 * was pressed.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#button(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var button = ui.button("Start...").id("button");
 *
 *        button.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *
 *        if (uiContext.getLast() === "button")
 *        {
 *            var data = uiContext.getData();
 *            var pressed = data.getInt("button");
 *
 *            if (pressed >= 100)
 *            {
 *                c.send("We have a winner!");
 *            }
 *
 *            uiContext.get("button").label("You pressed: " + pressed);
 *        }
 *    }
 * }</pre>
 */
public class UIButtonComponent extends UILabelBaseComponent
{
    public UIButtonComponent()
    {}

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected boolean isDataReserved()
    {
        return true;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiButtonElement button = (GuiButtonElement) element;

        if (key.equals("Label"))
        {
            button.label = IKey.str(this.getLabel());
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiButtonElement button = new GuiButtonElement(mc, IKey.str(this.getLabel()), (b) ->
        {
            if (!this.id.isEmpty())
            {
                this.populateData(context.data);
                context.dirty(this.id, this.updateDelay);
            }
        });

        return this.apply(button, context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            tag.setInteger(this.id, tag.getInteger(this.id) + 1);
        }
    }
}