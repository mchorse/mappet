package mchorse.mappet.client.gui.quests.rewards;

import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;

public class GuiReward <T extends IReward> extends GuiElement
{
    public T reward;

    public GuiReward(Minecraft mc, T reward)
    {
        super(mc);

        this.reward = reward;
    }
}