package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.GuiPanelBase;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiNpcEditor extends GuiPanelBase<GuiNpcPanel>
{
    public GuiNpcEditor(Minecraft mc)
    {
        super(mc, Direction.BOTTOM);

        this.registerPanel(new GuiNpcGeneralPanel(mc), IKey.str("General"), Icons.GEAR);
        this.registerPanel(new GuiNpcHealthPanel(mc), IKey.str("Health"), Icons.ADD);
        this.registerPanel(new GuiNpcDamagePanel(mc), IKey.str("Damage"), Icons.CUT);
        this.registerPanel(new GuiNpcMovementPanel(mc), IKey.str("Movement"), Icons.REVERSE);
        this.registerPanel(new GuiNpcBehaviorPanel(mc), IKey.str("Behavior"), Icons.PROCESSOR);
    }

    public void set(NpcState state)
    {
        for (GuiNpcPanel panel : this.panels)
        {
            panel.set(state);
        }
    }

    @Override
    protected void drawBackground(GuiContext context, int x, int y, int w, int h)
    {
        Gui.drawRect(x, y, x + w, y + h, 0xff000000);
    }
}