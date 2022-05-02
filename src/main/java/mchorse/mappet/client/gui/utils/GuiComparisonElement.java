package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.utils.Comparison;
import mchorse.mappet.api.utils.ComparisonMode;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiComparisonElement extends GuiElement
{
    public Comparison comparison;

    private GuiCirculateElement mode;
    private GuiTrackpadElement value;
    private GuiTextElement expression;

    public GuiComparisonElement(Minecraft mc, Comparison comparison)
    {
        super(mc);

        this.comparison = comparison;

        this.mode = new GuiCirculateElement(mc, this::toggleComparison);

        for (ComparisonMode mode : ComparisonMode.values())
        {
            this.mode.addLabel(mode.stringify());
        }

        this.mode.setValue(comparison.comparison.ordinal());
        this.value = new GuiTrackpadElement(mc, (v) -> this.comparison.value = v);
        this.value.setValue(comparison.value);

        this.expression = new GuiTextElement(mc, 1000, (t) -> this.comparison.expression = t);
        this.expression.setText(this.comparison.expression);
        this.expression.tooltip(IKey.lang("mappet.gui.conditions.expression_tooltip"));

        this.flex().row(5);
        this.toggleComparison(this.mode);
    }

    private void toggleComparison(GuiCirculateElement b)
    {
        this.comparison.comparison = ComparisonMode.values()[b.getValue()];

        GuiElement insert = this.value;
        IKey label = IKey.lang("mappet.gui.conditions.value");

        if (this.comparison.comparison == ComparisonMode.EXPRESSION)
        {
            insert = this.expression;
            label = IKey.lang("mappet.gui.conditions.expression");
        }
        else if (this.comparison.comparison == ComparisonMode.IS_TRUE || this.comparison.comparison == ComparisonMode.IS_FALSE)
        {
            insert = null;
        }
        else if (this.comparison.comparison.isString)
        {
            insert = this.expression;
        }

        this.removeAll();
        this.add(Elements.column(this.mc, 5,
            Elements.label(IKey.lang("mappet.gui.conditions.comparison")), this.mode)
        );

        if (insert != null)
        {
            this.add(Elements.column(this.mc, 5, Elements.label(label), insert));
        }

        GuiElement container = this.getParentContainer();

        if (container != null)
        {
            container.resize();
        }
    }
}