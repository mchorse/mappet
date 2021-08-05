package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UITextareaComponent extends UILabelBaseComponent
{
    @Override
    protected int getDefaultUpdateDelay()
    {
        return UIBaseComponent.DELAY;
    }

    @Override
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
        element.background();

        return this.apply(element, context);
    }
}