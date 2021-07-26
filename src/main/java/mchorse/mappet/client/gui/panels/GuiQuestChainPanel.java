package mchorse.mappet.client.gui.panels;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.quests.chains.QuestChain;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiNodeGraph;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import mchorse.mappet.client.gui.nodes.quests.GuiQuestNodePanel;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestChainPanel extends GuiMappetDashboardPanel<QuestChain>
{
    public static final IKey EMPTY = IKey.lang("mappet.gui.nodes.info.empty_chain");

    public GuiNodeGraph<QuestNode> graph;
    public GuiNodePanel panel;

    public GuiQuestChainPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiNodeGraph<QuestNode>(mc, CommonProxy.getChains(), this::pickNode);
        this.graph.flex().relative(this.editor).wh(1F, 1F);

        this.editor.add(this.graph);

        this.fill(null);
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

                this.editor.add(panel);
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
        return "mappet.gui.panels.chains";
    }

    @Override
    public void fill(QuestChain data, boolean allowed)
    {
        super.fill(data, allowed);

        this.graph.setVisible(data != null);

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

            GuiDraw.drawMultiText(this.font, EMPTY.get(), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}