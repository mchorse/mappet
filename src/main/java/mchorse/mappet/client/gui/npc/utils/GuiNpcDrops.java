package mchorse.mappet.client.gui.npc.utils;

import mchorse.mappet.api.npcs.NpcDrop;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiNpcDrops extends GuiElement
{
    private List<NpcDrop> drops;

    public GuiNpcDrops(Minecraft mc)
    {
        super(mc);
    }

    public void set(List<NpcDrop> drops)
    {
        this.drops = drops;
    }
}