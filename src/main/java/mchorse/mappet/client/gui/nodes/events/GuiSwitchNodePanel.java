package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiSwitchNodePanel extends GuiEventBaseNodePanel<SwitchNode>
{
    public GuiTextElement expression;

    public GuiSwitchNodePanel(Minecraft mc)
    {
        super(mc);

        this.expression = new GuiTextElement(mc, 10000, (t) -> this.node.expression = t);

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.expression")).marginTop(12), this.expression);
    }

    @Override
    public void set(SwitchNode node)
    {
        super.set(node);

        this.expression.setText(node.expression);
    }
}