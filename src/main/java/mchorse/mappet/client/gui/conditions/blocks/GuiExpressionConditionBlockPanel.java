package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiExpressionConditionBlockPanel extends GuiAbstractConditionBlockPanel<ExpressionConditionBlock>
{
    public GuiTextElement expression;

    public GuiExpressionConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, ExpressionConditionBlock block)
    {
        super(mc, overlay, block);

        this.expression = new GuiTextElement(mc, 1000, (t) -> this.block.expression = t);
        this.expression.setText(block.expression);

        this.add(this.expression);
    }
}