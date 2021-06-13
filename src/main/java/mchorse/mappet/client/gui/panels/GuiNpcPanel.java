package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.npc.GuiNpcEditor;
import mchorse.mappet.client.gui.npc.utils.GuiNpcStatesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiNpcPanel extends GuiMappetDashboardPanel<Npc>
{
    public GuiIconElement states;
    public GuiNpcEditor npcEditor;

    private String state = "";

    public GuiNpcPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.states = new GuiIconElement(mc, Icons.MORE, (b) -> this.openNpcStates());
        this.states.flex().relative(this);

        this.npcEditor = new GuiNpcEditor(mc, false, () -> this.inventory);
        this.npcEditor.flex().relative(this).y(10).wTo(this.editor.area, 1F).h(1F, -10);
        this.npcEditor.setVisible(false);

        this.editor.add(this.npcEditor, this.states, this.inventory);

        this.fill(null);
    }

    private void openNpcStates()
    {
        GuiNpcStatesOverlayPanel overlay = new GuiNpcStatesOverlayPanel(this.mc, this.data, this::pickState);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.state), 0.4F, 0.6F);
    }

    private void pickState(String name)
    {
        this.state = name;

        NpcState state = this.data.states.get(name);

        this.npcEditor.setVisible(state != null);

        if (state != null)
        {
            this.npcEditor.set(state);
        }

        this.resize();
    }

    @Override
    public void fill(Npc data, boolean allowed)
    {
        super.fill(data, allowed);

        this.npcEditor.setVisible(data != null);
        this.states.setVisible(data != null);

        if (data != null)
        {
            String key = "default";

            if (!data.states.containsKey(key) && !data.states.isEmpty())
            {
                key = data.states.keySet().iterator().next();
            }

            this.pickState(key);
        }
    }

    @Override
    public ContentType getType()
    {
        return ContentType.NPC;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.npcs";
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.npcEditor.isVisible())
        {
            GuiDraw.drawTextBackground(this.font, this.state, this.states.area.ex() + 3, this.states.area.my() - 4, 0xffffff, ColorUtils.HALF_BLACK, 2);
        }
        else
        {
            int w = (this.editor.area.ex() - this.area.x) / 2;
            int x = (this.area.x + this.editor.area.ex()) / 2 - w / 2;

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.npcs.info.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        super.draw(context);
    }
}