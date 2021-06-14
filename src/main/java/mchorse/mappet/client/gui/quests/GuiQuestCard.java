package mchorse.mappet.client.gui.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class GuiQuestCard
{
    public static void fillQuest(GuiElement element, Quest quest, boolean forceReward)
    {
        Minecraft mc = Minecraft.getMinecraft();
        String title = quest.getProcessedTitle();

        if (mc.player.isCreative())
        {
            title += TextFormatting.GRAY + " (" + quest.getId() + ")";
        }

        element.add(Elements.label(IKey.str(title)).background().marginBottom(12));
        element.add(new GuiText(mc).text(DialogueFragment.process(quest.story)).color(0xaaaaaa, true).marginBottom(12));
        element.add(Elements.label(IKey.lang("mappet.gui.quests.objectives.title")));

        for (AbstractObjective objective : quest.objectives)
        {
            element.add(Elements.label(IKey.str("- " + objective.stringify(mc.player))).color(0xaaaaaa));
        }

        if (!Mappet.questsPreviewRewards.get() && !forceReward)
        {
            return;
        }

        element.add(Elements.label(IKey.lang("mappet.gui.quests.rewards.title")).marginTop(12));

        for (IReward reward : quest.rewards)
        {
            if (reward instanceof ItemStackReward)
            {
                ItemStackReward stack = (ItemStackReward) reward;
                GuiElement stacks = new GuiElement(mc);

                stacks.flex().h(24).grid(5).resizes(false).width(24);

                for (ItemStack item : stack.stacks)
                {
                    GuiSlotElement slot = new GuiSlotElement(mc, 0, null);

                    slot.setEnabled(false);
                    slot.setStack(item);
                    slot.drawDisabled = false;
                    stacks.add(slot);
                }

                element.add(stacks);
            }
        }
    }
}