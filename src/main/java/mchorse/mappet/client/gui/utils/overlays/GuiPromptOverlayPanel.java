package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiPromptOverlayPanel extends GuiOverlayPanel
{
    public GuiTextElement text;

    public GuiPromptOverlayPanel(Minecraft mc, IKey title, GuiTextElement element)
    {
        super(mc, title);

        this.text = new GuiTextElement(mc, element.field.getMaxStringLength(), (t) ->
        {
            element.field.setText(t);

            if (element.callback != null)
            {
                element.callback.accept(t);
            }
        });
        this.text.setText(element.field.getText());

        this.text.flex().relative(this.content).y(1F, -30).w(1F);
        this.content.add(this.text);
    }
}