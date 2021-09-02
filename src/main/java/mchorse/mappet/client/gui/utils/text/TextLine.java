package mchorse.mappet.client.gui.utils.text;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class TextLine
{
    public String text;
    public List<String> wrappedLines;

    public TextLine(String text)
    {
        this.text = text;
    }

    public void set(String text)
    {
        this.text = text;
    }

    public int getLines()
    {
        return this.wrappedLines == null ? 1 : this.wrappedLines.size();
    }

    public void calculateWrappedLines(FontRenderer font, int w)
    {
        List<String> wrappedLines = splitIntoLines(font, w);

        if (wrappedLines.size() < 2)
        {
            this.wrappedLines = null;
        }
        else
        {
            this.wrappedLines = wrappedLines;
        }
    }

    /**
     * Shitty and inefficient algorithm to break lines which preserves
     * spaces and any other characters.
     */
    private List<String> splitIntoLines(FontRenderer font, int w)
    {
        List<String> lines = new ArrayList<String>();

        if (font.getStringWidth(this.text) < w)
        {
            lines.add(this.text);

            return lines;
        }

        int left = 0;
        int right = 0;
        int c = this.text.length();
        int increment = c > 5 ? 5 : 1;

        for (; right < c; right += increment)
        {
            String string = this.text.substring(left, right);
            int sw = font.getStringWidth(string);

            if (sw > w)
            {
                int space = string.lastIndexOf(' ', right);
                int diff = (right - left) - space;

                if (space != -1 && diff < 12)
                {
                    right -= diff - 1;
                    string = this.text.substring(left, right);
                }

                lines.add(string);
                left = right;
            }
        }

        if (left != right)
        {
            lines.add(this.text.substring(left, MathHelper.clamp(right, 0, c)));
        }

        return lines;
    }
}