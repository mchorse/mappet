package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.ItemTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiItemTriggerBlockPanel extends GuiAbstractTriggerBlockPanel<ItemTriggerBlock>
{
    public GuiTargetElement target;
    public GuiCirculateElement check;
    public GuiSlotElement slot;

    public GuiItemTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, ItemTriggerBlock block)
    {
        super(mc, overlay, block);

        this.target = new GuiTargetElement(mc, null).skipGlobal();
        this.check = new GuiCirculateElement(mc, this::toggleItemCheck);

        for (ItemTriggerBlock.ItemMode mode : ItemTriggerBlock.ItemMode.values())
        {
            this.check.addLabel(IKey.lang("mappet.gui.item_node.mode." + mode.name().toLowerCase()));
        }

        this.slot = new GuiSlotElement(mc, 0, (stack) -> this.block.stack = stack.copy());
        this.slot.marginTop(-2).marginBottom(-2);

        this.target.setTarget(block.target);
        this.check.setValue(block.mode.ordinal());
        this.slot.setStack(block.stack);

        this.add(Elements.row(mc, 5, this.slot, this.check));
        this.add(this.target.marginTop(12));
    }

    private void toggleItemCheck(GuiCirculateElement b)
    {
        this.block.mode = ItemTriggerBlock.ItemMode.values()[b.getValue()];
    }
}