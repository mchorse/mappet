package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiPropertyBlockElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiStateConditionBlockPanel extends GuiAbstractConditionBlockPanel<StateConditionBlock>
{
    public GuiTextElement id;
    public GuiPropertyBlockElement property;

    public GuiStateConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, StateConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiTextElement(mc, 1000, (t) -> this.block.id = t);
        this.id.setText(block.id);
        this.property = new GuiPropertyBlockElement(mc, block);

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.state.id")).marginTop(12), this.id);
        this.add(this.property.targeter.marginTop(12));
        this.add(this.property.compare.marginTop(12));
    }
}