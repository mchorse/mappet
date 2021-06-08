package mchorse.mappet.client.gui.conditions.utils;

import mchorse.mappet.api.conditions.blocks.PropertyConditionBlock;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

/**
 * Property block element
 *
 * This element doesn't add anything to itself, you need to
 * manually add those fields
 */
public class GuiPropertyBlockElement extends GuiTargetBlockElement<PropertyConditionBlock>
{
    public GuiElement compare;

    private GuiCirculateElement comparison;
    private GuiTrackpadElement value;
    private GuiTextElement expression;

    public GuiPropertyBlockElement(Minecraft mc, PropertyConditionBlock block)
    {
        super(mc, block);

        this.compare = Elements.row(mc, 5);
        this.comparison = new GuiCirculateElement(mc, this::toggleComparison);

        for (Comparison comparison : Comparison.values())
        {
            this.comparison.addLabel(comparison.stringify());
        }

        this.comparison.setValue(block.comparison.ordinal());
        this.value = new GuiTrackpadElement(mc, (v) -> this.block.value = v);
        this.value.setValue(block.value);

        this.expression = new GuiTextElement(mc, 1000, (t) -> this.block.expression = t);
        this.expression.setText(this.block.expression);
        this.expression.tooltip(IKey.lang("mappet.gui.conditions.expression_tooltip"));

        this.toggleComparison(this.comparison);
    }

    private void toggleComparison(GuiButtonElement b)
    {
        Comparison comparison = this.block.comparison;

        this.block.comparison = Comparison.values()[this.comparison.getValue()];

        if (
            (comparison == Comparison.EXPRESSION && this.block.comparison != Comparison.EXPRESSION) ||
            (comparison != Comparison.EXPRESSION && this.block.comparison == Comparison.EXPRESSION) ||
            this.compare.getChildren().isEmpty()
        ) {
            this.compare.removeAll();
            this.compare.add(Elements.column(mc, 5,
                Elements.label(IKey.lang("mappet.gui.conditions.comparison")), this.comparison)
            );

            if (this.block.comparison == Comparison.EXPRESSION)
            {
                this.compare.add(Elements.column(mc, 5,
                    Elements.label(IKey.lang("mappet.gui.conditions.expression")), this.expression)
                );
            }
            else
            {
                this.compare.add(Elements.column(mc, 5,
                    Elements.label(IKey.lang("mappet.gui.conditions.value")), this.value)
                );
            }

            if (this.compare.hasParent())
            {
                this.compare.getParentContainer().resize();
            }
        }
    }
}