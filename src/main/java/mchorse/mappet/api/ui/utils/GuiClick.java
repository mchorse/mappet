package mchorse.mappet.api.ui.utils;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

public class GuiClick extends GuiElement
{
    public UIComponent component;
    public UIContext context;

    public GuiClick(Minecraft mc, UIComponent component, UIContext context)
    {
        super(mc);

        this.component = component;
        this.context = context;
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        if (this.area.isInside(context) && !this.component.id.isEmpty())
        {
            this.context.data.setInteger(this.component.id + ".x", context.mouseX - this.area.x);
            this.context.data.setInteger(this.component.id + ".y", context.mouseY - this.area.y);
            this.context.data.setFloat(this.component.id + ".fx", (context.mouseX - this.area.x) / (float) this.area.w);
            this.context.data.setFloat(this.component.id + ".fy", (context.mouseY - this.area.y) / (float) this.area.h);
            this.context.dirty(this.component.id, this.component.updateDelay);

            return true;
        }

        return false;
    }
}