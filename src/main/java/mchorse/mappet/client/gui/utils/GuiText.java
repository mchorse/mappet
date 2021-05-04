package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiText extends GuiElement
{
    private String temp;
    private List<String> text;
    private int lineHeight = 12;
    private int color = 0xffffff;
    private int hoverColor = 0xffffff;
    private boolean shadow = true;

    private int lines;

    public GuiText(Minecraft mc)
    {
        super(mc);

        this.flex().h(() -> (float) this.height());
    }

    private int height()
    {
        int height = Math.max(this.lines, 1) * this.lineHeight - (this.lineHeight - this.font.FONT_HEIGHT);

        return height;
    }

    public GuiText text(String text)
    {
        this.temp = text;
        this.text = null;
        this.lines = 0;

        return this;
    }

    public GuiText lineHeight(int lineHeight)
    {
        this.lineHeight = lineHeight;

        return this;
    }

    public GuiText color(int color, boolean shadow)
    {
        this.color = this.hoverColor = color;
        this.shadow = shadow;

        return this;
    }

    public GuiText hoverColor(int color)
    {
        this.hoverColor = color;

        return this;
    }

    @Override
    public void resize()
    {
        super.resize();

        this.text = null;
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.text == null)
        {
            List<String> text = this.font.listFormattedStringToWidth(this.temp, this.area.w);

            this.lines = text.size();
            this.getParentContainer().resize();

            this.text = text;
            this.lines = text.size();
        }

        int y = 0;
        int color = this.area.isInside(context) ? this.hoverColor : this.color;

        for (String line : this.text)
        {
            if (this.shadow)
            {
                this.font.drawStringWithShadow(line, this.area.x, this.area.y + y, color);
            }
            else
            {
                this.font.drawString(line, this.area.x, this.area.y + y, color);
            }

            y += this.lineHeight;
        }

        super.draw(context);
    }
}