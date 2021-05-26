package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.events.hotkeys.EventHotkeys;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.events.GuiEventHotkeysOverlayPanel;
import mchorse.mappet.client.gui.nodes.GuiEventNodeGraph;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiCommandNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiConditionNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiTimerNodePanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventRequestHotkeys;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiEventPanel extends GuiMappetDashboardPanel<NodeSystem<EventNode>>
{
    public GuiEventNodeGraph graph;
    public GuiEventNodePanel panel;
    public GuiIconElement hotkeys;

    public GuiEventPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiEventNodeGraph(mc, EventManager.FACTORY, this::pickNode);
        this.graph.notifyAboutMain().flex().relative(this.editor).wh(1F, 1F);

        this.hotkeys = new GuiIconElement(mc, Icons.DOWNLOAD, (b) -> Dispatcher.sendToServer(new PacketEventRequestHotkeys()));
        this.buttons.prepend(this.hotkeys);

        this.editor.add(this.graph);

        this.fill(null);
    }

    private void pickNode(EventNode node)
    {
        if (this.panel != null)
        {
            this.panel.removeFromParent();
            this.panel = null;
        }

        if (node != null)
        {
            GuiEventNodePanel panel = null;

            if (node instanceof CommandNode)
            {
                panel = new GuiCommandNodePanel(this.mc);
                panel.set(node);
            }
            else if (node instanceof ConditionNode)
            {
                panel = new GuiConditionNodePanel(this.mc);
                panel.set(node);

                if (node instanceof SwitchNode)
                {
                    panel.binary.removeFromParent();
                }
            }
            else if (node instanceof TimerNode)
            {
                panel = new GuiTimerNodePanel(this.mc);
                panel.set(node);
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
        return ContentType.EVENT;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.events";
    }

    @Override
    public void fill(NodeSystem<EventNode> data, boolean allowed)
    {
        super.fill(data, allowed);

        this.graph.setVisible(data != null);
        this.pickNode(null);

        if (data != null)
        {
            this.graph.set(data);
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

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.nodes.info.empty_event"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }

    public void openHotkeysEditor(EventHotkeys hotkeys)
    {
        GuiEventHotkeysOverlayPanel overlay = new GuiEventHotkeysOverlayPanel(this.mc, hotkeys);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
    }
}