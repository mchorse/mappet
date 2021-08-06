package mchorse.mappet.api.ui.utils;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.ui.components.UIParentComponent;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIRootComponent extends UIParentComponent
{
    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiElement element = new GuiElement(mc);

        for (UIComponent component : this.getChildComponents())
        {
            GuiElement created = component.create(mc, context);

            created.flex().relative(element);
            element.add(created);
        }

        return element;
    }
}