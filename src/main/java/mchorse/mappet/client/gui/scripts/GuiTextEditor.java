package mchorse.mappet.client.gui.scripts;

import mchorse.mappet.client.gui.scripts.utils.SyntaxHighlighter;
import mchorse.mappet.client.gui.scripts.utils.TextSegment;
import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mappet.client.gui.utils.text.undo.TextEditUndo;
import mchorse.mappet.client.gui.utils.text.utils.Cursor;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiTextEditor extends GuiMultiTextElement
{
    private SyntaxHighlighter highlighter;
    private List<List<TextSegment>> segments = new ArrayList<List<TextSegment>>();
    private int placements;

    public GuiTextEditor(Minecraft mc, Consumer<String> callback)
    {
        super(mc, callback);

        this.highlighter = new SyntaxHighlighter();
    }

    public SyntaxHighlighter getHighlighter()
    {
        return this.highlighter;
    }

    public void resetHighlight()
    {
        this.segments.clear();
        this.ensureSize();
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);

        /* It will be null before when it will get called from parent's constructor */
        if (this.segments != null)
        {
            this.resetHighlight();
        }
    }

    private void ensureSize()
    {
        while (this.segments.size() < this.text.size())
        {
            this.segments.add(null);
        }

        while (this.segments.size() > this.text.size())
        {
            this.segments.remove(this.segments.size() - 1);
        }

        double power = Math.ceil(Math.log10(this.text.size() + 1));

        this.placements = (int) power * 6;
    }

    @Override
    protected void changedLine(int i)
    {
        this.ensureSize();

        String line = this.text.get(i);

        if (line.contains("/*") || line.contains("*/"))
        {
            this.changedLineAfter(i);
        }
        else
        {
            this.segments.set(i, null);
        }
    }

    @Override
    protected void changedLineAfter(int i)
    {
        this.ensureSize();

        while (i < this.segments.size())
        {
            this.segments.set(i, null);

            i += 1;
        }
    }

    /* Change input behavior */

    @Override
    protected void keyNewLine(TextEditUndo undo)
    {
        String line = this.text.get(this.cursor.line);
        boolean unwrap = line.length() >= 2
            && this.cursor.offset > 0
            && this.cursor.offset < line.length()
            && line.charAt(this.cursor.offset) == '}'
            && line.charAt(this.cursor.offset - 1) == '{';

        int indent = this.getIndent(line) + (unwrap ? 4 : 0);

        super.keyNewLine(undo);

        String margin = this.createIndent(indent);

        this.writeString(margin);
        this.cursor.offset = indent;

        undo.postText += margin;

        if (unwrap)
        {
            super.keyNewLine(undo);

            margin = this.createIndent(indent - 4);

            this.writeString(margin);
            this.cursor.line -= 1;
            this.cursor.offset = indent;

            undo.postText += margin;
        }
    }

    @Override
    protected void keyBackspace(TextEditUndo undo)
    {
        String line = this.text.get(this.cursor.line);

        line = this.cursor.start(line);

        if (!line.isEmpty() && line.trim().isEmpty())
        {
            int offset = 4 - line.length() % 4;

            this.startSelecting();
            this.cursor.offset -= offset;

            String deleted = this.getSelectedText();

            this.deleteSelection();
            this.deselect();

            undo.text = deleted;
        }
        else
        {
            super.keyBackspace(undo);
        }
    }

    @Override
    protected void keyTab(TextEditUndo undo)
    {
        if (this.isSelected())
        {
            boolean shift = GuiScreen.isShiftKeyDown();
            Cursor min = this.getMin();

            if (shift)
            {
                min.offset = Math.max(min.offset - 4, 0);
            }

            Cursor temp = new Cursor();
            String[] splits = this.getSelectedText().split("\n");

            for (int i = 0; i < splits.length; i++)
            {
                if (shift)
                {
                    int indent = this.getIndent(splits[i]);

                    splits[i] = splits[i].substring(Math.min(indent, 4));
                }
                else
                {
                    splits[i] = "    " + splits[i];
                }
            }

            temp.copy(min);
            this.deleteSelection();
            this.writeString(String.join("\n", splits));
            this.getMin().set(min.line, splits[splits.length - 1].length());
            min.copy(temp);

            if (!shift)
            {
                min.offset += 4;
            }

            undo.postText = this.getSelectedText();
        }
        else
        {
            super.keyTab(undo);
        }
    }

    public int getIndent(int i)
    {
        if (this.hasLine(i))
        {
            return this.getIndent(this.text.get(i));
        }

        return 0;
    }

    public int getIndent(String line)
    {
        for (int j = 0; j < line.length(); j++)
        {
            char c = line.charAt(j);

            if (c != ' ')
            {
                return j;
            }
        }

        return line.length();
    }

    public String createIndent(int i)
    {
        StringBuilder builder = new StringBuilder();

        while (i > 0)
        {
            builder.append(' ');

            i -= 1;
        }

        return builder.toString();
    }

    /* Replacing rendering */

    @Override
    protected void drawTextLine(String line, int i, int nx, int ny, int sw)
    {
        if (this.segments.size() != this.text.size())
        {
            return;
        }

        List<TextSegment> segments = this.segments.get(i);

        if (segments == null)
        {
            segments = this.highlighter.parse(this.font, this.segments, line, i);
            this.segments.set(i, segments);
        }

        if (segments != null)
        {
            for (TextSegment segment : segments)
            {
                this.font.drawString(segment.text, nx, ny, segment.color, this.highlighter.getStyle().shadow);

                nx += segment.width;
            }
        }
    }

    @Override
    protected int getShiftX()
    {
        return 10 + this.placements;
    }

    @Override
    protected void drawBackground()
    {
        this.area.draw(0xff000000 + ColorUtils.multiplyColor(this.highlighter.getStyle().background, 0.8F));
    }

    @Override
    protected void drawForeground(GuiContext context)
    {
        /* Draw line numbers background */
        int x = this.area.x + 10 + this.placements;

        Gui.drawRect(this.area.x, this.area.y, x, this.area.ey(), 0xff000000 + this.highlighter.getStyle().background);

        /* Draw line numbers themselves */
        int start = Math.max(0, (this.vertical.scroll - 10) / this.lineHeight);
        int y = start * this.lineHeight;

        for (int i = start; i < this.text.size(); i++)
        {
            int ny = this.area.y + this.padding + y - this.vertical.scroll;

            if (ny > this.area.ey())
            {
                break;
            }

            String label = String.valueOf(i + 1);

            this.font.drawString(label, this.area.x + 5 + this.placements - this.font.getStringWidth(label), ny, this.highlighter.getStyle().lineNumbers);

            y += this.lineHeight;
        }

        /* Draw shadow to the right of line numbers when scrolling */
        int a = (int) (Math.min(this.horizontal.scroll / 10F, 1F) * 0x44);

        if (a > 0)
        {
            GuiDraw.drawHorizontalGradientRect(x, this.area.y, x + 10, this.area.ey(), a << 24, 0);
        }
    }

    @Override
    protected int getHorizontalSize(int w)
    {
        return super.getHorizontalSize(w) + 10 + this.placements;
    }
}