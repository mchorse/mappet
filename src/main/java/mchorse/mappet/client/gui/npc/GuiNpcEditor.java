package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.GuiPanelBase;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.function.Supplier;

public class GuiNpcEditor extends GuiPanelBase<GuiNpcPanel>
{
    private NpcState state;

    public GuiNpcEditor(Minecraft mc, Supplier<GuiInventoryElement> inventory, Supplier<GuiCreativeMorphsMenu> morphs)
    {
        super(mc, Direction.BOTTOM);

        this.registerPanel(new GuiNpcGeneralPanel(mc, inventory, morphs), IKey.lang("mappet.gui.npcs.general.title"), Icons.GEAR);
        this.registerPanel(new GuiNpcHealthPanel(mc), IKey.lang("mappet.gui.npcs.health.title"), Icons.ADD);
        this.registerPanel(new GuiNpcDamagePanel(mc), IKey.lang("mappet.gui.npcs.damage.title"), Icons.CUT);
        this.registerPanel(new GuiNpcMovementPanel(mc), IKey.lang("mappet.gui.npcs.movement.title"), Icons.REVERSE);
        this.registerPanel(new GuiNpcBehaviorPanel(mc), IKey.lang("mappet.gui.npcs.behavior.title"), Icons.PROCESSOR);
    }

    public void set(NpcState state)
    {
        this.state = state;

        for (GuiNpcPanel panel : this.panels)
        {
            panel.set(state);
        }
    }

    public NpcState get()
    {
        return this.state;
    }

    @Override
    protected void drawBackground(GuiContext context, int x, int y, int w, int h)
    {
        Gui.drawRect(x, y, x + w, y + h, 0xff000000);
    }
}