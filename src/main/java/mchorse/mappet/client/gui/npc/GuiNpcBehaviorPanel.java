package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcBehaviorPanel extends GuiNpcPanel
{
    public GuiToggleElement lookAtPlayer;
    public GuiToggleElement lookAround;
    public GuiToggleElement wander;

    public GuiNpcBehaviorPanel(Minecraft mc)
    {
        super(mc);

        this.lookAtPlayer = new GuiToggleElement(mc, IKey.str("Look at player"), (b) -> this.state.lookAtPlayer = b.isToggled());
        this.lookAround = new GuiToggleElement(mc, IKey.str("Look around"), (b) -> this.state.lookAround = b.isToggled());
        this.wander = new GuiToggleElement(mc, IKey.str("Wander"), (b) -> this.state.wander = b.isToggled());

        this.scroll.add(this.lookAtPlayer, this.lookAround, this.wander);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.lookAtPlayer.toggled(state.lookAtPlayer);
        this.lookAround.toggled(state.lookAround);
        this.wander.toggled(state.wander);
    }
}