package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiConditionNodePanel extends GuiEventNodePanel<ConditionNode>
{
    public GuiElement row;
    public GuiTextElement expression;
    public GuiIconElement toggle;
    public GuiButtonElement condition;

    public GuiConditionNodePanel(Minecraft mc)
    {
        super(mc);

        this.row = new GuiElement(mc);
        this.expression = GuiMappetUtils.fullWindowContext(
            new GuiTextElement(mc, 10000, (text) -> this.node.expression = text),
            IKey.lang("mappet.gui.nodes.event.condition")
        );
        this.toggle = new GuiIconElement(mc, Icons.REVERSE, this::toggleMode);
        this.condition = new GuiButtonElement(mc, IKey.lang("mappet.gui.nodes.event.edit_condition"), this::openConditionEditor);

        this.row.flex().h(20).row(0);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.condition")).marginTop(12), this.row, this.binary);
    }

    private void toggleMode(GuiIconElement b)
    {
        this.node.mode = this.node.mode == ConditionNode.ConditionMode.CONDITION ? ConditionNode.ConditionMode.EXPRESSION : ConditionNode.ConditionMode.CONDITION;

        this.updateFields();
    }

    private void updateFields()
    {
        this.row.removeAll();
        this.row.add(this.node.mode == ConditionNode.ConditionMode.CONDITION ? this.condition : this.expression, this.toggle);
        this.resize();
    }

    private void openConditionEditor(GuiButtonElement b)
    {
        GuiConditionOverlayPanel panel = new GuiConditionOverlayPanel(this.mc, this.node.condition);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.6F, 0.6F);
    }

    @Override
    public void set(ConditionNode node)
    {
        super.set(node);

        this.expression.setText(node.expression);
        this.updateFields();
    }
}