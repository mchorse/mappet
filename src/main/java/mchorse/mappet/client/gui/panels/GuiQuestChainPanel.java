package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.quests.chains.QuestChain;
import mchorse.mappet.api.quests.chains.QuestChainManager;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiNodeGraph;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import mchorse.mappet.client.gui.nodes.quests.GuiQuestNodePanel;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;

public class GuiQuestChainPanel extends GuiMappetDashboardPanel<QuestChain>
{
    public GuiNodeGraph<QuestNode> graph;
    public GuiNodePanel panel;

    public GuiQuestChainPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiNodeGraph<QuestNode>(mc, QuestChainManager.FACTORY, this::pickNode);
        this.graph.flex().relative(this.editor).wh(1F, 1F);

        this.add(this.graph, this.panel);

        this.fill("", null);
    }

    private void pickNode(QuestNode node)
    {
        if (this.panel != null)
        {
            this.panel.removeFromParent();
            this.panel = null;
        }

        if (node != null)
        {
            GuiNodePanel panel = null;

            if (node instanceof QuestNode)
            {
                panel = new GuiQuestNodePanel(this.mc);
                panel.set(node);
            }

            if (panel != null)
            {
                panel.flex().relative(this).y(1F).w(220).anchorY(1F);

                this.panel = panel;
                this.panel.resize();

                this.add(panel);
            }
        }
    }

    @Override
    public ContentType getType()
    {
        return ContentType.CHAINS;
    }

    @Override
    public String getTitle()
    {
        return "Quest chains";
    }

    @Override
    public void fill(String id, QuestChain data)
    {
        super.fill(id, data);

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

            GuiDraw.drawMultiText(this.font, "Select or create a quest chain in the list on the right, to start editing...", x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}