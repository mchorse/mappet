package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.TriggerNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;

public class GuiTriggerNodePanel extends GuiEventBaseNodePanel<TriggerNode>
{
    public GuiTriggerElement trigger;
    public GuiTextElement customData;
    public GuiToggleElement cancel;

    public GuiTriggerNodePanel(Minecraft mc)
    {
        super(mc);

        this.trigger = new GuiTriggerElement(mc);
        this.customData = GuiMappetUtils.fullWindowContext(
                new GuiTextElement(mc, 100000, (text) -> this.node.customData = text),
                IKey.lang("mappet.gui.nodes.event.data")
        );
        this.customData.tooltip(IKey.lang("mappet.gui.nodes.event.data_tooltip"));
        this.cancel = new GuiToggleElement(mc, IKey.lang("mappet.gui.nodes.event.cancel"), (b) -> this.node.cancel = b.isToggled());
        this.cancel.tooltip(IKey.lang("mappet.gui.nodes.event.cancel_tooltip"), Direction.TOP);

        this.add(this.trigger);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.customData, this.cancel, this.binary);
    }

    @Override
    public void set(TriggerNode node)
    {
        super.set(node);

        this.trigger.set(node.trigger);
        this.customData.setText(node.customData);
        this.cancel.toggled(node.cancel);
    }
}