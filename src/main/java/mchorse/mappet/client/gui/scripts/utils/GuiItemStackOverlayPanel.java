package mchorse.mappet.client.gui.scripts.utils;

import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

public class GuiItemStackOverlayPanel extends GuiOverlayPanel
{
    public GuiSlotElement pick;
    public GuiButtonElement insert;

    private GuiTextEditor editor;
    private ItemStack stack;

    public GuiItemStackOverlayPanel(Minecraft mc, IKey title, GuiTextEditor editor, ItemStack stack)
    {
        super(mc, title);

        this.editor = editor;
        this.stack = stack;

        this.pick = new GuiSlotElement(mc, 0, this::pickItem);
        this.pick.flex().wh(20, 20);
        this.pick.setStack(stack);
        this.insert = new GuiButtonElement(mc, IKey.lang("mappet.gui.scripts.overlay.insert"), this::insert);

        GuiElement row = Elements.row(mc, 5, this.pick, this.insert);

        row.flex().relative(this.content).y(1F, -30).w(1F).h(20).row(0).preferred(1);
        this.content.add(row);
    }

    private void pickItem(ItemStack stack)
    {
        this.stack = stack.copy();
    }

    private void insert(GuiButtonElement b)
    {
        this.close();

        if (!this.stack.isEmpty())
        {
            String nbt = this.stack.serializeNBT().toString();

            this.editor.pasteText(NBTTagString.quoteAndEscape(nbt));
        }
    }
}