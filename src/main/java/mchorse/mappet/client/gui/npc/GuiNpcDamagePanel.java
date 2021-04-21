package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcDamagePanel extends GuiNpcPanel
{
    public GuiTrackpadElement damage;
    public GuiToggleElement canFallDamage;
    public GuiToggleElement canGetBurned;
    public GuiToggleElement invincible;
    public GuiToggleElement killable;

    public GuiNpcDamagePanel(Minecraft mc)
    {
        super(mc);

        this.damage = new GuiTrackpadElement(mc, (v) -> this.state.damage = v.floatValue());
        this.damage.limit(0);
        this.canFallDamage = new GuiToggleElement(mc, IKey.str("Fall damage"), (b) -> this.state.canFallDamage = b.isToggled());
        this.canGetBurned = new GuiToggleElement(mc, IKey.str("Fire damage"), (b) -> this.state.canGetBurned = b.isToggled());
        this.invincible = new GuiToggleElement(mc, IKey.str("Invincible"), (b) -> this.state.invincible = b.isToggled());
        this.killable = new GuiToggleElement(mc, IKey.str("Killable"), (b) -> this.state.killable = b.isToggled());

        this.scroll.add(Elements.label(IKey.str("Damage")).background(), this.damage);
        this.scroll.add(this.canFallDamage, this.canGetBurned, this.invincible, this.killable);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.damage.setValue(state.damage);
        this.canFallDamage.toggled(state.canFallDamage);
        this.canGetBurned.toggled(state.canGetBurned);
        this.invincible.toggled(state.invincible);
        this.killable.toggled(state.killable);
    }
}