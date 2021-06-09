package mchorse.mappet.client.gui.npc.utils;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.Consumer;

public class GuiNpcStatesOverlayPanel extends GuiStringOverlayPanel
{
    private Npc npc;

    public GuiNpcStatesOverlayPanel(Minecraft mc, Npc npc, Consumer<String> callback)
    {
        super(mc, IKey.lang("mappet.gui.npcs.overlay.title"), false, npc.states.keySet(), callback);

        this.npc = npc;

        this.strings.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.npcs.context.add"), this::addState);

            if (!this.strings.list.isDeselected())
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.npcs.context.copy"), this::copyState);

                try
                {
                    NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                    if (tag.hasKey("_StateCopy"))
                    {
                        NpcState state = new NpcState();

                        state.deserializeNBT(tag);
                        menu.action(Icons.PASTE, IKey.lang("mappet.gui.npcs.context.paste"), () -> this.pasteState(state));
                    }
                }
                catch (Exception e)
                {}

                menu.action(Icons.EDIT, IKey.lang("mappet.gui.npcs.context.rename"), this::renameState);
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.npcs.context.remove"), this::removeState, Colors.NEGATIVE);
            }

            return menu.shadow();
        });
    }

    /* Context menu modals */

    private void addState()
    {
        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.npcs.modals.add"), (name) -> this.addState(name, null)));
    }

    private void addState(String name, NpcState state)
    {
        if (!this.npc.states.containsKey(name))
        {
            if (state == null)
            {
                state = new NpcState();
            }

            this.npc.states.put(name, state);
            this.strings.list.add(name);
            this.strings.list.sort();

            this.set(name);
            this.accept(name);
        }
    }

    private void copyState()
    {
        String key = this.strings.list.getCurrentFirst();
        NBTTagCompound tag = this.npc.states.get(key).serializeNBT();
        tag.setBoolean("_StateCopy", true);

        GuiScreen.setClipboardString(tag.toString());
    }

    private void pasteState(NpcState state)
    {
        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.npcs.modals.paste"), (name) -> this.addState(name, state)));
    }

    private void renameState()
    {
        GuiModal.addFullModal(this, () ->
        {
            return new GuiPromptModal(this.mc, IKey.lang("mappet.gui.npcs.modals.rename"), this::renameState).setValue(this.strings.list.getCurrentFirst());
        });
    }

    private void renameState(String name)
    {
        String current = this.strings.list.getCurrentFirst();

        if (!this.npc.states.containsKey(name))
        {
            NpcState state = this.npc.states.remove(current);

            this.npc.states.put(name, state);
            this.strings.list.remove(current);
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

    @Override
    protected void drawBackground(GuiContext context)
    {
        super.drawBackground(context);

        if (this.npc.states.size() <= 1)
        {
            GuiMappetUtils.drawRightClickHere(context, this.area);
        }
    }
}