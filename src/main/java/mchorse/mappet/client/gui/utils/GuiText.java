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
        this.color = color;
        this.shadow = shadow;

        return this;
    }

    @Override
    public void resize()
    {
        super.resize();

        this.text = null;
        this.lines = 0;
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

        for (String line : this.text)
        {
            if (this.shadow)
            {
                this.font.drawStringWithShadow(line, this.area.x, this.area.y + y, this.color);
            }
            else
            {
                this.font.drawString(line, this.area.x, this.area.y + y, this.color);
            }

            y += this.lineHeight;
        }

        super.draw(context);
    }
}