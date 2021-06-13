package mchorse.mappet.client.gui;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.npc.GuiNpcEditor;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcState;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import net.minecraft.client.Minecraft;

public class GuiNpcStateScreen extends GuiBase
{
    public GuiNpcEditor editor;
    public GuiInventoryElement inventory;

    private int entityId;

    public GuiNpcStateScreen(Minecraft mc, int entityId, NpcState state)
    {
        this.entityId = entityId;

        this.inventory = new GuiInventoryElement(mc, (stack) ->
        {
            this.inventory.linked.acceptStack(stack);
            this.inventory.unlink();
        });
        this.inventory.flex().relative(this.root).xy(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.inventory.setVisible(false);

        this.editor = new GuiNpcEditor(mc, true, () -> this.inventory);
        this.editor.flex().relative(this.root).w(1F).h(1F);

        this.root.add(this.editor, this.inventory);

        this.editor.set(state);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketNpcState(this.entityId, this.editor.get().serializeNBT()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}