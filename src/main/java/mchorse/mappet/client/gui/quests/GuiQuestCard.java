package mchorse.mappet.client.gui.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.quests.PacketQuestVisibility;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class GuiQuestCard
{
    public static void fillQuest(GuiElement element, Quest quest, boolean forceReward, boolean displayVisibility)
    {
        Minecraft mc = Minecraft.getMinecraft();
        Quests quests = Character.get(mc.player).getQuests();
        Quest playerQuest = quests.getByName(quest.getId());
        Quest characterQuest = playerQuest == null ? quest : playerQuest;
        String title = characterQuest.getProcessedTitle();

        if (mc.player.isCreative())
        {
            title += TextFormatting.GRAY + " (" + characterQuest.getId() + ")";
        }

        element.add(Elements.label(IKey.str(title)).background().marginBottom(12));

        if (displayVisibility)
        {
            element.add(new GuiToggleElement(mc, IKey.lang("mappet.gui.quests.visible"), characterQuest.visible, (b) ->
            {
                Dispatcher.sendToServer(new PacketQuestVisibility(characterQuest.getId(), characterQuest, b.isToggled()));
                characterQuest.visible = b.isToggled();
            }));
        }

        element.add(new GuiText(mc).text(DialogueFragment.process(characterQuest.story)).color(0xaaaaaa, true).marginBottom(12));
        element.add(Elements.label(IKey.lang("mappet.gui.quests.objectives.title")));

        StringBuilder objectives = new StringBuilder();

        for (AbstractObjective objective : characterQuest.objectives)
        {
            objectives.append(IKey.str("- " + objective.stringify(mc.player)).toString() + "\n");
        }

        element.add(new GuiText(mc).text(objectives.toString()).color(0xaaaaaa, true));

        if (!Mappet.questsPreviewRewards.get() && !forceReward)
        {
            return;
        }

        if (characterQuest.rewards.isEmpty())
        {
            return;
        }

        element.add(Elements.label(IKey.lang("mappet.gui.quests.rewards.title")).marginTop(12));

        for (IReward reward : characterQuest.rewards)
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