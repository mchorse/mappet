package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcBehaviorPanel extends GuiNpcPanel
{
    public GuiToggleElement lookAtPlayer;
    public GuiToggleElement lookAround;
    public GuiToggleElement wander;
    public GuiToggleElement alwaysWander;
    public GuiToggleElement canFly;
    public GuiToggleElement canPickUpLoot;
    public GuiToggleElement canBeSteered;

    public GuiTriggerElement triggerDied;
    public GuiTriggerElement triggerDamaged;
    public GuiTriggerElement triggerInteract;
    public GuiTriggerElement triggerTick;
    public GuiTriggerElement triggerTarget;
    public GuiTriggerElement triggerInitialize;
    public GuiTriggerElement triggerEntityCollision;

    public GuiNpcBehaviorPanel(Minecraft mc)
    {
        super(mc);

        this.lookAtPlayer = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.look_at_player"), (b) -> this.state.lookAtPlayer = b.isToggled());
        this.lookAround = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.look_around"), (b) -> this.state.lookAround = b.isToggled());
        this.wander = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.wander"), (b) -> this.state.wander = b.isToggled());
        this.alwaysWander = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.always_wander"), (b) -> this.state.alwaysWander = b.isToggled());
        this.canFly = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.can_fly"), (b) -> this.state.canFly = b.isToggled());
        this.canPickUpLoot = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.can_pick_up_loot"), (b) -> this.state.canPickUpLoot = b.isToggled());
        this.canBeSteered = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.behavior.can_be_steered"), (b) -> this.state.canBeSteered = b.isToggled());

        this.triggerDied = new GuiTriggerElement(mc);
        this.triggerDamaged = new GuiTriggerElement(mc);
        this.triggerInteract = new GuiTriggerElement(mc);
        this.triggerTick = new GuiTriggerElement(mc);
        this.triggerTarget = new GuiTriggerElement(mc);
        this.triggerEntityCollision = new GuiTriggerElement(mc);
        this.triggerInitialize = new GuiTriggerElement(mc);

        this.add(this.lookAtPlayer, this.lookAround, this.wander, this.alwaysWander, this.canFly, this.canPickUpLoot, this.canBeSteered);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.initialize")).background().marginTop(12).marginBottom(5), this.triggerInitialize);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.interact")).background().marginTop(12).marginBottom(5), this.triggerInteract);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.damaged")).background().marginTop(12).marginBottom(5), this.triggerDamaged);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.died")).background().marginTop(12).marginBottom(5), this.triggerDied);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.tick")).background().marginTop(12).marginBottom(5), this.triggerTick);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.target")).background().marginTop(12).marginBottom(5), this.triggerTarget);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.behavior.collision")).background().marginTop(12).marginBottom(5), this.triggerEntityCollision);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.lookAtPlayer.toggled(state.lookAtPlayer);
        this.lookAround.toggled(state.lookAround);
        this.wander.toggled(state.wander);
        this.alwaysWander.toggled(state.alwaysWander);
        this.canFly.toggled(state.canFly);
        this.canPickUpLoot.toggled(state.canPickUpLoot);
        this.canBeSteered.toggled(state.canBeSteered);

        this.triggerDied.set(state.triggerDied);
        this.triggerDamaged.set(state.triggerDamaged);
        this.triggerInteract.set(state.triggerInteract);
        this.triggerTick.set(state.triggerTick);
        this.triggerTarget.set(state.triggerTarget);
        this.triggerEntityCollision.set(state.triggerEntityCollision);
        this.triggerInitialize.set(state.triggerInitialize);
    }
}