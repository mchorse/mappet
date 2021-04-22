package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.npc.GuiNpcEditor;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import net.minecraft.client.Minecraft;

public class GuiNpcPanel extends GuiMappetDashboardPanel<Npc>
{
    public GuiToggleElement unique;
    public GuiTrackpadElement pathDistance;
    public GuiStringListElement states;
    public GuiNpcEditor npcEditor;

    public GuiCreativeMorphsMenu morphs;

    public GuiNpcPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.unique = new GuiToggleElement(mc, IKey.str("Unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().h(20);

        this.pathDistance = new GuiTrackpadElement(mc, (v) -> this.data.pathDistance = v);

        this.states = new GuiStringListElement(mc, (list) -> this.pickState(list.get(0), false));
        this.states.flex().relative(this).y(52).w(120).h(1F, -52);

        this.npcEditor = new GuiNpcEditor(mc, () -> this.inventory, this::getMorphMenu);
        this.npcEditor.flex().relative(this).x(120).y(52).wTo(this.editor.area, 1F).h(1F, -52);
        this.npcEditor.setVisible(false);

        GuiElement bar = new GuiElement(mc);

        bar.flex().relative(this).xy(10, 22).wTo(this.editor.area, 1F, -10).h(20).row(10);
        bar.add(this.pathDistance, this.unique);

        this.toggleSidebar.removeFromParent();
        this.add(bar, this.states, this.npcEditor, this.toggleSidebar, this.inventory);

        this.fill("", null);
    }

    private GuiCreativeMorphsMenu getMorphMenu()
    {
        if (this.morphs == null)
        {
            this.morphs = new GuiCreativeMorphsMenu(this.mc, null);
        }

        return this.morphs;
    }

    private void pickState(String name, boolean select)
    {
        NpcState state = this.data.states.get(name);

        this.npcEditor.setVisible(state != null);

        if (select)
        {
            this.states.setCurrentScroll(name);
        }

        if (state != null)
        {
            this.npcEditor.set(state);
        }
    }

    @Override
    public void fill(String id, Npc data)
    {
        super.fill(id, data);

        this.unique.setVisible(data != null);
        this.pathDistance.setVisible(data != null);
        this.npcEditor.setVisible(data != null);

        if (data != null)
        {
            this.unique.toggled(data.unique);
            this.pathDistance.setValue(data.pathDistance);

            this.states.clear();
            this.states.add(data.states.keySet());
            this.states.sort();

            this.pickState(data.states.isEmpty() ? null : this.states.getList().get(0), true);
            this.npcEditor.setPanel(this.npcEditor.panels.get(0));
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
        return "NPCs";
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.npcEditor.isVisible())
        {
            this.npcEditor.area.draw(0x66000000);
        }

        super.draw(context);

        if (this.pathDistance.isVisible())
        {
            this.font.drawStringWithShadow("Path finding distance", this.pathDistance.area.x, this.pathDistance.area.y - 12, 0xffffff);
        }
    }
}