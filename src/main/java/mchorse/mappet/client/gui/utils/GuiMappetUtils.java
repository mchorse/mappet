package mchorse.mappet.client.gui.utils;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiPromptOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiMappetUtils
{
    public static GuiTextElement fullWindowContext(GuiTextElement text, IKey title)
    {
        Minecraft mc = Minecraft.getMinecraft();

        text.context(() -> new GuiSimpleContextMenu(mc).action(Icons.EDIT, IKey.lang("mappet.gui.overlays.text_fullscreen"), () ->
        {
            GuiPromptOverlayPanel panel = new GuiPromptOverlayPanel(mc, title, text);
            GuiOverlay overlay = new GuiOverlay(mc, panel);

            panel.flex().w(1F, -30).h(54);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay);
        }));

        return text;
    }
}