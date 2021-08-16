package mchorse.mappet.client.gui.scripts.utils;

import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.utils.overlays.GuiSoundOverlayPanel;
import net.minecraft.client.Minecraft;

public class GuiScriptSoundOverlayPanel extends GuiSoundOverlayPanel
{
    private GuiTextEditor editor;

    public GuiScriptSoundOverlayPanel(Minecraft mc, GuiTextEditor editor)
    {
        super(mc, null);

        this.editor = editor;
        this.set(editor.getSelectedText().replaceAll("\"", ""));
    }

    @Override
    public void onClose()
    {
        super.onClose();

        if (!this.rls.list.isDeselected() && this.rls.list.getIndex() > 0)
        {
            String current = this.editor.getSelectedText().trim();
            String result = this.rls.list.getCurrentFirst();

            if (current.startsWith("\""))
            {
                result = "\"" + result;
            }

            if (current.endsWith("\""))
            {
                result += "\"";
            }

            this.editor.pasteText(result);
        }
    }
}