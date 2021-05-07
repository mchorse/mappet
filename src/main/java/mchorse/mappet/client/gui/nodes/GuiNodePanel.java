package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.utils.nodes.Node;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;

public abstract class GuiNodePanel <T extends Node> extends GuiElement
{
    public T node;

    public GuiNodePanel(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).vertical().stretch().padding(10);
    }

    public void set(T node)
    {
        this.node = node;
    }
}