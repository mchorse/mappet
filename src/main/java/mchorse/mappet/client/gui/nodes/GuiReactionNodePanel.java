package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class GuiReactionNodePanel extends GuiDialogueNodePanel
{
    public GuiNestedEdit morph;

    public GuiReactionNodePanel(Minecraft mc, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        super(mc);

        this.morph = new GuiNestedEdit(mc, (b) -> this.openMorphMenu(b, morphs));

        this.add(this.morph);
    }

    private void openMorphMenu(boolean editing, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        GuiCreativeMorphsMenu menu = morphs.get();

        if (menu.hasParent())
        {
            return;
        }

        GuiElement parent = this.getParentContainer();

        menu.callback = (morph) ->
        {
            morph = morph.copy();

            this.get().morph = morph;
            this.morph.setMorph(morph);
        };
        menu.flex().reset().relative(parent).wh(1F, 1F);
        menu.resize();
        menu.setSelected(this.get().morph);

        if (editing)
        {
            menu.enterEditMorph();
        }

        parent.add(menu);
    }

    public ReactionNode get()
    {
        return (ReactionNode) this.node;
    }

    @Override
    public void set(DialogueNode node)
    {
        super.set(node);

        this.morph.setMorph(this.get().morph);
    }
}