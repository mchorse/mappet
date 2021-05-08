package mchorse.mappet.api.quests.chains;

import net.minecraft.util.text.TextFormatting;

public enum QuestStatus
{
    AVAILABLE(TextFormatting.WHITE), UNAVAILABLE(TextFormatting.GRAY), COMPLETED(TextFormatting.GOLD), CANCELED(TextFormatting.STRIKETHROUGH);

    public final TextFormatting formatting;

    private QuestStatus(TextFormatting formatting)
    {
        this.formatting = formatting;
    }
}