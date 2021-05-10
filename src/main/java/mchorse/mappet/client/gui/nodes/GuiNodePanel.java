package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.utils.nodes.Node;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiNodePanel <T extends Node> extends GuiElement
{
    public GuiTextElement title;

    public T node;

    public GuiNodePanel(Minecraft mc)
    {
        super(mc);

        this.title = new GuiTextElement(mc, 1000, (t) -> this.node.title = t);

        this.flex().column(5).vertical().stretch().padding(10);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.node.title")), this.title);
    }

    public void set(T node)
    {
        this.node = node;

        this.title.setText(node.title);
    }
}