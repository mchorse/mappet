package mchorse.mappet.client.gui.quests;

import mchorse.mappet.api.quests.chains.QuestInfo;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class GuiQuestInfoListElement extends GuiListElement<QuestInfo>
{
    public GuiQuestInfoListElement(Minecraft mc, Consumer<List<QuestInfo>> callback)
    {
        super(mc, callback);

        this.scroll.scrollItemSize = 16;
    }

    @Override
    protected boolean sortElements()
    {
        this.list.sort(Comparator.comparing(a -> a.quest.title));

        return true;
    }

    @Override
    protected String elementToString(QuestInfo element)
    {
        String string = element.status.formatting + element.quest.getProcessedTitle();

        if (this.mc.player.isCreative())
        {
            string += TextFormatting.GRAY + " (" + element.quest.getId() + ")";
        }

        return string;
    }
}