package mchorse.mappet.client.gui.panels;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.nodes.CancelNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.DialogueNode;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.events.nodes.ScriptNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.nodes.GuiEventNodeGraph;
import mchorse.mappet.client.gui.nodes.dialogues.GuiQuestChainNodePanel;
import mchorse.mappet.client.gui.nodes.dialogues.GuiReactionNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiCancelNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiCommandNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiConditionNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiDialogueNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiEventNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiScriptNodePanel;
import mchorse.mappet.client.gui.nodes.events.GuiTimerNodePanel;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.HashMap;
import java.util.Map;

public class GuiEventPanel extends GuiMappetDashboardPanel<NodeSystem<EventBaseNode>>
{
    public static final Map<
            Class<? extends EventBaseNode>,
            Class<? extends GuiEventBaseNodePanel<? extends EventBaseNode>>>
        PANELS = new HashMap<
            Class<? extends EventBaseNode>,
            Class<? extends GuiEventBaseNodePanel<? extends EventBaseNode>>>();

    static
    {
        PANELS.put(CancelNode.class, GuiCancelNodePanel.class);
        PANELS.put(CommandNode.class, GuiCommandNodePanel.class);
        PANELS.put(ConditionNode.class, GuiConditionNodePanel.class);
        PANELS.put(SwitchNode.class, GuiConditionNodePanel.class);
        PANELS.put(TimerNode.class, GuiTimerNodePanel.class);
        PANELS.put(EventNode.class, GuiEventNodePanel.class);
        PANELS.put(DialogueNode.class, GuiDialogueNodePanel.class);
        PANELS.put(ScriptNode.class, GuiScriptNodePanel.class);

        PANELS.put(ReactionNode.class, GuiReactionNodePanel.class);
        PANELS.put(ReplyNode.class, mchorse.mappet.client.gui.nodes.dialogues.GuiDialogueNodePanel.class);
        PANELS.put(QuestChainNode.class, GuiQuestChainNodePanel.class);
    }

    public GuiEventNodeGraph graph;
    public GuiEventBaseNodePanel panel;

    public GuiEventPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiEventNodeGraph(mc, CommonProxy.getEvents(), this::pickNode);
        this.graph.notifyAboutMain().flex().relative(this.editor).wh(1F, 1F);

        this.editor.add(this.graph);

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
                    .getConstructor(Minecraft.class, GuiMappetDashboardPanel.class)
                    .newInstance(this.mc, this);

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
        return ContentType.EVENT;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.events";
    }

    @Override
    public void fill(NodeSystem<EventBaseNode> data, boolean allowed)
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

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.nodes.info.empty_event"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}