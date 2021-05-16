package mchorse.mappet.client.gui;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

import java.util.List;
import java.util.Map;

public class GuiQuestTracker extends Gui
{
    public static void renderQuests(ScaledResolution size, float partial)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ICharacter character = Character.get(mc.player);

        if (character != null && !character.getQuests().quests.isEmpty())
        {
            Quests quests = character.getQuests();

            int i = 0;
            int c = Math.min(quests.quests.size(), 3);
            int w = 160;
            int x = size.getScaledWidth() - w;
            int y = 60;

            for (Map.Entry<String, Quest> entry : quests.quests.entrySet())
            {
                if (i >= c)
                {
                    break;
                }

                y += renderQuest(mc, entry.getValue(), x, y, w);
                i += 1;
            }
        }
    }

    private static int renderQuest(Minecraft mc, Quest value, int x, int y, int w)
    {
        /* TODO: optimize */
        boolean questComplete = value.isComplete(mc.player);
        String title = value.title;

        if (questComplete)
        {
            title = TextFormatting.GOLD + title;
        }

        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GuiDraw.drawHorizontalGradientRect(x, y, x + w, y + 16, 0x11000000, 0xaa000000);
        mc.fontRenderer.drawStringWithShadow(title, x + 4, y + 4, 0xffffff);

        int original = y;

        y += 16;

        for (AbstractObjective objective : value.objectives)
        {
            String description = "- " + objective.stringify(mc.player);
            List<String> lines = mc.fontRenderer.listFormattedStringToWidth(description, w - 6);
            boolean complete = questComplete || objective.isComplete(mc.player);

            for (String line : lines)
            {
                mc.fontRenderer.drawStringWithShadow(line, x + 4, y + 2, complete ? 0xffffff : 0xaaaaaa);

                y += 12;
            }
        }

        return (y - original) + 6;
    }
}