package mchorse.mappet.client.gui.events;

import mchorse.mappet.api.events.hotkeys.EventHotkey;
import mchorse.mappet.api.events.hotkeys.EventHotkeys;
import mchorse.mappet.client.gui.utils.GuiCheckerElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiKeybindElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Keys;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.function.Consumer;

public class GuiEventHotkeysOverlayPanel extends GuiOverlayPanel
{
    public GuiEventHotkeyList list;

    public GuiScrollElement editor;
    public GuiKeybindElement key;
    public GuiTextElement event;
    public GuiCheckerElement enabled;

    private EventHotkeys hotkeys;
    private EventHotkey hotkey;

    public GuiEventHotkeysOverlayPanel(Minecraft mc, EventHotkeys hotkeys)
    {
        super(mc, IKey.lang("mappet.gui.nodes.event,hotkeys.title"));

        this.hotkeys = hotkeys;

        this.list = new GuiEventHotkeyList(mc, (l) -> this.pickHotkey(l.get(0), false));
        this.list.sorting().setList(hotkeys.hotkeys);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc).action(Icons.ADD, IKey.lang("mappet.gui.nodes.event,hotkeys.context.add"), this::addHotkey);

            if (!this.hotkeys.hotkeys.isEmpty())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.nodes.event,hotkeys.context.remove"), this::removeHotkey, 0xff0022);
            }

            return menu.shadow();
        });

        this.editor = new GuiScrollElement(mc);
        this.key = new GuiKeybindElement(mc, (k) ->
        {
            if (k == Keyboard.KEY_ESCAPE)
            {
                this.hotkey.keycode = 0;
                this.key.setKeybind(0);
            }
            else
            {
                this.hotkey.keycode = k;
            }
        });
        this.event = new GuiTextElement(mc, 1000, (t) -> this.hotkey.event = t);
        this.enabled = new GuiCheckerElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.editor.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.editor.add(Elements.label(IKey.lang("mappet.gui.nodes.event,hotkeys.key")), this.key);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.nodes.event,hotkeys.event")).marginTop(12), this.event);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.nodes.event,hotkeys.enabled")).marginTop(12), this.enabled);

        this.content.add(this.list, this.editor);

        this.pickHotkey(hotkeys.hotkeys.isEmpty() ? null : hotkeys.hotkeys.get(0), true);
    }

    private void addHotkey()
    {
        EventHotkey hotkey = new EventHotkey();

        this.hotkeys.hotkeys.add(hotkey);
        this.pickHotkey(hotkey, true);
        this.list.update();
    }

    private void removeHotkey()
    {
        int index = this.list.getIndex();

        this.hotkeys.hotkeys.remove(index);
        this.pickHotkey(this.hotkeys.hotkeys.isEmpty() ? null : this.hotkeys.hotkeys.get(Math.max(index - 1, 0)), true);
        this.list.update();
    }

    private void pickHotkey(EventHotkey hotkey, boolean select)
    {
        this.hotkey = hotkey;

        this.editor.setVisible(hotkey != null);

        if (hotkey != null)
        {
            this.key.setKeybind(hotkey.keycode);
            this.event.setText(hotkey.event);
            this.enabled.set(hotkey.enabled);

            if (select)
            {
                this.list.setCurrentScroll(hotkey);
            }
        }
    }

    @Override
    public void onClose()
    {
        Dispatcher.sendToServer(new PacketEventHotkeys(this.hotkeys.serializeNBT()));
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.hotkeys.hotkeys.isEmpty())
        {
            GuiMappetUtils.drawRightClickHere(context, this.list.area);
        }
    }

    public static class GuiEventHotkeyList extends GuiListElement<EventHotkey>
    {
        public GuiEventHotkeyList(Minecraft mc, Consumer<List<EventHotkey>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected String elementToString(EventHotkey element)
        {
            return Keys.getKeyName(element.keycode) + " - " + element.event;
        }
    }
}