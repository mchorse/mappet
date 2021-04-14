package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiNodeGraph;
import net.minecraft.client.Minecraft;

public class GuiEventPanel extends GuiMappetDashboardPanel<NodeSystem<EventNode>>
{
    public GuiNodeGraph graph;

    public GuiEventPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiNodeGraph(mc);
        this.graph.flex().relative(this.editor).wh(1F, 1F);

        this.add(this.graph);

        this.fill("", null);
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

        if (data != null)
        {
            this.graph.set(data);
        }
    }
}