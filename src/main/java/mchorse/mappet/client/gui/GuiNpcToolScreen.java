package mchorse.mappet.client.gui;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcList;
import mchorse.mappet.network.common.npc.PacketNpcTool;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class GuiNpcToolScreen extends GuiBase
{
    public GuiStringListElement npcs;
    public GuiStringListElement states;

    public GuiNpcToolScreen(Minecraft mc, List<String> npcs, List<String> states)
    {
        this.npcs = new GuiStringListElement(mc, (l) -> this.queryStates(l.get(0)));
        this.npcs.background().setList(npcs);
        this.npcs.sort();
        this.states = new GuiStringListElement(mc, null);
        this.states.background().setList(states);
        this.states.sort();

        this.npcs.flex().relative(this.viewport).x(0.5F, -10).y(0.5F).w(100).h(200).anchor(1F, 0.5F);
        this.states.flex().relative(this.viewport).x(0.5F, 10).y(0.5F).w(100).h(200).anchor(0F, 0.5F);

        this.root.add(this.npcs, this.states);

        /* Setting up */
        ItemStack stack = mc.player.getHeldItemMainhand();

        if (stack.getItem() != Mappet.npcTool)
        {
            stack = mc.player.getHeldItemOffhand();
        }

        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null && tag.hasKey("Npc") && tag.hasKey("State"))
        {
            this.npcs.setCurrentScroll(tag.getString("Npc"));
            this.states.setCurrentScroll(tag.getString("State"));
        }
    }

    private void queryStates(String s)
    {
        PacketNpcList packet = new PacketNpcList();

        packet.npcs.add(s);
        Dispatcher.sendToServer(packet);
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

        String npc = this.npcs.isDeselected() ? "" : this.npcs.getCurrentFirst();
        String state = this.states.isDeselected() ? "" : this.states.getCurrentFirst();

        Dispatcher.sendToServer(new PacketNpcTool(npc, state));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        this.fontRenderer.drawStringWithShadow("NPCs", this.npcs.area.x, this.npcs.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow("State", this.states.area.x, this.states.area.y - 12, 0xffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}