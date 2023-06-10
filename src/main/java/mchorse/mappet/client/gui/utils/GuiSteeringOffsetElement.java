package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiSteeringOffsetElement extends GuiElement
{
    GuiBlockPosElement position;

    public GuiSteeringOffsetElement(Minecraft mc)
    {
        super(mc);

        this.position = new GuiBlockPosElement(mc, null);

        this.flex().column(5).stretch().vertical();
        this.add(this.position);
    }

    public void set(BlockPos pos)
    {
        this.position.set(pos);
    }
}
