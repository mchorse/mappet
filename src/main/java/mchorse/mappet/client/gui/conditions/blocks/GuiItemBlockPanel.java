package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.ItemBlock;
import mchorse.mappet.api.conditions.blocks.TargetBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.blocks.GuiAbstractBlockPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiTargetBlockElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiItemBlockPanel extends GuiAbstractBlockPanel<ItemBlock>
{
    public GuiTargetBlockElement<TargetBlock> property;
    public GuiCirculateElement check;
    public GuiSlotElement slot;

    public GuiItemBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, ItemBlock block)
    {
        super(mc, overlay, block);

        this.property = new GuiTargetBlockElement<TargetBlock>(mc, block).skipGlobal();
        this.check = new GuiCirculateElement(mc, this::toggleItemCheck);

        for (ItemBlock.ItemCheck check : ItemBlock.ItemCheck.values())
        {
            this.check.addLabel(IKey.lang("mappet.gui.conditions.item.types." + check.name().toLowerCase()));
        }

        this.check.setValue(block.check.ordinal());
        this.slot = new GuiSlotElement(mc, 0, overlay.inventory);
        this.slot.stackCallback((item) ->
        {
            item = item.copy();
            this.block.stack = item;
            this.slot.stack = item;
        });
        this.slot.marginTop(-2).marginBottom(-2);
        this.slot.stack = block.stack;

        this.add(Elements.row(mc, 5, this.slot, this.check));
        this.add(this.property.targeter.marginTop(12));
    }

    private void toggleItemCheck(GuiButtonElement b)
    {
        this.block.check = ItemBlock.ItemCheck.values()[this.check.getValue()];
    }
}