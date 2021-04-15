package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiNodePanel <T extends EventNode> extends GuiElement
{
    public GuiToggleElement binary;

    public T node;

    public GuiNodePanel(Minecraft mc)
    {
        super(mc);

        this.binary = new GuiToggleElement(mc, IKey.str("Binary"), (b) -> this.node.binary = b.isToggled());

        this.flex().column(5).vertical().stretch().padding(10);
    }

    public void set(T node)
    {
        this.node = node;
    }
}