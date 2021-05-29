package mchorse.mappet.client.gui.utils;

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
import java.util.function.Consumer;

public class GuiMultiTextElement extends GuiElement implements IFocusedGuiElement
{
    public static final int PADDING = 10;
    public static final int LINE_HEIGHT = 12;

    public ScrollArea horizontal = new ScrollArea();
    public ScrollArea vertical = new ScrollArea();

    public Consumer<String> callback;

    /* Editing */
    private boolean focused;
    private List<String> text = new ArrayList<String>();
    private int cursorLine = 0;
    private int cursorOffset = 0;

    public GuiMultiTextElement(Minecraft mc, Consumer<String> callback)
    {
        super(mc);

        this.callback = callback;

        this.horizontal.direction = ScrollDirection.HORIZONTAL;
        this.horizontal.cancelScrollEdge = true;
        this.horizontal.scrollSpeed = LINE_HEIGHT * 2;
        this.vertical.cancelScrollEdge = true;
        this.vertical.scrollSpeed = LINE_HEIGHT * 2;
    }

    public void setText(String text)
    {
        this.text.clear();
        this.text.addAll(Arrays.asList(text.split("\n")));

        this.cursorLine = 0;
        this.cursorOffset = 0;
    }

    public String getText()
    {
        return String.join("\n", this.text);
    }

    /* Writing API */

    public void writeNewLine()
    {
        if (!this.hasLine(this.cursorLine))
        {
            return;
        }

        String line = this.text.get(this.cursorLine);

        if (this.cursorOffset == 0 || line.isEmpty())
        {
            this.text.add(this.cursorLine, "");
        }
        else if (this.cursorOffset >= line.length())
        {
            this.text.add(this.cursorLine + 1, "");
        }
        else
        {
            this.text.set(this.cursorLine, line.substring(0, this.cursorOffset));
            this.text.add(this.cursorLine + 1, line.substring(this.cursorOffset));
            this.moveCursorToLineStart();
        }

        this.cursorLine += 1;
        this.cursorOffset = 0;
    }

