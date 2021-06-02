package mchorse.mappet.client.gui.utils.text;

import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiClickableText extends GuiText
{
    private Consumer<GuiClickableText> callback;

    public GuiClickableText(Minecraft mc)
    {
        super(mc);
    }

    public GuiClickableText callback(Consumer<GuiClickableText> callback)
    {
        this.callback = callback;

        return this;
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        if (context.mouseButton == 0 && this.area.isInside(context))
        {
            if (this.callback != null)
            {
                this.callback.accept(this);
            }

            return true;
        }

        return false;
    }
}