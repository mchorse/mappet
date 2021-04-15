package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiCommandNodePanel;
import mchorse.mappet.client.gui.nodes.GuiConditionNodePanel;
import mchorse.mappet.client.gui.nodes.GuiNodeGraph;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import net.minecraft.client.Minecraft;

public class GuiEventPanel extends GuiMappetDashboardPanel<NodeSystem<EventNode>>
{
    public GuiNodeGraph graph;
    public GuiNodePanel panel;

    public GuiEventPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiNodeGraph(mc, this::pickNode);
        this.graph.flex().relative(this.editor).wh(1F, 1F);

        this.add(this.graph, this.panel);

        this.fill("", null);
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
            GuiNodePanel panel = null;

            if (node instanceof CommandNode)
            {
                panel = new GuiCommandNodePanel(mc);
                panel.set(node);
            }
            else if (node instanceof ConditionNode)
            {
                panel = new GuiConditionNodePanel(mc);
                panel.set(node);
            }

            if (panel != null)
            {
                panel.flex().relative(this).y(1F).w(180).anchorY(1F);

                this.panel = panel;
                this.panel.resize();

                this.add(panel);
            }
        }
    }

    @Override
    public ContentType getType()
    {
        return ContentType.EVENT;
    }

    @Override
    public void fill(String id, NodeSystem<EventNode> data)
    {
        super.fill(id, data);

        this.graph.setVisible(data != null);
        this.pickNode(null);

        if (data != null)
        {
            this.graph.set(data);
        }
    }
}