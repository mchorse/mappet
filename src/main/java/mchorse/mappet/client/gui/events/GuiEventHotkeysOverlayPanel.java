package mchorse.mappet.client.gui.events;

import mchorse.mappet.api.misc.hotkeys.TriggerHotkey;
import mchorse.mappet.api.misc.hotkeys.TriggerHotkeys;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiKeybindElement;
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
    public GuiTriggerElement trigger;
    public GuiCheckerElement enabled;

    private TriggerHotkeys hotkeys;
    private TriggerHotkey hotkey;

    public GuiEventHotkeysOverlayPanel(Minecraft mc, TriggerHotkeys hotkeys)
    {
        super(mc, IKey.lang("mappet.gui.nodes.event.hotkeys.title"));

        this.hotkeys = hotkeys;

        this.list = new GuiEventHotkeyList(mc, (l) -> this.pickHotkey(l.get(0), false));
        this.list.sorting().setList(hotkeys.hotkeys);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc).action(Icons.ADD, IKey.lang("mappet.gui.nodes.event.hotkeys.context.add"), this::addHotkey);

            if (!this.hotkeys.hotkeys.isEmpty())
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.nodes.event.hotkeys.context.remove"), this::removeHotkey, Colors.NEGATIVE);
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
        this.trigger = new GuiTriggerElement(mc);
        this.enabled = new GuiCheckerElement(mc);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.editor.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.editor.add(Elements.label(IKey.lang("mappet.gui.nodes.event.hotkeys.key")), this.key);
        this.editor.add(this.trigger.marginTop(12));
        this.editor.add(Elements.label(IKey.lang("mappet.gui.nodes.event.hotkeys.enabled")).marginTop(12), this.enabled);

        this.content.add(this.editor, this.list);

        this.pickHotkey(hotkeys.hotkeys.isEmpty() ? null : hotkeys.hotkeys.get(0), true);
    }

    private void addHotkey()
    {
        TriggerHotkey hotkey = new TriggerHotkey();

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

    private void pickHotkey(TriggerHotkey hotkey, boolean select)
    {
        this.hotkey = hotkey;

        this.editor.setVisible(hotkey != null);

        if (hotkey != null)
        {
            this.key.setKeybind(hotkey.keycode);
            this.trigger.set(hotkey.trigger);
            this.enabled.set(hotkey.enabled);

            if (select)
            {
                this.list.setCurrentScroll(hotkey);
            }
        }
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

    public static class GuiEventHotkeyList extends GuiListElement<TriggerHotkey>
    {
        public GuiEventHotkeyList(Minecraft mc, Consumer<List<TriggerHotkey>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected String elementToString(TriggerHotkey element)
        {
            return Keys.getKeyName(element.keycode);
        }
    }
}