package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiTextLabeledElement extends GuiTextElement
{
    public IKey label = IKey.EMPTY;

    public GuiTextLabeledElement(Minecraft mc, Consumer<String> callback)
    {
        super(mc, callback);
    }

    public GuiTextLabeledElement label(IKey label)
    {
        this.label = label;
        return this;
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);
        if (!this.field.isFocused() && this.field.getText().isEmpty()) {
            this.font.drawStringWithShadow(this.label.get(), (float)(this.area.x + 5), (float)(this.area.y + 6), 8947848);
        }
    }
}