    public void writeCharacter(char character)
    {
        if (this.hasLine(this.cursorLine))
        {
            String line = this.text.get(this.cursorLine);
            int index = this.cursorOffset;

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
                line = line.substring(0, this.cursorOffset) + character + line.substring(this.cursorOffset);
            }

            this.text.set(this.cursorLine, line);
        }
    }

    public void deleteCharacter()
    {
        if (this.hasLine(this.cursorLine))
        {
            String line = this.text.get(this.cursorLine);
            int index = Math.min(this.cursorOffset, line.length());

            if (line.isEmpty())
            {
                if (this.cursorLine > 0)
                {
                    this.text.remove(this.cursorLine);

                    this.cursorLine -= 1;

                    this.moveCursorToLineEnd();
                }
            }
            else if (index >= line.length())
            {
                line = line.substring(0, line.length() - 1);
                this.text.set(this.cursorLine, line);
                this.moveCursorToLineEnd();
            }
            else if (index == 0)
            {
                if (this.cursorLine > 0)
                {
                    String text = this.text.remove(this.cursorLine);

                    this.cursorLine -= 1;

                    this.moveCursorToLineEnd();
                    this.text.set(this.cursorLine, this.text.get(this.cursorLine) + text);
                }
            }
            else
            {
                line = line.substring(0, this.cursorOffset - 1) + line.substring(this.cursorOffset);
                this.text.set(this.cursorLine, line);
                this.moveCursor(-1, 0);
            }
        }
    }

    public boolean hasLine(int line)
    {
        return line >= 0 && line < this.text.size();
    }

    /* Moving cursor API */

    public void moveCursor(int x, int y)
    {
        if (!this.hasLine(this.cursorLine))
        {
            return;
        }

        String line = this.text.get(this.cursorLine);

        if (x != 0)
        {
            int nx = this.cursorOffset + (x > 0 ? 1 : -1);

            if (nx < 0)
            {
                if (this.hasLine(this.cursorLine - 1))
                {
                    this.cursorLine -= 1;
                    this.moveCursorToLineEnd();
                }
            }
            else if (nx > line.length())
            {
                if (this.hasLine(this.cursorLine + 1))
                {
                    this.cursorLine += 1;
                    this.moveCursorToLineStart();
                }
            }
            else
            {
                this.cursorOffset = nx;
            }
        }

        if (y != 0)
        {
            int ny = this.cursorLine + (y > 0 ? 1 : -1);

            if (ny >= 0 && ny < this.text.size())
            {
                this.cursorLine = ny;
            }
        }
    }

    public void moveCursorToLineStart()
    {
        this.cursorOffset = 0;
    }

    public void moveCursorToLineEnd()
    {
        if (this.hasLine(this.cursorLine))
        {
            this.cursorOffset = this.text.get(this.cursorLine).length();
        }
    }

    public void moveCursorTo(int x, int y)
    {
        x -= this.area.x + PADDING;
        y -= this.area.y + PADDING;

        x += this.horizontal.scroll;
        y += this.vertical.scroll;

        this.cursorLine = MathUtils.clamp(y / LINE_HEIGHT, 0, this.text.size() - 1);

        String line = this.text.get(this.cursorLine);
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
            this.cursorOffset = 0;
            w = this.font.getStringWidth(line.substring(0, this.cursorOffset + 1));

            while (x > w)
            {
                w = this.font.getStringWidth(line.substring(0, this.cursorOffset + 1));

                this.cursorOffset += 1;
            }

            if (this.cursorOffset > 0)
            {
                this.cursorOffset -= 1;
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

        this.focused = this.area.isInside(context);

        if (this.focused)
        {
            this.moveCursorTo(context.mouseX, context.mouseY);
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

        if (context.keyCode == Keyboard.KEY_UP || context.keyCode == Keyboard.KEY_DOWN || context.keyCode == Keyboard.KEY_RIGHT || context.keyCode == Keyboard.KEY_LEFT)
        {
            int x = context.keyCode == Keyboard.KEY_RIGHT ? 1 : (context.keyCode == Keyboard.KEY_LEFT ? -1 : 0);
            int y = context.keyCode == Keyboard.KEY_UP ? -1 : (context.keyCode == Keyboard.KEY_DOWN ? 1 : 0);

            this.moveCursor(x, y);
        }
        else if (context.keyCode == Keyboard.KEY_HOME)
        {
            this.moveCursorToLineStart();
        }
        else if (context.keyCode == Keyboard.KEY_END)
        {
            this.moveCursorToLineEnd();
        }
        else if (context.keyCode == Keyboard.KEY_RETURN)
        {
            this.writeNewLine();
        }
        else if (context.keyCode == Keyboard.KEY_BACK)
        {
            this.deleteCharacter();
        }
        else if (context.keyCode == Keyboard.KEY_DELETE)
        {
            this.moveCursor(1, 0);
            this.deleteCharacter();
        }
        else if (ChatAllowedCharacters.isAllowedCharacter(context.typedChar))
        {
            this.writeCharacter(context.typedChar);
            this.moveCursor(1, 0);
        }

        this.horizontal.clamp();
        this.vertical.clamp();

        return false;
    }

    @Override
    public void draw(GuiContext context)
    {
        this.horizontal.drag(context);
        this.vertical.drag(context);

        super.draw(context);

        GuiDraw.scissor(this.area.x, this.area.y, this.area.w, this.area.h, context);

        int x = this.area.x + PADDING;
        int y = this.area.y + PADDING;
        int w = 0;
        int i = 0;

        for (String line : this.text)
        {
            w = Math.max(this.font.getStringWidth(line), w);

            int nx = x - this.horizontal.scroll;
            int ny = y - this.vertical.scroll;

            if (ny + this.font.FONT_HEIGHT >= this.area.y && ny < this.area.ey())
            {
                this.font.drawStringWithShadow(line, nx, ny, 0xffffff);

                if (this.cursorLine == i && this.focused)
                {
                    int index = Math.min(this.cursorOffset, line.length());
                    int nw = line.isEmpty() ? 0 : this.font.getStringWidth(line.substring(0, index));

                    Gui.drawRect(nx + nw, ny, nx + nw + 1, ny + this.font.FONT_HEIGHT, 0xffffffff);
                }
            }

            y += LINE_HEIGHT;
            i += 1;
        }

        this.horizontal.scrollSize = w + PADDING * 2;
        this.vertical.scrollSize = y - this.area.y + PADDING - (this.text.isEmpty() ? 0 : LINE_HEIGHT - this.font.FONT_HEIGHT);

        this.horizontal.drawScrollbar();
        this.vertical.drawScrollbar();

        GuiDraw.unscissor(context);
    }
}