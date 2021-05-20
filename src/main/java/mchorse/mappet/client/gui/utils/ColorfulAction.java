package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

public class ColorfulAction extends GuiSimpleContextMenu.Action
{
    public int color;

    public ColorfulAction(Icon icon, IKey label, Runnable runnable, int color)
    {
        super(icon, label, runnable);

        this.color = color;
    }

    @Override
    protected void drawBackground(FontRenderer font, int x, int y, int w, int h, boolean hover, boolean selected)
    {
        super.drawBackground(font, x, y, w, h, hover, selected);

        Gui.drawRect(x, y, x + 2, y + h, 0xff000000 + this.color);
        GuiDraw.drawHorizontalGradientRect(x + 2, y, x + 24, y + h, 0x44000000 + this.color, this.color);
    }
}