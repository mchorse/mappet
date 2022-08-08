package mchorse.mappet.api.ui.components;

import com.caoccao.javet.annotations.V8Property;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.IconRegistry;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Clickable icon button component.
 *
 * <p>This component displays an icon, which allows users to trigger the handler
 * script. The value that gets written to UI context's data (if ID is present)
 * is how many times the icon button was pressed.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#icon(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var button = ui.icon("more").id("icon");
 *
 *        button.rxy(0.5, 0.5).wh(20, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *
 *        if (uiContext.getLast() === "icon")
 *        {
 *            // Get a set of all icons
 *            var icons = Java.from(Java.type("mchorse.mclib.client.gui.utils.IconRegistry").icons.keySet());
 *
 *            // Set a random icon
 *            var index = Math.floor(Math.random() * icons.size());
 *            var i = 0;
 *
 *            for (var icon in icons)
 *            {
 *                if (i == index)
 *                {
 *                    uiContext.get("icon").icon(icon);
 *
 *                    break;
 *                }
 *
 *                i += 1;
 *            }
 *        }
 *    }
 * }</pre>
 */
public class UIIconButtonComponent extends UIComponent
{
    @V8Property(name = "_icon")
    public String icon = "";

    public UIIconButtonComponent()
    {}

    /**
     * Change icon component's icon.
     *
     * <p>You can find out all available icons by entering following line into
     * Mappet's REPL (it returns a Java Set):</p>
     *
     * <pre>{@code
     *    Java.type("mchorse.mclib.client.gui.utils.IconRegistry").icons.keySet()
     * }</pre>
     *
     * <p>So using that piece of code, you can get create a GUI that shows
     * every icon with a tooltip:</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").background();
     *        var icons = Java.type("mchorse.mclib.client.gui.utils.IconRegistry").icons.keySet();
     *
     *        var grid = ui.grid(5);
     *
     *        grid.getCurrent().width(20).rxy(0.5, 0.5).w(245).anchor(0.5);
     *
     *        for each (var icon in icons)
     *        {
     *            grid.icon(icon).wh(20, 20).tooltip("Icon's ID: " + icon);
     *        }
     *
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     *
     * <p>A basic example:</p>
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("icon").icon("gear");
     * }</pre>
     *
     * @param icon The icon's ID.
     */
    public UIIconButtonComponent icon(String icon)
    {
        this.change("Icon");

        this.icon = icon;

        return this;
    }

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

        GuiIconElement button = (GuiIconElement) element;

        if (key.equals("Icon"))
        {
            button.both(this.getIcon());
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiIconElement button = new GuiIconElement(mc, this.getIcon(), (b) ->
        {
            if (!this.id.isEmpty())
            {
                this.populateData(context.data);
                context.dirty(this.id, this.updateDelay);
            }
        });

        return this.apply(button, context);
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    private Icon getIcon()
    {
        Icon icon = IconRegistry.icons.get(this.icon);

        if (icon == null)
        {
            icon = Icons.NONE;
        }

        return icon;
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

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Icon", this.icon);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Icon"))
        {
            this.icon = tag.getString("Icon");
        }
    }
}