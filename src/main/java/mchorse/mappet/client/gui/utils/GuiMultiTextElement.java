package mchorse.mappet.client.gui.utils;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.IFocusedGuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.ScrollArea;
import mchorse.mclib.client.gui.utils.ScrollDirection;
import mchorse.mclib.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class GuiMultiTextElement extends GuiElement implements IFocusedGuiElement
{
    public ScrollArea horizontal = new ScrollArea();
    public ScrollArea vertical = new ScrollArea();

    public Consumer<String> callback;

    /* Visual properties */
    private boolean background;
    private int padding = 10;
    private int lineHeight = 12;

    /* Editing */
    private boolean focused;
    private int dragging;
    private List<String> text = new ArrayList<String>();
    private Cursor cursor = new Cursor(0, 0);
    private Cursor selection = new Cursor(-1, -1);

    private int lastMX;
    private int lastMY;
    private long update;
    private long lastUpdate;

    public GuiMultiTextElement(Minecraft mc, Consumer<String> callback)
    {
        super(mc);

        this.callback = callback;

        this.horizontal.direction = ScrollDirection.HORIZONTAL;
        this.horizontal.cancelScrollEdge = true;
        this.horizontal.scrollSpeed = this.lineHeight * 2;
        this.vertical.cancelScrollEdge = true;
        this.vertical.scrollSpeed = this.lineHeight * 2;
    }

    public GuiMultiTextElement background()
    {
        return this.background(true);
    }

    public GuiMultiTextElement background(boolean background)
    {
        this.background = background;

        return this;
    }

    public GuiMultiTextElement padding(int padding)
    {
        this.padding = padding;

        return this;
    }

    public GuiMultiTextElement lineHeight(int lineHeight)
    {
        this.lineHeight = lineHeight;

        return this;
    }

    public void setText(String text)
    {
        this.text.clear();
        this.text.addAll(Arrays.asList(text.split("\n")));

        this.cursor.set(0, 0);
    }

    public String getText()
    {
        return String.join("\n", this.text);
    }

    /* Selection API */

    public boolean isSelecting()
    {
        return this.selection.line >= 0;
    }

    public void startSelecting()
    {
        this.selection.copy(this.cursor);
    }

    public void deselect()
    {
        this.selection.set(-1, -1);
    }

    public void selectAll()
    {
        this.cursor.set(0, 0);

        this.startSelecting();
        this.cursor.line = this.text.size() - 1;
        this.moveCursorToLineEnd();
    }

    public String getSelectedText()
    {
        if (!this.isSelecting())
        {
            return "";
        }

        StringJoiner joiner = new StringJoiner("\n");

        Cursor min = this.cursor;
        Cursor max = this.selection;

        if (this.selection.isGreater(this.cursor))
        {
            min = this.selection;
            max = this.cursor;
        }

        for (int i = min.line; i <= Math.min(max.line, this.text.size() - 1); i++)
        {
            String line = this.text.get(i);

            if (i == min.line && i == max.line)
            {
                joiner.add(line.substring(min.getOffset(line), max.getOffset(line)));
            }
            else if (i == min.line)
            {
                joiner.add(min.end(line));
            }
            else if (i == max.line)
            {
                joiner.add(max.start(line));
            }
            else
            {
                joiner.add(line);
            }
        }

        return joiner.toString();
    }

    public void checkSelection(boolean selecting)
    {
        if (selecting && !this.isSelecting())
        {
            this.startSelecting();
        }
        else if (!selecting && this.isSelecting())
        {
            this.deselect();
        }
    }

    /* Writing API */

    public void writeNewLine()
    {
        if (!this.hasLine(this.cursor.line))
        {
            return;
        }

        String line = this.text.get(this.cursor.line);

        if (this.cursor.offset == 0 || line.isEmpty())
        {
            this.text.add(this.cursor.line, "");
        }
        else if (this.cursor.offset >= line.length())
        {
            this.text.add(this.cursor.line + 1, "");
        }
        else
        {
            this.text.set(this.cursor.line, this.cursor.start(line));
            this.text.add(this.cursor.line + 1, this.cursor.end(line));
            this.moveCursorToLineStart();
        }

        this.cursor.line += 1;
        this.cursor.offset = 0;
    }

    public void writeCharacter(String character)
    {
        if (this.hasLine(this.cursor.line))
        {
            String line = this.text.get(this.cursor.line);
            int index = this.cursor.offset;

            if (index >= line.length())
            {
                line += character;
            }
            else if (index == 0)
            {
                line = character + line;
            }
            else
            {
                line = this.cursor.start(line) + character + this.cursor.end(line);
            }

            this.text.set(this.cursor.line, line);
        }
    }

    public void writeString(String string)
    {
        String[] splits = string.split("\n");

        if (splits.length == 1)
        {
            this.writeCharacter(string);
        }
        else
        {
            for (int i = 0; i < splits.length; i++)
            {
                if (i != 0 && i <= splits.length - 1)
                {
                    this.cursor.line += 1;

                    this.moveCursorToLineStart();
                    this.text.add(this.cursor.line, "");
                }

                this.writeCharacter(splits[i]);
            }

            this.cursor.offset = splits[splits.length - 1].length();
        }
    }

    public void deleteCharacter()
    {
        if (this.hasLine(this.cursor.line))
        {
            String line = this.text.get(this.cursor.line);
            int index = Math.min(this.cursor.offset, line.length());

            if (line.isEmpty())
            {
                if (this.cursor.line > 0)
                {
                    this.text.remove(this.cursor.line);

                    this.cursor.line -= 1;

                    this.moveCursorToLineEnd();
                }
            }
            else if (index >= line.length())
            {
                line = line.substring(0, line.length() - 1);
                this.text.set(this.cursor.line, line);
                this.moveCursorToLineEnd();
            }
            else if (index == 0)
            {
                if (this.cursor.line > 0)
                {
                    String text = this.text.remove(this.cursor.line);

                    this.cursor.line -= 1;

                    this.moveCursorToLineEnd();
                    this.text.set(this.cursor.line, this.text.get(this.cursor.line) + text);
                }
            }
            else
            {
                line = this.cursor.start(line, -1) + this.cursor.end(line);
                this.text.set(this.cursor.line, line);
                this.moveCursor(-1, 0);
            }
        }
    }

    public void deleteSelection()
    {
        if (!this.isSelecting())
        {
            return;
        }

        Cursor min = this.cursor;
        Cursor max = this.selection;

        if (this.selection.isGreater(this.cursor))
        {
            min = this.selection;
            max = this.cursor;
        }

        if (min.line == max.line)
        {
            String line = this.text.get(min.line);

            if (min.offset <= 0 && max.offset >= line.length())
            {
                this.text.set(min.line, "");
            }
            else
            {
                this.text.set(min.line, min.start(line) + max.end(line));
            }
        }
        else
        {
            String end = "";

            for (int i = max.line; i >= min.line; i--)
            {
                String line = this.text.get(i);

                if (i == max.line)
                {
                    end = max.end(line);
                    this.text.remove(i);
                }
                else if (i == min.line)
                {
                    this.text.set(i, min.start(line) + end);
                }
                else
                {
                    this.text.remove(i);
                }
            }
        }

        this.cursor.copy(min);
        this.deselect();
    }

    public boolean hasLine(int line)
    {
        return line >= 0 && line < this.text.size();
    }

    /* Moving cursor API */

    public void moveCursor(int x, int y)
    {
        if (!this.hasLine(this.cursor.line))
        {
            return;
        }

        String line = this.text.get(this.cursor.line);

        if (x != 0)
        {
            int nx = this.cursor.offset + (x > 0 ? 1 : -1);

            if (nx < 0)
            {
                if (this.hasLine(this.cursor.line - 1))
                {
                    this.cursor.line -= 1;
                    this.moveCursorToLineEnd();
                }
            }
            else if (nx > line.length())
            {
                if (this.hasLine(this.cursor.line + 1))
                {
                    this.cursor.line += 1;
                    this.moveCursorToLineStart();
                }
            }
            else
            {
                this.cursor.offset = nx;
            }
        }

        if (y != 0)
        {
            int ny = this.cursor.line + (y > 0 ? 1 : -1);

            if (ny >= 0 && ny < this.text.size())
            {
                this.cursor.line = ny;
            }
        }
    }

    public void moveCursorToLineStart()
    {
        this.cursor.offset = 0;
    }

    public void moveCursorToLineEnd()
    {
        if (this.hasLine(this.cursor.line))
        {
            this.cursor.offset = this.text.get(this.cursor.line).length();
        }
    }

    public void moveCursorTo(Cursor cursor, int x, int y)
    {
        x -= this.area.x + this.padding;
        y -= this.area.y + this.padding;

        x += this.horizontal.scroll;
        y += this.vertical.scroll;

        cursor.line = MathUtils.clamp(y / this.lineHeight, 0, this.text.size() - 1);

        String line = this.text.get(cursor.line);
        int w = this.font.getStringWidth(line);

        if (x <= 0)
        {
            this.moveCursorToLineStart();
        }
        else if (x > w)
        {
            this.moveCursorToLineEnd();
        }
        else
        {
            cursor.offset = 0;
            w = this.font.getStringWidth(cursor.start(line));

            while (x > w)
            {
                w = this.font.getStringWidth(cursor.start(line, 1));

                cursor.offset += 1;
            }

            if (cursor.offset > 0)
            {
                cursor.offset -= 1;
            }
        }
    }

    /* Focusable */

    @Override
    public boolean isFocused()
    {
        return this.focused;
    }

    @Override
    public void focus(GuiContext context)
    {
        this.focused = true;

        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void unfocus(GuiContext context)
    {
        this.focused = false;

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void selectAll(GuiContext context)
    {
        /* TODO: ... */
    }

    @Override
    public void unselect(GuiContext context)
    {
        /* TODO: ... */
    }

    /* GUI input handling */

    @Override
    public void resize()
    {
        super.resize();

        this.horizontal.copy(this.area);
        this.horizontal.clamp();
        this.vertical.copy(this.area);
        this.vertical.clamp();
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        if (this.horizontal.mouseClicked(context) || this.vertical.mouseClicked(context))
        {
            return true;
        }

        boolean wasFocused = this.focused;
        boolean shift = GuiScreen.isShiftKeyDown();

        this.focused = this.area.isInside(context);

        if (this.focused)
        {
            if (!shift)
            {
                this.deselect();

                this.dragging = 1;
            }
            else if (!this.isSelecting())
            {
                this.startSelecting();
            }

            this.moveCursorTo(this.cursor, context.mouseX, context.mouseY);
            this.lastMX = context.mouseX;
            this.lastMY = context.mouseY;
        }

        if (wasFocused != this.focused)
        {
            context.focus(wasFocused ? null : this);
        }

        return this.focused;
    }

    @Override
    public boolean mouseScrolled(GuiContext context)
    {
        if (super.mouseScrolled(context))
        {
            return true;
        }

        return GuiScreen.isShiftKeyDown() ? this.horizontal.mouseScroll(context) : this.vertical.mouseScroll(context);
    }

    @Override
    public void mouseReleased(GuiContext context)
    {
        super.mouseReleased(context);

        this.horizontal.mouseReleased(context);
        this.vertical.mouseReleased(context);
        this.dragging = 0;
    }

    @Override
    public boolean keyTyped(GuiContext context)
    {
        if (super.keyTyped(context))
        {
            return true;
        }

        if (!this.focused)
        {
            return false;
        }

        boolean ctrl = GuiScreen.isCtrlKeyDown();
        boolean shift = GuiScreen.isShiftKeyDown();

        if (context.keyCode == Keyboard.KEY_A && ctrl)
        {
            this.selectAll();
        }
        else if (context.keyCode == Keyboard.KEY_C && ctrl)
        {
            GuiScreen.setClipboardString(this.getSelectedText());
        }
        else if (context.keyCode == Keyboard.KEY_V && ctrl)
        {
            this.deleteSelection();
            this.deselect();
            this.writeString(GuiScreen.getClipboardString());
        }
        else if (context.keyCode == Keyboard.KEY_UP || context.keyCode == Keyboard.KEY_DOWN || context.keyCode == Keyboard.KEY_RIGHT || context.keyCode == Keyboard.KEY_LEFT)
        {
            int x = context.keyCode == Keyboard.KEY_RIGHT ? 1 : (context.keyCode == Keyboard.KEY_LEFT ? -1 : 0);
            int y = context.keyCode == Keyboard.KEY_UP ? -1 : (context.keyCode == Keyboard.KEY_DOWN ? 1 : 0);

            this.checkSelection(shift);
            this.moveCursor(x, y);
        }
        else if (context.keyCode == Keyboard.KEY_HOME)
        {
            this.checkSelection(shift);
            this.moveCursorToLineStart();
        }
        else if (context.keyCode == Keyboard.KEY_END)
        {
            this.checkSelection(shift);
            this.moveCursorToLineEnd();
        }
        else if (context.keyCode == Keyboard.KEY_RETURN)
        {
            this.deleteSelection();
            this.deselect();
            this.writeNewLine();
        }
        else if (context.keyCode == Keyboard.KEY_BACK || context.keyCode == Keyboard.KEY_DELETE)
        {
            if (this.isSelecting())
            {
                this.deleteSelection();
                this.deselect();
            }
            else
            {
                if (context.keyCode == Keyboard.KEY_DELETE)
                {
                    this.moveCursor(1, 0);
                }

                this.deleteCharacter();
            }
        }
        else if (ChatAllowedCharacters.isAllowedCharacter(context.typedChar))
        {
            this.deleteSelection();
            this.deselect();
            this.writeCharacter(String.valueOf(context.typedChar));
            this.moveCursor(1, 0);
        }

        this.update = context.tick + 20;
        this.horizontal.clamp();
        this.vertical.clamp();

        return false;
    }

    @Override
    public void draw(GuiContext context)
    {
        this.handleLogic(context);

        if (this.background)
        {
            this.drawBackground();
        }

        super.draw(context);

        GuiDraw.scissor(this.area.x, this.area.y, this.area.w, this.area.h, context);

        int x = this.area.x + this.padding;
        int y = this.area.y + this.padding;
        int w = 0;
        int i = 0;

        Cursor min = this.cursor;
        Cursor max = this.selection;

        if (this.selection.isGreater(this.cursor))
        {
            min = this.selection;
            max = this.cursor;
        }

        for (String line : this.text)
        {
            int sw = this.font.getStringWidth(line);
            int nx = x - this.horizontal.scroll;
            int ny = y - this.vertical.scroll;

            if (ny + this.font.FONT_HEIGHT >= this.area.y && ny < this.area.ey())
            {
                if (this.isSelecting())
                {
                    this.drawSelectionBar(nx, ny, line, sw, i, min, max);
                }

                if (this.cursor.line == i && this.focused)
                {
                    int nw = line.isEmpty() ? 0 : this.font.getStringWidth(this.cursor.start(line));
                    int a = (int) (Math.sin((context.tick + context.partialTicks) / 2D) * 127.5 + 127.5) << 24;

                    Gui.drawRect(nx + nw, ny - 1, nx + nw + 1, ny + this.font.FONT_HEIGHT + 1, a + 0xffffff);
                }

                this.font.drawStringWithShadow(line, nx, ny, 0xffffff);
            }

            w = Math.max(sw, w);
            y += this.lineHeight;
            i += 1;
        }

        this.horizontal.scrollSize = w + this.padding * 2;
        this.vertical.scrollSize = y - this.area.y + this.padding - (this.text.isEmpty() ? 0 : this.lineHeight - this.font.FONT_HEIGHT);

        this.horizontal.drawScrollbar();
        this.vertical.drawScrollbar();

        GuiDraw.unscissor(context);
    }

    protected void drawBackground()
    {
        this.area.draw(0xffa0a0a0);
        this.area.draw(0xff000000, 1);
    }

    /**
     * Handle dragging scrollbars and selecting text
     */
    private void handleLogic(GuiContext context)
    {
        if (this.update > this.lastUpdate)
        {
            this.lastUpdate = this.update;

            if (this.callback != null)
            {
                this.callback.accept(this.getText());
            }
        }

        if (this.dragging == 1 && (Math.abs(context.mouseX - this.lastMX) > 4 || Math.abs(context.mouseY - this.lastMY) > 4))
        {
            this.startSelecting();
            this.dragging = 2;
        }

        if (this.focused && this.dragging == 2)
        {
            this.moveCursorTo(this.cursor, context.mouseX, context.mouseY);
        }

        this.horizontal.drag(context);
        this.vertical.drag(context);
    }

    /**
     * Draw background selection under every line when that line is selected
     */
    private void drawSelectionBar(int nx, int ny, String line, int sw, int i, Cursor min, Cursor max)
    {
        final int selectionPad = 2;
        int color = 0x88000000 + McLib.primaryColor.get();

        if (min.line == i && max.line == i)
        {
            int lw1 = this.font.getStringWidth(min.start(line));
            int lw2 = this.font.getStringWidth(max.start(line));

            Gui.drawRect(nx + lw1, ny - selectionPad, nx + lw2, ny + this.font.FONT_HEIGHT + selectionPad, color);
        }
        else if (min.line == i)
        {
            int lw = this.font.getStringWidth(min.start(line));

            Gui.drawRect(nx + lw, ny - selectionPad, nx + sw, ny + this.font.FONT_HEIGHT + selectionPad, color);
        }
        else if (max.line == i)
        {
            int lw = this.font.getStringWidth(max.start(line));

            Gui.drawRect(nx, ny - selectionPad, nx + lw, ny + this.font.FONT_HEIGHT + selectionPad, color);
        }
        else if (i < max.line && i > min.line)
        {
            Gui.drawRect(nx, ny - selectionPad, nx + (line.isEmpty() ? 2 : sw), ny + this.font.FONT_HEIGHT + selectionPad, color);
        }
    }

    public static class Cursor
    {
        public int line;
        public int offset;

        public Cursor(int line, int offset)
        {
            this.set(line, offset);
        }

        public void set(int line, int offset)
        {
            this.line = line;
            this.offset = offset;
        }

        public void copy(Cursor cursor)
        {
            this.set(cursor.line, cursor.offset);
        }

        /* String clipping */

        public String start(String line)
        {
            return this.start(line, 0);
        }

        public String start(String line, int offset)
        {
            return line.isEmpty() ? line : line.substring(0, this.getOffset(line, offset));
        }

        public String end(String line)
        {
            return this.end(line, 0);
        }

        public String end(String line, int offset)
        {
            return line.isEmpty() ? line : line.substring(this.getOffset(line, offset));
        }

        public int getOffset(String line)
        {
            return this.getOffset(line, 0);
        }

        public int getOffset(String line, int offset)
        {
            return MathUtils.clamp(this.offset + offset, 0, line.length());
        }

        /**
         * Whether given cursor is greater than this one
         */
        public boolean isGreater(Cursor cursor)
        {
            if (this.line == cursor.line)
            {
                return this.offset < cursor.offset;
            }

            return this.line < cursor.line;
        }
    }
}