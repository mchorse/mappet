package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCraftingNodePanel extends GuiEventNodePanel<CraftingNode>
{
    public GuiTextElement crafting;

    public GuiCraftingNodePanel(Minecraft mc)
    {
        super(mc);

        this.crafting = new GuiTextElement(mc, 10000, (text) -> this.node.table = text);

        this.add(Elements.label(IKey.lang("mappet.gui.nodes.dialogue.crafting")).marginTop(12), this.crafting);
    }

    @Override
    public void set(CraftingNode node)
    {
        super.set(node);

        this.crafting.setText(node.table);
    }
}