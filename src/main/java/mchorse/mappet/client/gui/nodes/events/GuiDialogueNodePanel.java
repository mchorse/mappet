package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.DialogueNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueNodePanel extends GuiDataNodePanel<DialogueNode>
{
    public GuiDialogueNodePanel(Minecraft mc, GuiMappetDashboardPanel parentPanel)
    {
        super(mc, parentPanel);
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.dialogue");
    }

    @Override
    protected ContentType getType()
    {
        return ContentType.DIALOGUE;
    }
}