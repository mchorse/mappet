package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiPatrolPointElement extends GuiElement
{
    GuiBlockPosElement position;

    GuiTriggerElement trigger;

    public GuiPatrolPointElement(Minecraft mc)
    {
        super(mc);

        this.position = new GuiBlockPosElement(mc, null);
        this.trigger = new GuiTriggerElement(mc);

        this.flex().column(5).stretch().vertical();
        this.add(this.position, this.trigger);
    }

    public void set(BlockPos pos, Trigger trigger)
    {
        this.position.set(pos);
        this.trigger.set(trigger);
    }
}
