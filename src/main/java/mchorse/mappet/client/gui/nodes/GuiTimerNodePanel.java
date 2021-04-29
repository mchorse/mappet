package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTimerNodePanel extends GuiNodePanel<TimerNode>
{
    public GuiTrackpadElement timer;

    public GuiTimerNodePanel(Minecraft mc)
    {
        super(mc);

        this.timer = new GuiTrackpadElement(mc, (value) -> this.node.timer = value.intValue());
        this.timer.integer().limit(0);

        this.add(Elements.label(IKey.str("Timer")), this.timer);
    }

    @Override
    public void set(TimerNode node)
    {
        super.set(node);

        this.timer.setValue(node.timer);
    }
}