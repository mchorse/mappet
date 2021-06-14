package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.ItemConditionBlock;
import mchorse.mappet.api.conditions.blocks.TargetConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiTargetBlockElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiItemConditionBlockPanel extends GuiAbstractConditionBlockPanel<ItemConditionBlock>
{
    public GuiTargetBlockElement<TargetConditionBlock> property;
    public GuiCirculateElement check;
    public GuiSlotElement slot;

    public GuiItemConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, ItemConditionBlock block)
    {
        super(mc, overlay, block);

        this.property = new GuiTargetBlockElement<TargetConditionBlock>(mc, block).skipGlobal();
        this.check = new GuiCirculateElement(mc, this::toggleItemCheck);

        for (ItemConditionBlock.ItemCheck check : ItemConditionBlock.ItemCheck.values())
        {
            this.check.addLabel(IKey.lang("mappet.gui.conditions.item.types." + check.name().toLowerCase()));
        }

        this.check.setValue(block.check.ordinal());
        this.slot = new GuiSlotElement(mc, 0, (stack) -> this.block.stack = stack.copy());
        this.slot.marginTop(-2).marginBottom(-2);
        this.slot.setStack(block.stack);

        this.add(Elements.row(mc, 5, this.slot, this.check));
        this.add(this.property.targeter.marginTop(12));
    }

    private void toggleItemCheck(GuiCirculateElement b)
    {
        this.block.check = ItemConditionBlock.ItemCheck.values()[b.getValue()];
    }
}