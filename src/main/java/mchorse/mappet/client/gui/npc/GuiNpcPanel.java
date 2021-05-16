package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import net.minecraft.client.Minecraft;

public abstract class GuiNpcPanel extends GuiElement
{
    protected NpcState state;

    public GuiNpcPanel(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).vertical().stretch();
    }

    public void set(NpcState state)
    {
        this.state = state;
    }
}