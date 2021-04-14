package mchorse.mappet.client.gui.quests.rewards;

import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.client.gui.utils.GuiItemsElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.function.Supplier;

public class GuiItemStackReward extends GuiReward<ItemStackReward>
{
    public GuiItemsElement items;

    public GuiItemStackReward(Minecraft mc, ItemStackReward reward, Supplier<GuiInventoryElement> inventory)
    {
        super(mc, reward);

        this.items = new GuiItemsElement(mc, IKey.str("Reward items"), reward.stacks, inventory);
        this.items.flex().relative(this).wh(1F, 1F);
        this.flex = this.items.flex();

        this.add(this.items);
    }
}