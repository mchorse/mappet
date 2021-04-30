package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcBehaviorPanel extends GuiNpcPanel
{
    public GuiToggleElement lookAtPlayer;
    public GuiToggleElement lookAround;
    public GuiToggleElement wander;

    public GuiTriggerElement triggerDied;
    public GuiTriggerElement triggerDamaged;
    public GuiTriggerElement triggerInteract;
    public GuiTriggerElement triggerTick;
    public GuiTriggerElement triggerTarget;
    public GuiTriggerElement triggerInitialize;

    public GuiNpcBehaviorPanel(Minecraft mc)
    {
        super(mc);

        this.lookAtPlayer = new GuiToggleElement(mc, IKey.str("Look at player"), (b) -> this.state.lookAtPlayer = b.isToggled());
        this.lookAround = new GuiToggleElement(mc, IKey.str("Look around"), (b) -> this.state.lookAround = b.isToggled());
        this.wander = new GuiToggleElement(mc, IKey.str("Wander"), (b) -> this.state.wander = b.isToggled());

        this.triggerDied = new GuiTriggerElement(mc);
        this.triggerDamaged = new GuiTriggerElement(mc);
        this.triggerInteract = new GuiTriggerElement(mc);
        this.triggerTick = new GuiTriggerElement(mc);
        this.triggerTarget = new GuiTriggerElement(mc);
        this.triggerInitialize = new GuiTriggerElement(mc);

        this.scroll.add(this.lookAtPlayer, this.lookAround, this.wander);
        this.scroll.add(Elements.label(IKey.str("On NPC initialization")).background().marginTop(12).marginBottom(5), this.triggerInitialize);
        this.scroll.add(Elements.label(IKey.str("On NPC interaction trigger")).background().marginTop(12).marginBottom(5), this.triggerInteract);
        this.scroll.add(Elements.label(IKey.str("On NPC getting damaged trigger")).background().marginTop(12).marginBottom(5), this.triggerDamaged);
        this.scroll.add(Elements.label(IKey.str("On NPC death trigger")).background().marginTop(12).marginBottom(5), this.triggerDied);
        this.scroll.add(Elements.label(IKey.str("On NPC tick trigger")).background().marginTop(12).marginBottom(5), this.triggerTick);
        this.scroll.add(Elements.label(IKey.str("On NPC target trigger")).background().marginTop(12).marginBottom(5), this.triggerTarget);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.lookAtPlayer.toggled(state.lookAtPlayer);
        this.lookAround.toggled(state.lookAround);
        this.wander.toggled(state.wander);

        this.triggerDied.set(state.triggerDied);
        this.triggerDamaged.set(state.triggerDamaged);
        this.triggerInteract.set(state.triggerInteract);
        this.triggerTick.set(state.triggerTick);
        this.triggerTarget.set(state.triggerTarget);
        this.triggerInitialize.set(state.triggerInitialize);
    }
}