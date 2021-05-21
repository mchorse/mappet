package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class GuiReactionNodePanel extends GuiDialogueNodePanel
{
    public GuiNestedEdit morph;
    public GuiToggleElement read;
    public GuiTextElement marker;

    public GuiReactionNodePanel(Minecraft mc, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        super(mc);

        this.morph = new GuiNestedEdit(mc, (b) -> this.openMorphMenu(b, morphs));
        this.read = new GuiToggleElement(mc, IKey.lang("mappet.gui.nodes.dialogue.read"), (b) -> this.get().read = b.isToggled());
        this.read.flex().h(20);
        this.marker = new GuiTextElement(mc, (t) -> this.get().marker = t);
        this.marker.filename().tooltip(IKey.lang("mappet.gui.nodes.dialogue.marker_tooltip"), Direction.TOP);

        this.add(this.morph);
        this.add(
            Elements.label(IKey.lang("mappet.gui.nodes.dialogue.marker")).marginTop(12),
            Elements.row(mc, 5, this.marker, this.read)
        );
    }

    private void openMorphMenu(boolean editing, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        GuiCreativeMorphsMenu menu = morphs.get();

        if (menu.hasParent())
        {
            return;
        }

        GuiElement parent = this.getParentContainer();

        GuiBase.getCurrent().unfocus();

        menu.callback = (morph) ->
        {
            morph = MorphUtils.copy(morph);

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
        this.read.toggled(this.get().read);
        this.marker.setText(this.get().marker);
    }
}