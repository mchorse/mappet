package mchorse.mappet.client.gui.scripts;

import com.google.common.collect.ImmutableSet;
import mchorse.mappet.client.gui.utils.GuiMultiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class GuiTextEditor extends GuiMultiTextElement
{
    private static final Set<String> OPERATORS = ImmutableSet.of("+", "-", "=", "/", "*", "<", ">", "~", "&", "|");
    private static final Set<String> PRIMARY_KEYWORDS = ImmutableSet.of("break", "continue", "switch", "case", "default", "try", "catch", "debugger", "delete", "do", "while", "else", "finally", "if", "else", "for", "in", "instanceof", "new", "throw", "typeof", "with", "yeild", "import", "from", "return");
    private static final Set<String> SECONDARY_KEYWORDS = ImmutableSet.of("const", "function", "var", "let", "class", "Math", "JSON", "mappet");
    private static final Set<String> TYPE_KEYSWORDS = ImmutableSet.of("true", "false", "null", "undefined");

    private static final int PRIMARY = 0xf92472;
    private static final int SECONDARY = 0x67d8ef;
    private static final int STRINGS = 0xe7db74;
    private static final int COMMENTS = 0x74705d;
    private static final int NUMBERS = 0xac80ff;
    private static final int LINE_NUMBERS = 0x90918b;
    private static final int BACKGROUND = 0x282923;
    private static final int OTHER = 0xffffff;

    private List<List<TextSegment>> segments = new ArrayList<List<TextSegment>>();
    private int placements;

    public GuiTextEditor(Minecraft mc, Consumer<String> callback)
    {
        super(mc, callback);
    }

    @Override
    public void setText(String text)
    {
        super.setText(text);

        this.segments.clear();
        this.ensureSize();
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

        this.placements = (int) Math.ceil(Math.pow(this.text.size() + 1, 0.1D)) * 6;
    }

    private List<TextSegment> parseSegments(String line, int lineIndex)
    {
        List<TextSegment> list = new ArrayList<TextSegment>();
        List<TextSegment> prevLine = lineIndex > 0 ? this.segments.get(lineIndex - 1) : null;

        if (prevLine != null && !prevLine.isEmpty())
        {
            TextSegment last = prevLine.get(prevLine.size() - 1);

            if (last.color == COMMENTS && !last.text.startsWith("//") && !last.text.trim().endsWith("*/"))
            {
                list.add(new TextSegment(line, COMMENTS, 0));

                return list;
            }
        }

        String buffer = "";
        char string = '\0';
        int last = 0;

        main:
        for (int i = 0, c = line.length(); i < c; i++)
        {
            char character = line.charAt(i);

            /* Strings */
            if (character == '\'' || character == '"')
            {
                if (string == '\0')
                {
                    list.add(new TextSegment(buffer, OTHER, this.font.getStringWidth(buffer)));

                    buffer = "";
                    string = character;
                }
                else if (string == character)
                {
                    char prev = i > 0 ? line.charAt(i - 1) : '\0';

                    if (prev != '\\')
                    {
                        string = '\0';
                        buffer += character;
                        list.add(new TextSegment(buffer, STRINGS, this.font.getStringWidth(buffer)));

                        buffer = "";

                        continue;
                    }
                }
            }

            boolean isString = string != '\0';

            /* Multiline comments */
            if (!isString && character == '/' && i < c - 1 && line.charAt(i + 1) == '*')
            {
                int lastI = i;

                i += 2;

                while (i < c)
                {
                    character = line.charAt(i);

                    if (character == '*' && i < c - 1 && line.charAt(i + 1) == '/')
                    {
                        String comment = line.substring(lastI, i + 2);

                        list.add(new TextSegment(buffer, OTHER, this.font.getStringWidth(buffer)));
                        list.add(new TextSegment(comment, COMMENTS, this.font.getStringWidth(comment)));

                        i += 1;
                        buffer = "";

                        continue main;
                    }

                    i += 1;
                }

                String comment = line.substring(lastI);

                list.add(new TextSegment(buffer, OTHER, this.font.getStringWidth(buffer)));
                list.add(new TextSegment(comment, COMMENTS, 0));

                return list;
            }

            /* One line comments */
            if (!isString && character == '/' && i < c - 1 && line.charAt(i + 1) == '/')
            {
                String comment = line.substring(i);

                list.add(new TextSegment(buffer, OTHER, this.font.getStringWidth(buffer)));
                list.add(new TextSegment(comment, COMMENTS, 0));

                return list;
            }

            /* Operators */
            if (!isString && OPERATORS.contains(String.valueOf(character)))
            {
                String sign = String.valueOf(character);

                list.add(new TextSegment(buffer, OTHER, this.font.getStringWidth(buffer)));
                list.add(new TextSegment(sign, PRIMARY, this.font.getStringWidth(sign)));

                buffer = "";

                continue;
            }

            buffer += character;

            /* Keywords */
            if (i < c - 1)
            {
                if (!Character.isLetterOrDigit(line.charAt(last)))
                {
                    last += 1;
                }

                String keyword = line.substring(last, i + 1).trim();

                if (!isString && PRIMARY_KEYWORDS.contains(keyword))
                {
                    if (buffer.length() > keyword.length())
                    {
                        String other = buffer.substring(0, buffer.length() - keyword.length());

                        list.add(new TextSegment(other, OTHER, this.font.getStringWidth(other)));
                    }

                    list.add(new TextSegment(keyword, PRIMARY, this.font.getStringWidth(keyword)));

                    buffer = "";
                    last = i + 1;
                }
                else if (!isString && SECONDARY_KEYWORDS.contains(keyword))
                {
                    if (buffer.length() > keyword.length())
                    {
                        String other = buffer.substring(0, buffer.length() - keyword.length());

                        list.add(new TextSegment(other, OTHER, this.font.getStringWidth(other)));
                    }

                    list.add(new TextSegment(keyword, SECONDARY, this.font.getStringWidth(keyword)));

                    buffer = "";
                    last = i + 1;
                }
                else if (!isString && this.isNumberOrBool(keyword))
                {
                    if (buffer.length() > keyword.length())
                    {
                        String other = buffer.substring(0, buffer.length() - keyword.length());

                        list.add(new TextSegment(other, OTHER, this.font.getStringWidth(other)));
                    }

                    list.add(new TextSegment(keyword, NUMBERS, this.font.getStringWidth(keyword)));

                    buffer = "";
                    last = i + 1;
                }
            }

            if (!Character.isLetterOrDigit(character))
            {
                last = i;
            }
        }

        if (!buffer.trim().isEmpty())
        {
            list.add(new TextSegment(buffer, OTHER, 0));
        }

        return list;
    }

    private boolean isNumberOrBool(String keyword)
    {
        if (TYPE_KEYSWORDS.contains(keyword))
        {
            return true;
        }

        try
        {
            Double.parseDouble(keyword);

            return true;
        }
        catch (NumberFormatException e)
        {}

        return false;
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

    @Override
    protected void drawTextLine(String line, int i, int nx, int ny, int sw)
    {
        if (this.segments.size() == this.text.size())
        {
            List<TextSegment> segments = this.segments.get(i);

            if (segments == null)
            {
                segments = this.parseSegments(line, i);
                this.segments.set(i, segments);
            }

            if (segments != null)
            {
                for (TextSegment segment : segments)
                {
                    this.font.drawString(segment.text, nx, ny, segment.color, true);

                    nx += segment.width;
                }
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
        this.area.draw(0xff000000 + ColorUtils.multiplyColor(BACKGROUND, 0.8F));
    }

    @Override
    protected void drawForeground(GuiContext context)
    {
        int start = Math.max(0, (this.vertical.scroll - 10) / this.lineHeight);

        int y = start * this.lineHeight;
        int x = this.area.x + 10 + this.placements;

        Gui.drawRect(this.area.x, this.area.y, x, this.area.ey(), 0xff000000 + BACKGROUND);

        for (int i = start; i < this.text.size(); i++)
        {
            int ny = this.area.y + this.padding + y - this.vertical.scroll;

            if (ny > this.area.ey())
            {
                break;
            }

            String label = String.valueOf(i + 1);

            this.font.drawString(label, this.area.x + 5 + this.placements - this.font.getStringWidth(label), ny, LINE_NUMBERS);

            y += this.lineHeight;
        }

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

    public static class TextSegment
    {
        public String text;
        public int color;
        public int width;

        public TextSegment(String text, int color, int width)
        {
            this.text = text;
            this.color = color;
            this.width = width;
        }
    }
}