package mchorse.mappet.client.gui;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.quests.objectives.IObjective;
import mchorse.mappet.api.quests.rewards.IReward;
import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.client.gui.factions.GuiFactionCard;
import mchorse.mappet.client.gui.factions.GuiFactions;
import mchorse.mappet.client.gui.utils.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.factions.PacketRequestFactions;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class GuiJournalScreen extends GuiBase
{
    public GuiScrollElement factions;
    public GuiElement quests;

    public GuiLabelListElement<Quest> questList;
    public GuiScrollElement questArea;

    public GuiJournalScreen(Minecraft mc)
    {
        ICharacter character = Character.get(mc.player);
        Quests quests = character.getQuests();

        this.factions = new GuiScrollElement(mc);
        this.quests = new GuiElement(mc);

        this.factions.flex().relative(this.viewport).x(1F, -250).y(22).w(240).h(1F, -22).column(10).vertical().stretch().scroll().padding(10);
        this.quests.flex().relative(this.viewport).xy(10, 22).wTo(this.factions.area).h(1F, -22);

        this.questList = new GuiLabelListElement<Quest>(mc, (l) -> this.pickQuest(l.get(0).value, false));
        this.questArea = new GuiScrollElement(mc);

        for (Map.Entry<String, Quest> entry : quests.quests.entrySet())
        {
            this.questList.add(IKey.str(entry.getValue().title), entry.getValue());
        }

        this.questList.background().sort();

        this.questList.flex().relative(this.quests).w(120).h(1F);
        this.questArea.flex().relative(this.quests).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.root.add(this.factions, this.quests);
        this.quests.add(this.questList, this.questArea);

        this.pickQuest(quests.quests.isEmpty() ? null : quests.quests.values().iterator().next(), true);

        Dispatcher.sendToServer(new PacketRequestFactions());
    }

    public void fillFactions(Map<String, Faction> factions, Map<String, Double> states)
    {
        this.factions.removeAll();

        for (String key : factions.keySet())
        {
            this.factions.add(new GuiFactionCard(Minecraft.getMinecraft(), factions.get(key), states.get(key)));
        }

        this.root.resize();
    }

    private void pickQuest(Quest value, boolean select)
    {
        this.questArea.removeAll();
        this.questArea.setVisible(value != null);

        if (value != null)
        {
            this.fillQuest(this.questArea, value);

            this.root.resize();
            this.root.resize();
        }

        if (!select)
        {
            return;
        }

        this.questList.setCurrentScroll(null);

        for (Label<Quest> label : this.questList.getList())
        {
            if (label.value == value)
            {
                this.questList.setCurrentScroll(label);

                break;
            }
        }
    }

    private void fillQuest(GuiElement element, Quest value)
    {
        Minecraft mc = Minecraft.getMinecraft();

        element.add(Elements.label(IKey.str(value.title)).background().marginBottom(12));
        element.add(new GuiText(mc).text(value.story.replace("\\n", "\n")).color(0xaaaaaa, true).marginBottom(12));
        element.add(Elements.label(IKey.str("Objectives")));

        for (IObjective objective : value.objectives)
        {
            element.add(Elements.label(IKey.str("- " + objective.stringify(mc.player))).color(0xaaaaaa));
        }

        if (!Mappet.questsPreviewRewards.get())
        {
            return;
        }

        element.add(Elements.label(IKey.str("Rewards")).marginTop(12));

        for (IReward reward : value.rewards)
        {
            if (reward instanceof ItemStackReward)
            {
                ItemStackReward stack = (ItemStackReward) reward;
                GuiElement stacks = new GuiElement(mc);

                stacks.flex().h(24).grid(5).resizes(false).width(24);

                for (ItemStack item : stack.stacks)
                {
                    GuiSlotElement slot = new GuiSlotElement(mc, 0, (Consumer<GuiSlotElement>) null);

                    slot.setEnabled(false);
                    slot.stack = item;
                    slot.drawDisabled = false;
                    stacks.add(slot);
                }

                element.add(stacks);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        this.fontRenderer.drawStringWithShadow("Factions", this.factions.area.x + 10, 10, 0xffffff);
        this.fontRenderer.drawStringWithShadow("Quests", this.quests.area.x + 4, 10, 0xffffff);
        GuiDraw.drawVerticalGradientRect(this.questArea.area.x, this.questArea.area.y(0.75F), this.questArea.area.ex(), this.questArea.area.ey(), 0, 0x44000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}