package mchorse.mappet.client;

import mchorse.mappet.client.gui.GuiQuestTracker;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderingHandler
{
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            GuiQuestTracker.renderQuests(event.getResolution(), event.getPartialTicks());
        }
    }
}