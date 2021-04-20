package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcHealthPanel extends GuiNpcPanel
{
    public GuiTrackpadElement maxHealth;
    public GuiTrackpadElement health;
    public GuiTrackpadElement regenDelay;
    public GuiTrackpadElement regenFrequency;

    public GuiNpcHealthPanel(Minecraft mc)
    {
        super(mc);

        this.maxHealth = new GuiTrackpadElement(mc, (v) -> this.state.maxHealth = v.floatValue());
        this.health = new GuiTrackpadElement(mc, (v) -> this.state.health = v.floatValue());
        this.regenDelay = new GuiTrackpadElement(mc, (v) -> this.state.regenDelay = v.intValue());
        this.regenFrequency = new GuiTrackpadElement(mc, (v) -> this.state.regenFrequency = v.intValue());

        this.scroll.add(Elements.label(IKey.str("Max HP")).background(0x88000000), this.maxHealth);
        this.scroll.add(Elements.label(IKey.str("Initial HP"), 20).anchor(0, 1F).background(0x88000000), this.health);
        this.scroll.add(Elements.label(IKey.str("HP regeneration delay"), 20).anchor(0, 1F).background(0x88000000), this.regenDelay);
        this.scroll.add(Elements.label(IKey.str("HP regeneration frequency"), 20).anchor(0, 1F).background(0x88000000), this.regenFrequency);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.maxHealth.setValue(state.maxHealth);
        this.health.setValue(state.health);
        this.regenDelay.setValue(state.regenDelay);
        this.regenFrequency.setValue(state.regenFrequency);
    }
}