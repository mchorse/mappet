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
        this.maxHealth.limit(0);
        this.health = new GuiTrackpadElement(mc, (v) -> this.state.health = v.floatValue());
        this.health.limit(0);
        this.regenDelay = new GuiTrackpadElement(mc, (v) -> this.state.regenDelay = v.intValue());
        this.regenDelay.limit(0).integer();
        this.regenFrequency = new GuiTrackpadElement(mc, (v) -> this.state.regenFrequency = v.intValue());
        this.regenFrequency.limit(1).integer();

        this.add(Elements.label(IKey.lang("mappet.gui.npcs.health.max_hp")), this.maxHealth);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.health.hp")), this.health);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.health.regen_delay")), this.regenDelay);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.health.regen_frequency")), this.regenFrequency);
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