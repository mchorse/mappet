package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.npc.utils.GuiNpcDrops;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class GuiNpcGeneralPanel extends GuiNpcPanel
{
    public GuiTextElement faction;
    public GuiNestedEdit morph;
    public GuiNpcDrops drops;
    public GuiTrackpadElement xp;

    public GuiNpcGeneralPanel(Minecraft mc, Supplier<GuiInventoryElement> inventory, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        super(mc);

        this.faction = new GuiTextElement(mc, (t) -> this.state.faction = t);
        this.morph = new GuiNestedEdit(mc, (b) -> this.openMorphMenu(b, morphs));
        this.drops = new GuiNpcDrops(mc, inventory);
        this.xp = new GuiTrackpadElement(mc, (v) -> this.state.xp = v.intValue());
        this.xp.limit(0).integer();

        this.scroll.add(Elements.label(IKey.lang("mappet.gui.npcs.general.faction")), this.faction);
        this.scroll.add(Elements.label(IKey.lang("mappet.gui.npcs.general.morph")).marginTop(12), this.morph);
        this.scroll.add(this.drops.marginTop(12));
        this.scroll.add(Elements.label(IKey.lang("mappet.gui.npcs.general.xp")).marginTop(12), this.xp);
    }

    private void openMorphMenu(boolean editing, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        GuiCreativeMorphsMenu menu = morphs.get();

        if (menu.hasParent())
        {
            return;
        }

        GuiBase.getCurrent().unfocus();

        GuiElement parent = this.getParentContainer();

        menu.callback = (morph) ->
        {
            this.state.morph = morph;
            this.morph.setMorph(morph);
        };
        menu.flex().reset().relative(parent).wh(1F, 1F);
        menu.resize();
        menu.setSelected(this.state.morph);

        if (editing)
        {
            menu.enterEditMorph();
        }

        parent.add(menu);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.faction.setText(state.faction);
        this.morph.setMorph(state.morph);
        this.drops.set(state.drops);
        this.xp.setValue(state.xp);
    }
}