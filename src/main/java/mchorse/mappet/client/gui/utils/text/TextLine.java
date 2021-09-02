package mchorse.mappet.client.gui.utils.text;

import net.minecraft.client.gui.FontRenderer;

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

        StringBuilder builder = new StringBuilder();

        for (int i = 0, c = this.text.length(); i < c; i++)
        {
            char character = this.text.charAt(i);
            String string = builder.toString();
            int sw = font.getStringWidth(string + character);

            if (sw > w)
            {
                lines.add(builder.toString());
                builder = new StringBuilder();
            }

            builder.append(character);
        }

        String string = builder.toString();

        if (!string.isEmpty())
        {
            lines.add(string);
        }

        return lines;
    }
}