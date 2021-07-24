package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.ItemNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiItemNodePanel extends GuiEventBaseNodePanel<ItemNode>
{
    public GuiTargetElement target;
    public GuiCirculateElement check;
    public GuiSlotElement slot;

    public GuiItemNodePanel(Minecraft mc)
    {
        super(mc);

        this.target = new GuiTargetElement(mc, null).skipGlobal();
        this.check = new GuiCirculateElement(mc, this::toggleItemCheck);

        for (ItemNode.ItemMode mode : ItemNode.ItemMode.values())
        {
            this.check.addLabel(IKey.lang("mappet.gui.item_node.mode." + mode.name().toLowerCase()));
        }

        this.slot = new GuiSlotElement(mc, 0, (stack) -> this.node.stack = stack.copy());
        this.slot.marginTop(-2).marginBottom(-2);

        this.add(Elements.row(mc, 5, this.slot, this.check));
        this.add(this.target.marginTop(12));
        this.add(this.binary);
    }

    private void toggleItemCheck(GuiCirculateElement b)
    {
        this.node.mode = ItemNode.ItemMode.values()[b.getValue()];
    }

    @Override
    public void set(ItemNode node)
    {
        super.set(node);

        this.target.setTarget(node.target);
        this.check.setValue(node.mode.ordinal());
        this.slot.setStack(node.stack);
    }
}