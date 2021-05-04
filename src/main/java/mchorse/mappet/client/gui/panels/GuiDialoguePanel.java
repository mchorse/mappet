package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.dialogues.DialogueManager;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.nodes.GuiCommandNodePanel;
import mchorse.mappet.client.gui.nodes.GuiConditionNodePanel;
import mchorse.mappet.client.gui.nodes.GuiCraftingNodePanel;
import mchorse.mappet.client.gui.nodes.GuiDialogueNodePanel;
import mchorse.mappet.client.gui.nodes.GuiNodeGraph;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;

public class GuiDialoguePanel extends GuiMappetDashboardPanel<DialogueNodeSystem>
{
    public GuiNodeGraph graph;
    public GuiNodePanel panel;

    public GuiDialoguePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.graph = new GuiNodeGraph(mc, DialogueManager.FACTORY, this::pickNode);
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
                panel = new GuiCommandNodePanel(this.mc);
                panel.set(node);
            }
            else if (node instanceof ConditionNode)
            {
                panel = new GuiConditionNodePanel(this.mc);
                panel.set(node);
            }
            else if (node instanceof DialogueNode)
            {
                panel = new GuiDialogueNodePanel(this.mc);
                panel.set(node);
            }
            else if (node instanceof CraftingNode)
            {
                panel = new GuiCraftingNodePanel(this.mc);
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
        return ContentType.DIALOGUE;
    }

    @Override
    public String getTitle()
    {
        return "Dialogues";
    }

    @Override
    public void fill(String id, DialogueNodeSystem data)
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

            GuiDraw.drawMultiText(this.font, "Select or create a dialogue in the list on the right, to start editing...", x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}