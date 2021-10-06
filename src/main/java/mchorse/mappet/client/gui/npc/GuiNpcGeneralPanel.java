package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.npc.utils.GuiNpcDrops;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiNpcGeneralPanel extends GuiNpcPanel
{
    public GuiButtonElement faction;
    public GuiNestedEdit morph;
    public GuiNpcDrops drops;
    public GuiTrackpadElement xp;

    public GuiNpcGeneralPanel(Minecraft mc)
    {
        super(mc);

        this.faction = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.faction"), (t) -> this.openFactions());
        this.morph = new GuiNestedEdit(mc, this::openMorphMenu);
        this.drops = new GuiNpcDrops(mc);
        this.xp = new GuiTrackpadElement(mc, (v) -> this.state.xp = v.intValue());
        this.xp.limit(0).integer();

        this.add(Elements.label(IKey.lang("mappet.gui.npcs.general.faction")), this.faction);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.general.morph")).marginTop(12), this.morph);
        this.add(this.drops.marginTop(12));
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.general.xp")).marginTop(12), this.xp);
    }

    private void openFactions()
    {
        GuiMappetUtils.openPicker(ContentType.FACTION, this.state.faction, (name) -> this.state.faction = name);
    }

    private void openMorphMenu(boolean editing)
    {
        GuiMappetDashboard.get(this.mc).openMorphMenu(this.morph.getParentContainer(), editing, this.state.morph, this::setMorph);
    }

    private void setMorph(AbstractMorph morph)
    {
        this.state.morph = MorphUtils.copy(morph);
        this.morph.setMorph(morph);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.morph.setMorph(state.morph);
        this.drops.set(state.drops);
        this.xp.setValue(state.xp);
    }
}