package mchorse.mappet.client.gui;

import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.quests.chains.QuestStatus;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.client.gui.factions.GuiFactionCard;
import mchorse.mappet.client.gui.quests.GuiQuestCard;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.factions.PacketRequestFactions;
import mchorse.mappet.network.common.quests.PacketQuestAction;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.Map;

public class GuiJournalScreen extends GuiBase
{
    public GuiScrollElement factions;
    public GuiElement quests;

    public GuiLabelListElement<Quest> questList;
    public GuiScrollElement questArea;
    public GuiIconElement cancel;

    public GuiJournalScreen(Minecraft mc)
    {
        ICharacter character = Character.get(mc.player);
        Quests quests = character.getQuests();

        this.factions = new GuiScrollElement(mc);
        this.quests = new GuiElement(mc);

        this.factions.flex().relative(this.viewport).x(1F, -250).y(22).w(240).h(1F, -22).column(10).vertical().stretch().scroll().padding(10);
        this.quests.flex().relative(this.viewport).xy(10, 22).wTo(this.factions.area).h(1F, -32);

        this.questList = new GuiLabelListElement<Quest>(mc, (l) -> this.pickQuest(l.get(0).value, false));
        this.questArea = new GuiScrollElement(mc);

        for (Map.Entry<String, Quest> entry : quests.quests.entrySet())
        {
            this.questList.add(IKey.str(entry.getValue().getProcessedTitle()), entry.getValue());
        }

        this.questList.background().sort();

        this.cancel = new GuiIconElement(mc, Icons.CLOSE, (b) -> this.cancelQuest());
        this.cancel.disabledColor(0x88ffffff);
        this.cancel.flex().relative(this.quests).x(1F, -14).y(14).anchor(0.5F, 0.5F);

        this.questList.flex().relative(this.quests).w(120).h(1F);
        this.questArea.flex().relative(this.quests).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.root.add(this.factions, this.quests);
        this.quests.add(this.questList, this.questArea, this.cancel);

        this.pickQuest(quests.quests.isEmpty() ? null : quests.quests.values().iterator().next(), true);

        Dispatcher.sendToServer(new PacketRequestFactions());
    }

    private void cancelQuest()
    {
        Label<Quest> label = this.questList.getCurrentFirst();

        if (label != null)
        {
            Dispatcher.sendToServer(new PacketQuestAction(label.value.getId(), QuestStatus.CANCELED));

            int index = this.questList.getIndex();

            this.questList.remove(label);
            this.questList.setIndex(Math.max(index - 1, 0));

            label = this.questList.getCurrentFirst();

            this.pickQuest(label == null ? null : label.value, true);
        }
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
        this.cancel.setVisible(value != null);

        if (value != null)
        {
            GuiQuestCard.fillQuest(this.questArea, value, false, true);

            this.cancel.setEnabled(value.cancelable);

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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.panels.factions"), this.factions.area.x + 10, 10, 0xffffff);
        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.panels.quests"), this.quests.area.x + 4, 10, 0xffffff);
        GuiDraw.drawVerticalGradientRect(this.questArea.area.x, this.questArea.area.y(0.75F), this.questArea.area.ex(), this.questArea.area.ey(), 0, 0x44000000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}