package mchorse.mappet.client;

import mchorse.mappet.api.hud.HUDStage;
import mchorse.mappet.client.gui.GuiQuestTracker;
import mchorse.mclib.events.RenderOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderingHandler
{
    public static HUDStage stage = new HUDStage();
    public static HUDStage currentStage;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL)
        {
            GuiQuestTracker.renderQuests(event.getResolution(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(RenderOverlayEvent.Pre event)
    {
        HUDStage stage = RenderingHandler.currentStage != null ? RenderingHandler.currentStage : RenderingHandler.stage;

        stage.render(event.resolution, event.mc.getRenderPartialTicks());
    }
}