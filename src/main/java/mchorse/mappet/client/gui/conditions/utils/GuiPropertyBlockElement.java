package mchorse.mappet.client.gui.conditions.utils;

import mchorse.mappet.api.conditions.blocks.PropertyBlock;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

/**
 * Property block element
 *
 * This element doesn't add anything to itself, you need to
 * manually add those fields
 */
public class GuiPropertyBlockElement extends GuiTargetBlockElement<PropertyBlock>
{
    public GuiCirculateElement comparison;
    public GuiTrackpadElement value;

    public GuiPropertyBlockElement(Minecraft mc, PropertyBlock block)
    {
        super(mc, block);

        this.comparison = new GuiCirculateElement(mc, this::toggleComparison);

        for (Comparison comparison : Comparison.values())
        {
            this.comparison.addLabel(IKey.str(comparison.operation.sign));
        }

        this.comparison.setValue(block.comparison.ordinal());
        this.value = new GuiTrackpadElement(mc, (v) -> this.block.value = v.intValue());
        this.value.setValue(block.value);
    }

    private void toggleComparison(GuiButtonElement b)
    {
        this.block.comparison = Comparison.values()[this.comparison.getValue()];
    }
}