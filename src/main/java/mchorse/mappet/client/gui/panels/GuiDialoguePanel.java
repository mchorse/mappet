package mchorse.mappet.client.gui.panels;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.nodes.GuiEventNodeGraph;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialoguePanel extends GuiMappetDashboardPanel<Dialogue>
{
    public static final IKey EMPTY = IKey.lang("mappet.gui.nodes.info.empty_dialogue");

    public GuiEventNodeGraph graph;
    public GuiEventBaseNodePanel panel;
    public GuiToggleElement closable;

    public GuiDialoguePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiEventNodeGraph(mc, CommonProxy.getDialogues(), this::pickNode);
        this.graph.notifyAboutMain().flex().relative(this.editor).wh(1F, 1F);

        this.closable = new GuiToggleElement(mc, IKey.lang("mappet.gui.nodes.dialogue.closable"), (b) -> this.data.closable = b.isToggled());
        this.closable.flex().relative(this.sidebar).x(10).y(1F, -10).w(1F, -20).anchorY(1F);

        this.names.flex().hTo(this.closable.area, -5);

        this.editor.add(this.graph);
        this.sidebar.prepend(this.closable);

        this.fill(null);
    }

    private void pickNode(EventBaseNode node)
    {
        if (this.panel != null)
        {
            this.panel.removeFromParent();
            this.panel = null;
        }

        if (node != null)
        {
            GuiEventBaseNodePanel panel = null;

            try
            {
                panel = GuiEventPanel.PANELS.get(node.getClass())
                    .getConstructor(Minecraft.class)
                    .newInstance(this.mc);

                panel.set(node);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            if (panel != null)
            {
                panel.flex().relative(this).y(1F).w(220).anchorY(1F);

                this.panel = panel;
                this.panel.resize();

                this.editor.add(panel);
            }
        }
    }

    @Override
    public ContentType getType()
    {
        return ContentType.DIALOGUE;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.dialogues";
    }

    @Override
    public void fill(Dialogue data, boolean allowed)
    {
        super.fill(data, allowed);

        this.graph.setVisible(data != null);
        this.closable.setVisible(data != null && allowed);

        if (data != null)
        {
            this.graph.set(data);
            this.closable.toggled(data.closable);
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (!this.graph.isVisible())
        {
            int w = this.editor.area.w / 2;
            int x = this.editor.area.mx() - w / 2;

            GuiDraw.drawMultiText(this.font, EMPTY.get(), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}