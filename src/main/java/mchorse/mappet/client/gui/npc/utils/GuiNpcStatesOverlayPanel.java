package mchorse.mappet.client.gui.npc.utils;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiNpcStatesOverlayPanel extends GuiStringOverlayPanel
{
    public GuiIconElement add;
    public GuiIconElement remove;

    private Npc npc;

    public GuiNpcStatesOverlayPanel(Minecraft mc, Npc npc, Consumer<String> callback)
    {
        super(mc, IKey.lang("mappet.gui.npcs.overlay.title"), false, npc.states.keySet(), callback);

        this.npc = npc;

        this.add = new GuiIconElement(mc, Icons.ADD, (b) -> this.addState());
        this.add.tooltip(IKey.lang("mappet.gui.npcs.context.add")).flex().wh(16, 16);

        this.remove = new GuiIconElement(mc, Icons.REMOVE, (b) -> this.removeState());
        this.remove.tooltip(IKey.lang("mappet.gui.npcs.context.remove")).flex().wh(16, 16);

        this.icons.add(this.remove.marginRight(4), this.add);
    }

    /* Context menu modals */

    private void addState()
    {
        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.npcs.modals.add"), this::addState));
    }

    private void addState(String name)
    {
        if (!this.npc.states.containsKey(name))
        {
            NpcState state = new NpcState();

            this.npc.states.put(name, state);
            this.strings.list.add(name);
            this.strings.list.sort();

            this.set(name);
            this.accept(name);
        }
    }

    private void removeState()
    {
        GuiModal.addFullModal(this, () -> new GuiConfirmModal(this.mc, IKey.lang("mappet.gui.npcs.modals.remove"), this::removeState));
    }

    private void removeState(boolean confirm)
    {
        if (confirm)
        {
            int index = this.strings.list.getIndex();
            String key = this.strings.list.getCurrentFirst();

            this.strings.list.remove(key);
            this.npc.states.remove(key);
            this.strings.list.setIndex(Math.max(index - 1, 0));

            String name = this.getValue();

            this.set(name);
            this.accept(name);
        }
    }
}