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
import java.util.regex.Pattern;

public class GuiMultiTextElement extends GuiElement implements IFocusedGuiElement
{
    public ScrollArea horizontal = new ScrollArea();
    public ScrollArea vertical = new ScrollArea();

    public Consumer<String> callback;

    /* Visual properties */
    private boolean background;
    protected int padding = 10;
    protected int lineHeight = 12;

    /* Editing */
    private boolean focused;
    private int dragging;
    protected List<String> text = new ArrayList<String>();
    protected Cursor cursor = new Cursor(0, 0);
    protected Cursor selection = new Cursor(-1, -1);

    /* Last mouse position */
    private int lastMX;
    private int lastMY;
    private long lastClick;

    /* Callback update (to avoid joining a huge array of text every keystroke) */
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
        this.horizontal.scroll = 0;
        this.vertical.scroll = 0;
    }

    public String getText()
    {
        return String.join("\n", this.text);
    }

    /* Selection API */

    public boolean isSelected()
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
        if (!this.isSelected())
        {
            return "";
        }

        StringJoiner joiner = new StringJoiner("\n");

        Cursor min = this.getMin();
        Cursor max = this.getMax();

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

    public boolean selectGroup(int direction, boolean select)
    {
        String line = this.text.get(this.cursor.line);

        if (line.isEmpty() || this.cursor.offset >= line.length() - 1)
        {
            return false;
        }

        int offset = this.cursor.offset;

        String character = String.valueOf(line.charAt(offset));
        StringGroup group = StringGroup.get(character);

        int min = offset;
        int max = offset;

        if (direction <= 0)
        {
            while (min > 0)
            {
                if (group.match(String.valueOf(line.charAt(min - 1))))
                {
                    min -= 1;
                }
                else
                {
                    break;
                }
            }
        }

        if (direction >= 0)
        {
            while (max < line.length())
            {
                if (group.match(String.valueOf(line.charAt(max))))
                {
                    max += 1;
                }
                else
                {
                    break;
                }
            }
        }

        if (offset == max && offset == min)
        {
            return false;
        }

        if (select)
        {
            if (direction == 0)
            {
                this.cursor.offset = max;
                this.selection.set(this.cursor.line, min);
            }
            else
            {
                if (!this.isSelected())
                {
                    this.selection.copy(this.cursor);
                }

                this.cursor.offset = direction < 0 ? min : max;
            }
        }
        else
        {
            this.deselect();
            this.cursor.offset = direction < 0 ? min : max;
        }

        return true;
    }

    public void checkSelection(boolean selecting)
    {
        if (selecting && !this.isSelected())
        {
            this.startSelecting();
        }
        else if (!selecting && this.isSelected())
        {
            this.deselect();
        }
    }

    /* Writing API */

    protected void changedLine(int i)
    {}

    protected void changedLineAfter(int i)
    {}

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

        this.changedLineAfter(this.cursor.line);

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
            this.changedLine(this.cursor.line);
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
            int line = this.cursor.line;

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
            this.changedLineAfter(line);
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
                    this.changedLineAfter(this.cursor.line);
                }
            }
            else if (index >= line.length())
            {
                line = line.substring(0, line.length() - 1);
                this.text.set(this.cursor.line, line);
                this.moveCursorToLineEnd();

                this.changedLine(this.cursor.line);
            }
            else if (index == 0)
            {
                if (this.cursor.line > 0)
                {
                    String text = this.text.remove(this.cursor.line);

                    this.cursor.line -= 1;

                    this.moveCursorToLineEnd();
                    this.text.set(this.cursor.line, this.text.get(this.cursor.line) + text);
                    this.changedLineAfter(this.cursor.line);
                }
            }
            else
            {
                line = this.cursor.start(line, -1) + this.cursor.end(line);
                this.text.set(this.cursor.line, line);
                this.moveCursor(-1, 0);
                this.changedLine(this.cursor.line);
            }
        }
    }

    public void deleteSelection()
    {
        if (!this.isSelected())
        {
            return;
        }

        Cursor min = this.getMin();
        Cursor max = this.getMax();

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

        this.changedLineAfter(min.line);
        this.cursor.copy(min);
        this.deselect();
    }

    public boolean hasLine(int line)
    {
        return line >= 0 && line < this.text.size();
    }

    protected Cursor getMin()
    {
        return this.selection.isGreater(this.cursor) ? this.selection : this.cursor;
    }

    protected Cursor getMax()
    {
        return this.selection.isGreater(this.cursor) ? this.cursor : this.selection;
    }

    /* Moving cursor API */

    public void moveCursor(int x, int y)
    {
        this.moveCursor(x, y, true);
    }

    public void moveCursor(int x, int y, boolean jumpLine)
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
                if (jumpLine)
                {
                    if (this.hasLine(this.cursor.line - 1))
                    {
                        this.cursor.line -= 1;
                        this.moveCursorToLineEnd();
                    }
                }
                else
                {
                    this.moveCursorToLineStart();
                }
            }
            else if (nx > line.length())
            {
                if (jumpLine)
                {
                    if (this.hasLine(this.cursor.line + 1))
                    {
                        this.cursor.line += 1;
                        this.moveCursorToLineStart();
                    }
                }
                else
                {
                    this.moveCursorToLineEnd();
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

        x += this.horizontal.scroll - this.getShiftX();
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

    public void moveViewportToCursor()
    {
        if (!this.hasLine(this.cursor.line))
        {
            return;
        }

        int x = this.font.getStringWidth(this.cursor.start(this.text.get(this.cursor.line)));
        int y = this.cursor.line * this.lineHeight;
        int w = 4;
        int h = this.lineHeight;

        this.horizontal.scrollIntoView(x, w + this.padding * 2);
        this.vertical.scrollIntoView(y, h + this.padding * 2);
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
        this.selectAll();
    }

    @Override
    public void unselect(GuiContext context)
    {
        this.deselect();
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
            if (context.mouseButton == 0)
            {
                if (System.currentTimeMillis() < this.lastClick)
                {
                    this.selectGroup(0, true);
                    this.lastClick -= 500;
                }
                else
                {
                    if (!shift)
                    {
                        this.deselect();

                        this.dragging = 1;
                    }
                    else if (!this.isSelected())
                    {
                        this.startSelecting();
                    }

                    this.moveCursorTo(this.cursor, context.mouseX, context.mouseY);
                    this.lastClick = System.currentTimeMillis() + 200;
                }
            }
            else if (context.mouseButton == 2)
            {
                this.dragging = 3;
            }

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

        if (context.keyCode == Keyboard.KEY_ESCAPE)
        {
            context.unfocus();
            return true;
        }

        boolean ctrl = GuiScreen.isCtrlKeyDown();
        boolean shift = GuiScreen.isShiftKeyDown();

        if (this.handleKeys(context, ctrl, shift))
        {
            this.moveViewportToCursor();
        }

        this.update = context.tick + 20;
        this.horizontal.clamp();
        this.vertical.clamp();

        return false;
    }

    /**
     * Handle multiline text editor keybinds
     */
    protected boolean handleKeys(GuiContext context, boolean ctrl, boolean shift)
    {
        if (ctrl && context.keyCode == Keyboard.KEY_A)
        {
            this.selectAll();
        }
        else if (ctrl && (context.keyCode == Keyboard.KEY_C || context.keyCode == Keyboard.KEY_X) && this.isSelected())
        {
            GuiScreen.setClipboardString(this.getSelectedText());

            if (context.keyCode == Keyboard.KEY_X)
            {
                this.deleteSelection();
                this.deselect();
            }

            return context.keyCode == Keyboard.KEY_X;
        }
        else if (ctrl && context.keyCode == Keyboard.KEY_V)
        {
            this.deleteSelection();
            this.deselect();
            this.writeString(GuiScreen.getClipboardString());

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_UP || context.keyCode == Keyboard.KEY_DOWN || context.keyCode == Keyboard.KEY_RIGHT || context.keyCode == Keyboard.KEY_LEFT)
        {
            int x = context.keyCode == Keyboard.KEY_RIGHT ? 1 : (context.keyCode == Keyboard.KEY_LEFT ? -1 : 0);
            int y = context.keyCode == Keyboard.KEY_UP ? -1 : (context.keyCode == Keyboard.KEY_DOWN ? 1 : 0);

            if (x != 0 && ctrl)
            {
                if (!this.selectGroup(x, shift))
                {
                    this.checkSelection(shift);
                    this.moveCursor(x, 0);
                }
            }
            else
            {
                this.checkSelection(shift);
                this.moveCursor(x, y);
            }

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_HOME)
        {
            this.checkSelection(shift);
            this.moveCursorToLineStart();

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_END)
        {
            this.checkSelection(shift);
            this.moveCursorToLineEnd();

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_RETURN)
        {
            this.keyNewLine();

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_BACK || context.keyCode == Keyboard.KEY_DELETE)
        {
            if (this.isSelected())
            {
                this.deleteSelection();
                this.deselect();
            }
            else
            {
                if (context.keyCode == Keyboard.KEY_DELETE)
                {
                    this.moveCursor(1, 0);
                    this.deleteCharacter();
                }
                else
                {
                    this.keyBackspace();
                }
            }

            return true;
        }
        else if (context.keyCode == Keyboard.KEY_TAB)
        {
            this.keyTab();
        }
        else if (ChatAllowedCharacters.isAllowedCharacter(context.typedChar))
        {
            this.deleteSelection();
            this.deselect();
            this.writeCharacter(String.valueOf(context.typedChar));
            this.moveCursor(1, 0);

            return true;
        }

        return false;
    }

    protected void keyNewLine()
    {
        this.deleteSelection();
        this.deselect();
        this.writeNewLine();
    }

    protected void keyBackspace()
    {
        this.deleteCharacter();
    }

    protected void keyTab()
    {
        this.deleteSelection();
        this.deselect();
        this.writeString("    ");

        for (int i = 0; i < 4; i++)
        {
            this.moveCursor(1, 0, false);
        }
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

        Cursor min = this.getMin();
        Cursor max = this.getMax();

        if (this.isSelected())
        {
            this.drawSelectionBar(x, y, min, max);
        }

        for (String line : this.text)
        {
            int sw = this.font.getStringWidth(line);
            int nx = x - this.horizontal.scroll + this.getShiftX();
            int ny = y - this.vertical.scroll;

            if (ny + this.font.FONT_HEIGHT >= this.area.y && ny < this.area.ey())
            {
                if (this.cursor.line == i && this.focused)
                {
                    int nw = line.isEmpty() ? 0 : this.font.getStringWidth(this.cursor.start(line));
                    int a = (int) (Math.sin((context.tick + context.partialTicks) / 2D) * 127.5 + 127.5) << 24;

                    Gui.drawRect(nx + nw, ny - 1, nx + nw + 1, ny + this.font.FONT_HEIGHT + 1, a + 0xffffff);
                }

                this.drawTextLine(line, i, nx, ny, sw);
            }

            w = Math.max(sw, w);
            y += this.lineHeight;
            i += 1;
        }

        this.horizontal.scrollSize = this.getHorizontalSize(w);
        this.vertical.scrollSize = y - this.area.y + this.padding - (this.text.isEmpty() ? 0 : this.lineHeight - this.font.FONT_HEIGHT);

        this.drawForeground(context);

        this.horizontal.drawScrollbar();
        this.vertical.drawScrollbar();

        GuiDraw.unscissor(context);
    }

    protected int getShiftX()
    {
        return 0;
    }

    protected int getHorizontalSize(int w)
    {
        return w + this.padding * 2;
    }

    protected void drawTextLine(String line, int i, int nx, int ny, int sw)
    {
        this.font.drawStringWithShadow(line, nx, ny, 0xffffff);
    }

    protected void drawBackground()
    {
        this.area.draw(0xffa0a0a0);
        this.area.draw(0xff000000, 1);
    }

    protected void drawForeground(GuiContext context)
    {}

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
            this.moveViewportToCursor();
        }

        if (this.dragging == 3)
        {
            this.horizontal.scroll += this.lastMX - context.mouseX;
            this.horizontal.clamp();

            this.vertical.scroll += this.lastMY - context.mouseY;
            this.vertical.clamp();

            this.lastMX = context.mouseX;
            this.lastMY = context.mouseY;
        }

        this.horizontal.drag(context);
        this.vertical.drag(context);
    }

    /**
     * Draw background text selection
     */
    private void drawSelectionBar(int x, int y, Cursor min, Cursor max)
    {
        final int selectionPad = 2;

        String first = this.text.get(min.line);
        String last = this.text.get(max.line);

        int nx = x - this.horizontal.scroll + this.getShiftX();
        int ny = y - this.vertical.scroll + min.line * this.lineHeight;
        int color = 0x88000000 + McLib.primaryColor.get();

        int x1 = nx + this.font.getStringWidth(min.start(first)) - selectionPad / 2;
        int x2 = nx + this.font.getStringWidth(max.start(last)) + selectionPad / 2;

        if (min.line == max.line)
        {
            Gui.drawRect(x1, ny - selectionPad, x2, ny + this.font.FONT_HEIGHT + selectionPad, color);
        }
        else
        {
            int diff = max.line - min.line;

            Gui.drawRect(x1, ny - selectionPad, this.area.ex(), ny + this.lineHeight, color);

            if (diff > 1)
            {
                Gui.drawRect(this.area.x, ny + this.lineHeight, this.area.ex(), ny + diff * this.lineHeight, color);
            }

            Gui.drawRect(this.area.x, ny + diff * this.lineHeight, x2, ny + diff * this.lineHeight + this.font.FONT_HEIGHT + selectionPad, color);
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

    public static enum StringGroup
    {
        SPACE("[\\s]"), ALPHANUMERIC("[\\w\\d]"), OTHER("[^\\w\\d\\s]");


        private Pattern regex;

        public static StringGroup get(String character)
        {
            for (StringGroup group : values())
            {
                if (group.match(character))
                {
                    return group;
                }
            }

            return OTHER;
        }

        StringGroup(String regex)
        {
            this.regex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        }

        public boolean match(String character)
        {
            return this.regex.matcher(character).matches();
        }
    }
}