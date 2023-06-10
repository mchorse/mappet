package mchorse.mappet.client.gui.scripts;

import mchorse.mappet.client.gui.scripts.highlights.Highlighters;
import mchorse.mappet.client.gui.scripts.utils.HighlightedTextLine;
import mchorse.mappet.client.gui.scripts.utils.SyntaxHighlighter;
import mchorse.mappet.client.gui.scripts.utils.TextLineNumber;
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
import net.minecraft.init.SoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiTextEditor extends GuiMultiTextElement<HighlightedTextLine>
{
    private SyntaxHighlighter highlighter;
    private int placements;
    private boolean lines = true;

    private List<TextLineNumber> numbers = new ArrayList<TextLineNumber>(40);
    private int lineNumber = 0;

    public GuiTextEditor(Minecraft mc, Consumer<String> callback)
    {
        super(mc, callback);

        this.highlighter = Highlighters.readHighlighter(Highlighters.highlighterFile("js.json"));
    }

    @Override
    protected HighlightedTextLine createTextLine(String line)
    {
        return new HighlightedTextLine(line);
    }

    public GuiTextEditor disableLines()
    {
        this.lines = false;

        return this;
    }

    public SyntaxHighlighter getHighlighter()
    {
        return this.highlighter;
    }

    public void setHighlighter(SyntaxHighlighter highlighter)
    {
        this.highlighter = highlighter;
    }

    public void resetHighlight()
    {
        for (HighlightedTextLine textLine : this.text)
        {
            textLine.resetSegments();
        }
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);

        /* It will be null before when it will get called from parent's constructor */
        this.resetHighlight();
    }

    @Override
    protected void recalculateSizes()
    {
        /* Calculate how many pixels will number lines will occupy horizontally */
        double power = Math.ceil(Math.log10(this.text.size() + 1));

        this.placements = (int) power * 6;

        super.recalculateSizes();
    }

    @Override
    protected void changedLine(int i)
    {
        String line = this.text.get(i).text;

        if (line.contains("/*") || line.contains("*/"))
        {
            this.changedLineAfter(i);
        }
        else
        {
            super.changedLine(i);
            this.text.get(i).resetSegments();
        }
    }

    @Override
    protected void changedLineAfter(int i)
    {
        super.changedLineAfter(i);

        while (i < this.text.size())
        {
            this.text.get(i).resetSegments();

            i += 1;
        }
    }

    /* Change input behavior */

    @Override
    protected String getFromChar(char typedChar)
    {
        if (
                this.wasDoubleInsert(typedChar, ')', '(') ||
                        this.wasDoubleInsert(typedChar, ']', '[') ||
                        this.wasDoubleInsert(typedChar, '}', '{') ||
                        this.wasDoubleInsert(typedChar, '"', '"') ||
                        this.wasDoubleInsert(typedChar, '\'', '\'')
        )
        {
            this.moveCursor(1, 0);
            this.playSound(SoundEvents.BLOCK_STONE_PLACE);

            return "";
        }

        if (typedChar == '(')
        {
            return "()";
        }
        else if (typedChar == '[')
        {
            return "[]";
        }
        else if (typedChar == '{')
        {
            return "{}";
        }
        else if (typedChar == '"')
        {
            return "\"\"";
        }
        else if (typedChar == '\'')
        {
            return "''";
        }

        return super.getFromChar(typedChar);
    }

    private boolean wasDoubleInsert(char input, char target, char supplementary)
    {
        if (input != target)
        {
            return false;
        }

        String line = this.text.get(this.cursor.line).text;

        return line.length() >= 2
                && this.cursor.offset > 0
                && this.cursor.offset < line.length()
                && line.charAt(this.cursor.offset) == target
                && line.charAt(this.cursor.offset - 1) == supplementary;
    }

    @Override
    protected void keyNewLine(TextEditUndo undo)
    {
        String line = this.text.get(this.cursor.line).text;
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
    protected void keyBackspace(TextEditUndo undo, boolean ctrl)
    {
        String line = this.text.get(this.cursor.line).text;

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
            super.keyBackspace(undo, ctrl);
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
            List<String> splits = GuiMultiTextElement.splitNewlineString(this.getSelectedText());

            for (int i = 0; i < splits.size(); i++)
            {
                if (shift)
                {
                    int indent = this.getIndent(splits.get(i));

                    splits.set(i, splits.get(i).substring(Math.min(indent, 4)));
                }
                else
                {
                    splits.set(i, "    " + splits.get(i));
                }
            }

            String result = String.join("\n", splits);

            temp.copy(min);
            this.deleteSelection();
            this.writeString(result);
            this.getMin().set(min.line, splits.get(splits.size() - 1).length());
            min.copy(temp);

            if (!shift)
            {
                min.offset += 4;
            }

            undo.postText = result;
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
            return this.getIndent(this.text.get(i).text);
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
    protected void drawTextLine(String line, int i, int j, int nx, int ny)
    {
        /* Cache line number to be later rendered in drawForeground() */
        if (this.lines && j == 0)
        {
            String label = String.valueOf(i + 1);

            int x = this.area.x + 5 + this.placements - this.font.getStringWidth(label);

            if (this.lineNumber >= this.numbers.size())
            {
                this.numbers.add(new TextLineNumber());
            }

            this.numbers.get(this.lineNumber).set(label, x, ny);
            this.lineNumber += 1;
        }

        /* Draw  */
        HighlightedTextLine textLine = this.text.get(i);

        if (textLine.segments == null)
        {
            textLine.setSegments(this.highlighter.parse(this.font, this.text, textLine.text, i));

            if (textLine.wrappedLines != null)
            {
                textLine.calculateWrappedSegments(this.font);
            }
        }

        List<TextSegment> segments = textLine.segments;

        if (textLine.wrappedSegments != null)
        {
            segments = j < textLine.wrappedSegments.size() ? textLine.wrappedSegments.get(j) : null;
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
        return this.lines ? 10 + this.placements : 0;
    }

    @Override
    protected void drawBackground()
    {
        this.area.draw(0xff000000 + ColorUtils.multiplyColor(this.highlighter.getStyle().background, 0.8F));
    }

    @Override
    protected void drawForeground(GuiContext context)
    {
        if (this.lines)
        {
            /* Draw line numbers background */
            int x = this.area.x + this.getShiftX();

            Gui.drawRect(this.area.x, this.area.y, x, this.area.ey(), 0xff000000 + this.highlighter.getStyle().background);

            /* Draw cached line numbers */
            for (TextLineNumber number : this.numbers)
            {
                if (!number.draw)
                {
                    break;
                }

                this.font.drawString(number.line, number.x, number.y, this.highlighter.getStyle().lineNumbers);
                number.draw = false;
            }

            this.lineNumber = 0;

            /* Draw shadow to the right of line numbers when scrolling */
            int a = (int) (Math.min(this.horizontal.scroll / 10F, 1F) * 0x44);

            if (a > 0)
            {
                GuiDraw.drawHorizontalGradientRect(x, this.area.y, x + 10, this.area.ey(), a << 24, 0);
            }
        }
    }
}