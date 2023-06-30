package mchorse.mappet.client.gui.scripts.scriptedItem;

import mchorse.mappet.client.gui.scripts.scriptedItem.util.Textbox;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.IFocusedGuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Area;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.function.Consumer;

public class GuiFormattedTextElement extends GuiElement implements IFocusedGuiElement
{
    private static String[] formattingCodes = new String[] {
        "\u00A70", "\u00A71", "\u00A72", "\u00A73", "\u00A74", "\u00A75", "\u00A76", "\u00A77", "\u00A78", "\u00A79", "\u00A7a", "\u00A7b", "\u00A7c", "\u00A7d", "\u00A7e", "\u00A7f", "\u00A7k", "\u00A7l", "\u00A7m", "\u00A7n", "\u00A7o"
    };

    private static int[] colors = new int[] {
        0x000000, 0x0000AA, 0x00AA00, 0x00AAAA, 0xAA0000, 0xAA00AA, 0xFFAA00, 0xAAAAAA, 0x555555, 0x5555FF, 0x55FF55, 0x55FFFF, 0xFF5555, 0xFF55FF, 0xFFFF55, 0xFFFFFF
    };

    public Textbox text;

    private Consumer<String> callback;

    public GuiFormattedTextElement(Minecraft mc, Consumer<String> consumer)
    {
        super(mc);

        this.callback = consumer;

        this.text = new Textbox(consumer);
        this.text.setFont(mc.fontRenderer);
        this.text.setBorder(true);
    }

    @Override
    public void resize()
    {
        super.resize();

        this.text.area.set(this.area.x, this.area.ey() - 20, this.area.w, 20);
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        int cell = this.area.h - 20;

        if (this.area.isInside(context) && context.mouseY < this.area.y + cell)
        {
            int index = (context.mouseX - this.area.x) / cell;

            if (index >= formattingCodes.length || !this.text.isSelected())
            {
                return super.mouseClicked(context);
            }

            String formattingCode = formattingCodes[index];
            String text = this.text.getText();
            int cursor = this.text.getCursor();
            int selection = this.text.getSelection();
            int beginning = Math.min(cursor, selection);
            int end = Math.max(cursor, selection);

            this.text.setText(text.substring(0, beginning) + formattingCode + this.text.getSelectedText() + "\u00A7r" + text.substring(end));
            this.text.moveCursorTo(beginning);
            this.text.setSelection(end + 4);

            if (this.callback != null)
            {
                this.callback.accept(this.text.getText());
            }

            return true;
        }

        if (super.mouseClicked(context))
        {
            return true;
        }

        boolean wasFocused = this.text.isFocused();

        this.text.mouseClicked(context.mouseX, context.mouseY, context.mouseButton);

        if (wasFocused != this.text.isFocused())
        {
            context.focus(wasFocused ? null : this);
        }

        return this.text.area.isInside(context);
    }

    @Override
    public void mouseReleased(GuiContext context)
    {
        this.text.mouseReleased(context.mouseX, context.mouseY, context.mouseButton);

        super.mouseReleased(context);
    }

    @Override
    public boolean keyTyped(GuiContext context)
    {
        if (this.isFocused())
        {
            if (context.keyCode == Keyboard.KEY_TAB)
            {
                context.focus(this, -1, GuiScreen.isShiftKeyDown() ? -1 : 1);

                return true;
            }

            if (context.keyCode == Keyboard.KEY_ESCAPE)
            {
                context.unfocus();

                return true;
            }
        }

        return this.text.keyPressed(context) || this.text.textInput(context.typedChar) || super.keyTyped(context);
    }

    @Override
    public void draw(GuiContext context)
    {
        this.area.draw(0xff111111);

        int x = this.area.x;
        int y = this.area.y;
        int cell = this.area.h - 20;

        for (int i = 0; i < formattingCodes.length; i++)
        {
            Area.SHARED.set(x, y, cell, cell);

            int a = Area.SHARED.isInside(context) ? 0x88000000 : 0xFF000000;
            float aa = Area.SHARED.isInside(context) ? 0.5F : 1F;

            if (i < colors.length)
            {
                Gui.drawRect(x, y, x + cell, y + cell, colors[i] | a);
            }
            else if (i == 16)
            {
                this.drawCenteredString(context.font, formattingCodes[i] + "W", x + cell / 2, y + cell / 2 - context.font.FONT_HEIGHT / 2, ColorUtils.multiplyColor(0xffffff, aa));
            }
            else if (i == 17)
            {
                this.drawCenteredString(context.font, formattingCodes[i] + "B", x + cell / 2, y + cell / 2 - context.font.FONT_HEIGHT / 2, ColorUtils.multiplyColor(0xffffff, aa));
            }
            else if (i == 18)
            {
                this.drawCenteredString(context.font, formattingCodes[i] + "S", x + cell / 2, y + cell / 2 - context.font.FONT_HEIGHT / 2, ColorUtils.multiplyColor(0xffffff, aa));
            }
            else if (i == 19)
            {
                this.drawCenteredString(context.font, formattingCodes[i] + "U", x + cell / 2, y + cell / 2 - context.font.FONT_HEIGHT / 2, ColorUtils.multiplyColor(0xffffff, aa));
            }
            else if (i == 20)
            {
                this.drawCenteredString(context.font, formattingCodes[i] + "I", x + cell / 2, y + cell / 2 - context.font.FONT_HEIGHT / 2, ColorUtils.multiplyColor(0xffffff, aa));
            }

            x += cell;
        }

        this.text.render(context);

        super.draw(context);
    }

    /* Focus implementation */

    @Override
    public boolean isFocused()
    {
        return this.text.isFocused();
    }

    @Override
    public void focus(GuiContext guiContext)
    {
        this.text.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void unfocus(GuiContext guiContext)
    {
        this.text.setFocused(false);
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void selectAll(GuiContext guiContext)
    {
        this.text.moveCursorToEnd();
        this.text.setSelection(0);
    }

    @Override
    public void unselect(GuiContext guiContext)
    {
        this.text.deselect();
    }
}