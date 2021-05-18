package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.utils.nodes.Node;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

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

    @Override
    public void draw(GuiContext context)
    {
        Gui.drawRect(this.area.x, this.area.ey() - 40, this.area.ex(), this.area.ey(), ColorUtils.HALF_BLACK);
        GuiDraw.drawVerticalGradientRect(this.area.x, this.area.y, this.area.ex(), this.area.ey() - 40, 0, ColorUtils.HALF_BLACK);

        super.draw(context);
    }
}