package mchorse.mappet.client.gui.conditions.utils;

import mchorse.mappet.api.conditions.blocks.PropertyBlock;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mappet.api.conditions.utils.Target;
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
public class GuiPropertyBlockElement extends GuiElement
{
    public PropertyBlock block;

    public GuiElement targeter;
    private GuiCirculateElement target;
    private GuiTextElement selector;
    public GuiCirculateElement comparison;
    public GuiTrackpadElement value;

    public GuiPropertyBlockElement(Minecraft mc, PropertyBlock block)
    {
        super(mc);

        this.block = block;

        this.targeter = new GuiElement(mc);
        this.target = new GuiCirculateElement(mc, this::toggleTarget);

        for (Target target : Target.values())
        {
            this.target.addLabel(IKey.lang("mappet.gui.conditions.targets." + target.name().toLowerCase()));
        }

        this.target.setValue(block.target.ordinal());

        this.selector = new GuiTextElement(mc, 1000, (t) -> this.block.selector = t);
        this.selector.setText(block.selector);
        this.comparison = new GuiCirculateElement(mc, this::toggleComparison);

        for (Comparison comparison : Comparison.values())
        {
            this.comparison.addLabel(IKey.str(comparison.operation.sign));
        }

        this.comparison.setValue(block.comparison.ordinal());
        this.value = new GuiTrackpadElement(mc, (v) -> this.block.value = v.intValue());
        this.value.setValue(block.value);

        this.targeter.flex().column(5).stretch().vertical();
        this.updateTarget();
    }

    private void toggleTarget(GuiButtonElement b)
    {
        this.block.target = Target.values()[this.target.getValue()];

        this.updateTarget();
    }

    private void updateTarget()
    {
        this.targeter.removeAll();
        this.targeter.add(Elements.label(IKey.lang("mappet.gui.conditions.target")), this.target);

        if (this.block.target == Target.SELECTOR)
        {
            this.targeter.add(Elements.label(IKey.lang("mappet.gui.conditions.selector")), this.selector);
        }

        if (this.targeter.hasParent())
        {
            this.targeter.getParentContainer().resize();
        }
    }

    private void toggleComparison(GuiButtonElement b)
    {
        this.block.comparison = Comparison.values()[this.comparison.getValue()];
    }
}