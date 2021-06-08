package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiConditionNodePanel extends GuiEventNodePanel<ConditionNode>
{
    public GuiCheckerElement checker;

    public GuiConditionNodePanel(Minecraft mc)
    {
        super(mc);

        this.checker = new GuiCheckerElement(mc);

        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.condition")).marginTop(12), this.checker, this.binary);
    }

    @Override
    public void set(ConditionNode node)
    {
        super.set(node);

        this.checker.set(node.condition);
    }
}