package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiEventNodePanel <T extends EventNode> extends GuiNodePanel<T>
{
    public GuiToggleElement binary;

    public GuiEventNodePanel(Minecraft mc)
    {
        super(mc);

        this.binary = new GuiToggleElement(mc, IKey.str("Binary"), (b) -> this.node.binary = b.isToggled());
    }

    public void set(T node)
    {
        super.set(node);

        this.binary.toggled(node.binary);
    }
}