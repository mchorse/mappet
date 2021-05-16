package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.npc.GuiNpcEditor;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiNpcPanel extends GuiMappetDashboardPanel<Npc>
{
    public GuiToggleElement unique;
    public GuiTrackpadElement pathDistance;
    public GuiStringListElement states;
    public GuiNpcEditor npcEditor;

    public GuiNpcPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().h(20);

        this.pathDistance = new GuiTrackpadElement(mc, (v) -> this.data.pathDistance = v);

        this.states = new GuiStringListElement(mc, (list) -> this.pickState(list.get(0), false));
        this.states.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.npcs.context.add"), this::addState);

            if (!this.states.isDeselected())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.npcs.context.remove"), this::removeState);
            }

            return menu;
        });
        this.states.flex().relative(this).y(52).w(120).h(1F, -52);

        this.npcEditor = new GuiNpcEditor(mc, () -> this.inventory, this.dashboard::getMorphMenu);
        this.npcEditor.flex().relative(this).x(120).y(52).wTo(this.editor.area, 1F).h(1F, -52);
        this.npcEditor.setVisible(false);

        GuiElement bar = new GuiElement(mc);

        bar.flex().relative(this).xy(10, 22).wTo(this.editor.area, 1F, -10).h(20).row(10);
        bar.add(this.pathDistance, this.unique);

        this.toggleSidebar.removeFromParent();
        this.add(bar, this.states, this.npcEditor, this.toggleSidebar, this.inventory);

        this.fill("", null);
    }

    /* Context menu modals */

    private void addState()
    {
        GuiModal.addFullModal(this.states, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.npcs.modals.add"), this::addState));
    }

    private void addState(String name)
    {
        if (!this.data.states.containsKey(name))
        {
            NpcState state = new NpcState();

            this.data.states.put(name, state);
            this.states.add(name);
            this.states.sort();

            this.pickState(name, true);
        }
    }

    private void removeState()
    {
        GuiModal.addFullModal(this.states, () -> new GuiConfirmModal(this.mc, IKey.lang("mappet.gui.npcs.modals.remove"), this::removeState));
    }

    private void removeState(boolean confirm)
    {
        if (confirm)
        {
            int index = this.states.getIndex();
            String key = this.states.getCurrentFirst();

            this.states.remove(key);
            this.data.states.remove(key);
            this.states.setIndex(Math.max(index - 1, 0));

            String state = this.states.isDeselected() ? null : this.states.getList().get(this.states.getIndex());

            this.pickState(state, true);
        }
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

        this.resize();
    }

    @Override
    public void fill(String id, Npc data)
    {
        super.fill(id, data);

        this.unique.setVisible(data != null);
        this.pathDistance.setVisible(data != null);
        this.npcEditor.setVisible(data != null);
        this.states.setVisible(data != null);

        if (data != null)
        {
            this.unique.toggled(data.unique);
            this.pathDistance.setValue(data.pathDistance);

            this.states.clear();
            this.states.add(data.states.keySet());
            this.states.sort();

            this.pickState(data.states.isEmpty() ? null : this.states.getList().get(0), true);
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
            this.npcEditor.area.draw(0x66000000);
        }
        else
        {
            int w = (this.editor.area.ex() - this.area.x) / 2;
            int x = (this.area.x + this.editor.area.ex()) / 2 - w / 2;

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.npcs.info.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }

        if (this.pathDistance.isVisible())
        {
            this.font.drawStringWithShadow(I18n.format("mappet.gui.npcs.path_distance"), this.pathDistance.area.x, this.pathDistance.area.y - 12, 0xffffff);
        }

        super.draw(context);
    }
}