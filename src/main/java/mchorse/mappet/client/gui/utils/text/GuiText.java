package mchorse.mappet.client.gui.utils.text;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.ITextColoring;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiText extends GuiElement implements ITextColoring
{
    private IKey temp = IKey.EMPTY;
    private List<String> text;
    private int lineHeight = 12;
    private int color = 0xffffff;
    private int hoverColor = 0xffffff;
    private boolean shadow = true;
    private int paddingH;
    private int paddingV;
    private float anchorX;

    private int lines;

    public GuiText(Minecraft mc)
    {
        super(mc);

        this.flex().h(() -> (float) this.height());
    }

    private int height()
    {
        int height = Math.max(this.lines, 1) * this.lineHeight - (this.lineHeight - this.font.FONT_HEIGHT);

        return height + this.paddingV * 2;
    }

    public IKey getText()
    {
        return this.temp;
    }

    public GuiText text(String text)
    {
        return this.text(IKey.str(text));
    }

    public GuiText text(IKey text)
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

    public GuiText padding(int padding)
    {
        return this.padding(padding, padding);
    }

    public GuiText padding(int horizontal, int vertical)
    {
        this.paddingH = horizontal;
        this.paddingV = vertical;

        return this;
    }

    public GuiText anchorX(float anchor)
    {
        this.anchorX = anchor;

        return this;
    }

    @Override
    public void setColor(int color, boolean shadow)
    {
        this.color(color, shadow);
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
        if (this.area.w > 0)
        {
            if (this.text == null)
            {
                List<String> text = this.font.listFormattedStringToWidth(this.temp.get(), this.area.w - this.paddingH * 2);

                this.lines = text.size();
                this.getParentContainer().resize();

                this.text = text;
                this.lines = text.size();
            }

            int y = this.paddingV;
            int color = this.area.isInside(context) ? this.hoverColor : this.color;

            for (String line : this.text)
            {
                int x = this.area.x + this.paddingH;

                if (this.anchorX != 0)
                {
                    x = x + (int) (((this.area.w - this.paddingH * 2) - (this.font.getStringWidth(line))) * this.anchorX);
                }

                if (this.shadow)
                {
                    this.font.drawStringWithShadow(line, x, this.area.y + y, color);
                }
                else
                {
                    this.font.drawString(line, x, this.area.y + y, color);
                }

                y += this.lineHeight;
            }
        }

        super.draw(context);
    }
}