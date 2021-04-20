package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import net.minecraft.client.Minecraft;

public abstract class GuiNpcPanel extends GuiElement
{
    protected GuiScrollElement scroll;

    protected NpcState state;

    public GuiNpcPanel(Minecraft mc)
    {
        super(mc);

        this.scroll = new GuiScrollElement(mc);
        this.scroll.flex().relative(this).wh(1F, 1F).column(5).stretch().scroll().vertical().padding(10);

        this.add(this.scroll);
    }

    public void set(NpcState state)
    {
        this.state = state;
    }
}