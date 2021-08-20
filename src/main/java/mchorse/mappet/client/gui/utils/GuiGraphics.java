package mchorse.mappet.client.gui.utils;

import mchorse.mappet.client.gui.utils.graphics.Graphic;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class GuiGraphics extends GuiElement
{
    public List<Graphic> graphics = new ArrayList<Graphic>();

    public GuiGraphics(Minecraft mc)
    {
        super(mc);
    }

    @Override
    public void draw(GuiContext context)
    {
        for (Graphic graphic : this.graphics)
        {
            graphic.draw(context, this.area);
        }

        super.draw(context);
    }
}