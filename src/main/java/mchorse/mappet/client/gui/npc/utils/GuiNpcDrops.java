package mchorse.mappet.client.gui.npc.utils;

import mchorse.mappet.api.npcs.NpcDrop;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Supplier;

public class GuiNpcDrops extends GuiElement
{
    public GuiElement element;

    private Supplier<GuiInventoryElement> inventory;
    private List<NpcDrop> drops;

    public GuiNpcDrops(Minecraft mc, Supplier<GuiInventoryElement> inventory)
    {
        super(mc);

        this.inventory = inventory;

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.npcs.drops.title"));
        GuiIconElement add = new GuiIconElement(mc, Icons.ADD, (b) -> this.addDrop(new NpcDrop(), true));
        add.flex().wh(10, 8);

        GuiElement row = Elements.row(mc, 5, 0, this.font.FONT_HEIGHT, label, add);
        this.element = new GuiElement(mc);

        row.flex().row(5).preferred(0);
        this.element.flex().column(5).vertical().stretch();

        this.flex().column(5).vertical().stretch();
        this.add(row, this.element);
    }

    private void addDrop(NpcDrop drop, boolean insert)
    {
        if (this.inventory == null)
        {
            return;
        }

        if (insert)
        {
            this.drops.add(drop);
        }

        GuiElement row = Elements.row(this.mc, 5, 0, 24);
        GuiSlotElement slot = new GuiSlotElement(this.mc, 0, this.inventory.get());
        GuiTrackpadElement chance = new GuiTrackpadElement(this.mc, (v) -> drop.chance = v.floatValue() / 100F);

        chance.setValue(drop.chance * 100);
        chance.limit(0, 100).flex().h(24);
        slot.stackCallback((item) ->
        {
            item = item.copy();
            drop.stack = item;
            slot.stack = item;
        });
        slot.stack = drop.stack;
        row.context(() -> new GuiSimpleContextMenu(this.mc).action(Icons.REMOVE, IKey.lang("mappet.gui.npcs.drops.context.remove"), () ->
        {
            int index = this.element.getChildren().indexOf(row);

            if (index != -1)
            {
                this.drops.remove(index);
                row.removeFromParent();
                this.getParentContainer().resize();
            }
        }, 0xff0022));

        row.add(slot, chance);
        this.element.add(row);

        this.getParentContainer().resize();
    }

    public void set(List<NpcDrop> drops)
    {
        this.drops = drops;

        this.element.removeAll();

        if (drops != null)
        {
            for (NpcDrop drop : this.drops)
            {
                this.addDrop(drop, false);
            }
        }
    }
}