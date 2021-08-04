package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIButtonComponent extends UILabelBaseComponent
{
    public UIButtonComponent()
    {}

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiButtonElement button = new GuiButtonElement(mc, IKey.str(this.label), (b) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setInteger(this.id, context.data.getInteger(this.id) + 1);
                context.dirty(this.id, this.updateDelay);
            }
        });

        return this.apply(button, context);
    }
}