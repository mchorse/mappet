package mchorse.mappet.client.gui.quests;

import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.client.gui.quests.rewards.GuiItemStackReward;
import mchorse.mappet.client.gui.quests.rewards.GuiReward;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiRewards extends GuiElement
{
    public List<IReward> rewards;

    public GuiRewards(Minecraft mc)
    {
        super(mc);

        this.flex().column(10).vertical().stretch();
    }

    public GuiSimpleContextMenu getAdds()
    {
        return new GuiSimpleContextMenu(Minecraft.getMinecraft())
                .action(Icons.ADD, IKey.lang("mappet.gui.quests.rewards.context.add_item"), () -> this.addReward(new ItemStackReward(), true));
    }

    private void addReward(IReward reward, boolean add)
    {
        GuiReward element = null;

        if (reward instanceof ItemStackReward)
        {
            element = new GuiItemStackReward(this.mc, (ItemStackReward) reward);
        }

        if (element != null)
        {
            this.add(element);

            final GuiReward finalElement = element;

            element.context(() -> new GuiSimpleContextMenu(Minecraft.getMinecraft())
                    .action(Icons.REMOVE, IKey.lang("mappet.gui.quests.rewards.context.remove"), () -> this.removeReward(finalElement), Colors.NEGATIVE));

            if (add)
            {
                this.rewards.add(reward);
                this.getParentContainer().resize();
            }
        }
    }

    private void removeReward(GuiReward element)
    {
        if (this.rewards.remove(element.reward))
        {
            element.removeFromParent();
            this.getParentContainer().resize();
        }
    }

    public void set(List<IReward> rewards)
    {
        this.rewards = rewards;

        this.removeAll();

        for (IReward reward : this.rewards)
        {
            this.addReward(reward, false);
        }

        this.getParentContainer().resize();
    }
}