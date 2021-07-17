package mchorse.mappet.client.gui.scripts;

import mchorse.mappet.client.gui.panels.GuiScriptPanel;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.client.gui.utils.text.utils.Cursor;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketRepl;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class GuiRepl extends GuiElement
{
    public GuiTextEditor repl;
    public GuiScrollElement log;

    private List<String> history = new ArrayList<String>();
    private int index = 0;

    public GuiRepl(Minecraft mc)
    {
        super(mc);

        this.repl = new GuiTextEditor(mc, null);
        this.repl.background().flex().relative(this).y(1F).w(1F).h(100).anchorY(1F);
        this.repl.context(() ->
        {
            return new GuiSimpleContextMenu(this.mc)
                .action(Icons.POSE, IKey.lang("mappet.gui.scripts.context.paste_morph"), () -> GuiScriptPanel.openMorphPicker(this.repl))
                .action(MMIcons.ITEM, IKey.lang("mappet.gui.scripts.context.paste_item"), () -> GuiScriptPanel.openItemPicker(this.repl))
                .action(Icons.BLOCK, IKey.lang("mappet.gui.scripts.context.paste_player_pos"), () -> GuiScriptPanel.pastePlayerPosition(this.repl))
                .action(Icons.VISIBLE, IKey.lang("mappet.gui.scripts.context.paste_block_pos"), () -> GuiScriptPanel.pasteBlockPosition(this.repl));
        });

        this.log = new GuiScrollElement(mc);
        this.log.flex().relative(this).w(1F).h(1F, -100).column(0).vertical().stretch().scroll();

        this.add(this.repl, this.log);

        this.repl.setText("\"" + I18n.format("mappet.gui.scripts.repl.hello_world") + "\"");
        this.log(I18n.format("mappet.gui.scripts.repl.welcome"));
    }

    @Override
    public boolean keyTyped(GuiContext context)
    {
        if (context.keyCode == Keyboard.KEY_RETURN && !GuiScreen.isShiftKeyDown())
        {
            String text = this.repl.getText();

            if (!text.isEmpty())
            {
                Dispatcher.sendToServer(new PacketRepl(text));

                this.repl.clear();
                this.history.add(text);
                this.index = this.history.size();
            }

            return true;
        }
        else if (this.repl.isFocused() && !this.repl.isSelected() && !this.history.isEmpty() && GuiScreen.isCtrlKeyDown())
        {
            Cursor cursor = this.repl.cursor;

            /* Handle history cycling using up and down arrow keys */
            if (context.keyCode == Keyboard.KEY_UP && this.index > 0)
            {
                this.index -= 1;
                this.repl.setText(this.history.get(this.index));

                int lastLine = this.repl.getLines().size() - 1;

                cursor.set(lastLine, this.repl.getLines().get(lastLine).length());
                this.repl.moveViewportToCursor();

                return true;
            }
            else if (context.keyCode == Keyboard.KEY_DOWN && this.index < this.history.size() - 1)
            {
                this.index += 1;
                this.repl.setText(this.history.get(this.index));

                int lastLine = this.repl.getLines().size() - 1;

                cursor.set(lastLine, this.repl.getLines().get(lastLine).length());
                this.repl.moveViewportToCursor();

                return true;
            }
        }

        return super.keyTyped(context);
    }

    public void log(String code)
    {
        code = code.trim();

        if (code.isEmpty())
        {
            return;
        }

        int size = this.log.getChildren().size();
        boolean odd = (size + 1) % 2 == 1;

        this.log.add(new GuiReplText(this.mc, odd, size == 0 ? 10 : 5).text(code));
        this.resize();

        this.log.scroll.scrollTo(this.log.scroll.scrollSize);
    }

    public static class GuiReplText extends GuiText
    {
        private boolean odd;

        public GuiReplText(Minecraft mc, boolean odd, int vertical)
        {
            super(mc);

            this.odd = odd;

            this.padding(10, vertical);
        }

        @Override
        public void draw(GuiContext context)
        {
            if (this.odd)
            {
                this.area.draw(ColorUtils.HALF_BLACK);
            }

            super.draw(context);
        }
    }
}