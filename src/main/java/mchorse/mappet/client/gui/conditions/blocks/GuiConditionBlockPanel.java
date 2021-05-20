package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.ConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiConditionBlockPanel extends GuiAbstractBlockPanel<ConditionBlock>
{
    public GuiButtonElement condition;

    public GuiConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, ConditionBlock block)
    {
        super(mc, overlay, block);

        this.condition = new GuiButtonElement(mc, IKey.lang("mappet.gui.checker.edit"), this::openConditionEditor);

        this.add(this.condition);
    }

    private void openConditionEditor(GuiButtonElement b)
    {
        GuiConditionOverlayPanel panel = new GuiConditionOverlayPanel(this.mc, this.block.condition);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.55F, 0.75F);
    }
}