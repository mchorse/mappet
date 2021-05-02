package mchorse.mappet.client;

import mchorse.mappet.client.gui.GuiInteractionScreen;
import mchorse.mappet.client.gui.GuiJournalScreen;
import mchorse.mappet.client.gui.GuiQuestTracker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderingHandler
{
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            Minecraft mc = Minecraft.getMinecraft();

            if (!(mc.currentScreen instanceof GuiJournalScreen) && !(mc.currentScreen instanceof GuiInteractionScreen))
            {
                GuiQuestTracker.renderQuests(event.getResolution(), event.getPartialTicks());
            }
        }
    }
}