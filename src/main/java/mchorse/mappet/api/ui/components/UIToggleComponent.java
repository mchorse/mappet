package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Toggle UI component.
 *
 * <p>This component allows users to input boolean value (yes/no, true/false,
 * <code>1</code>/<code>0</code>). The value that gets written to UI context's data
 * (if ID is present) is boolean.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#toggle(String)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var trackpad = ui.toggle("Show", false).id("toggle");
 *        var label = ui.label("You can see this label").id("label");
 *
 *        trackpad.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        label.rx(0.5).ry(0.5, 25).wh(160, 20).anchor(0.5).labelAnchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *        var data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "toggle")
 *        {
 *            uiContext.get("label").visible(uiContext.getData().getBoolean("toggle"));
 *        }
 *    }
 * }</pre>
 */
public class UIToggleComponent extends UILabelBaseComponent
{
    public boolean state;

    /**
     * Change component's toggled state.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("toggle").state(true);
     * }</pre>
     */
    public UIToggleComponent state(boolean state)
    {
        this.change("State");

        this.state = state;

        return this;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiToggleElement toggle = (GuiToggleElement) element;

        if (key.equals("Label"))
        {
            toggle.label = IKey.str(this.getLabel());
        }
        else if (key.equals("State"))
        {
            toggle.toggled(this.state);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiToggleElement toggle = new GuiToggleElement(mc, IKey.str(this.getLabel()), (b) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setBoolean(this.id, b.isToggled());
                context.dirty(this.id, this.updateDelay);
            }
        });

        toggle.toggled(this.state);

        return this.apply(toggle, context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            tag.setBoolean(this.id, this.state);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setBoolean("State", this.state);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("State"))
        {
            this.state = tag.getBoolean("State");
        }
    }
}