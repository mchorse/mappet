package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Supplier;

public class GuiItemsElement extends GuiElement
{
    public GuiElement stacks;

    private List<ItemStack> items;
    private Supplier<GuiInventoryElement> inventory;

    public GuiItemsElement(Minecraft mc, IKey title, List<ItemStack> items, Supplier<GuiInventoryElement> inventory)
    {
        super(mc);

        this.inventory = inventory;

        GuiIconElement add = new GuiIconElement(mc, Icons.ADD, (b) ->
        {
            this.items.add(ItemStack.EMPTY);
            this.addItem(ItemStack.EMPTY);
            this.getParentContainer().resize();
        });
        GuiLabel label = Elements.label(title).anchor(0, 0.5F);
        GuiElement row = Elements.row(mc, 5, 0, 20, label, add);
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
        if (this.inventory == null)
        {
            return;
        }

        GuiSlotElement slotElement = new GuiSlotElement(this.mc, 0, this.inventory.get());

        slotElement.stackCallback((item) ->
        {
            int index = this.stacks.getChildren().indexOf(slotElement);

            if (index != -1)
            {
                this.items.set(index, item);
                slotElement.stack = item;
            }
        });
        slotElement.stack = stack;
        slotElement.context(() -> new GuiSimpleContextMenu(this.mc).action(Icons.REMOVE, IKey.str("Remove item"), () ->
        {
            int index = this.stacks.getChildren().indexOf(slotElement);

            if (index != -1)
            {
                this.items.remove(index);
                slotElement.removeFromParent();
                this.getParentContainer().resize();
            }
        }));

        this.stacks.add(slotElement);
    }
}
