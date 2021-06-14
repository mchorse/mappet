package mchorse.mappet.client.gui.utils;

import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GuiItemsElement extends GuiElement
{
    public GuiElement stacks;

    private List<ItemStack> items;

    public GuiItemsElement(Minecraft mc, IKey title, List<ItemStack> items)
    {
        super(mc);

        GuiLabel label = Elements.label(title);
        GuiIconElement add = new GuiIconElement(mc, Icons.ADD, (b) ->
        {
            this.items.add(ItemStack.EMPTY);
            this.addItem(ItemStack.EMPTY);
            this.getParentContainer().resize();
        });
        add.flex().wh(10, 8);

        GuiElement row = Elements.row(mc, 5, 0, this.font.FONT_HEIGHT, label, add);
        this.stacks = new GuiElement(mc);

        label.flex().h(0);
        row.flex().row(5).preferred(0);
        this.stacks.flex().grid(5).width(24).resizes(true);

        this.flex().column(5).vertical().stretch();
        this.add(row, this.stacks);

        this.set(items);
    }

    public void set(List<ItemStack> items)
    {
        this.stacks.removeAll();

        this.items = items;

        if (this.items != null)
        {
            for (ItemStack stack : this.items)
            {
                this.addItem(stack);
            }
        }
    }

    public void addItem(ItemStack stack)
    {
        GuiSlotElement slot = new GuiSlotElement(this.mc, 0, null);

        slot.callback = (item) ->
        {
            int index = this.stacks.getChildren().indexOf(slot);

            if (index != -1)
            {
                this.items.set(index, item.copy());
            }
        };
        slot.setStack(stack);
        slot.context(() -> slot.createDefaultSlotContextMenu().action(Icons.REMOVE, IKey.lang("mappet.gui.items.context.remove"), () ->
        {
            int index = this.stacks.getChildren().indexOf(slot);

            if (index != -1)
            {
                this.items.remove(index);
                slot.removeFromParent();
                this.getParentContainer().resize();
            }
        }, Colors.NEGATIVE));

        this.stacks.add(slot);
    }
}
