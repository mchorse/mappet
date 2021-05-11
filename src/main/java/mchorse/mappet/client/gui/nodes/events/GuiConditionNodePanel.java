package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiConditionNodePanel extends GuiEventNodePanel<ConditionNode>
{
    public GuiTextElement condition;

    public GuiConditionNodePanel(Minecraft mc)
    {
        super(mc);

        this.condition = new GuiTextElement(mc, 10000, (text) -> this.node.expression = text);

        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.condition")).marginTop(12), this.condition, this.binary);
    }

    @Override
    public void set(ConditionNode node)
    {
        super.set(node);

        this.condition.setText(node.expression);
    }
}