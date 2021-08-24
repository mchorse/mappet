package mchorse.mappet.client;

import mchorse.mappet.api.huds.HUDStage;
import mchorse.mappet.client.gui.GuiQuestTracker;
import mchorse.mappet.client.morphs.WorldMorph;
import mchorse.mclib.events.RenderOverlayEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderingHandler
{
    public static HUDStage stage = new HUDStage(false);
    public static HUDStage currentStage;
    public static List<WorldMorph> worldMorphs = new ArrayList<WorldMorph>();

    public static void update()
    {
        HUDStage stage = RenderingHandler.currentStage == null ? RenderingHandler.stage : RenderingHandler.currentStage;

        stage.update(stage == RenderingHandler.stage);

        worldMorphs.removeIf(WorldMorph::update);
    }

    public static void reset()
    {
        stage.reset();
        currentStage = null;
        worldMorphs.clear();
    }

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

    @SubscribeEvent
    public void onRenderLast(RenderWorldLastEvent event)
    {
        Minecraft.getMinecraft().entityRenderer.enableLightmap();

        for (WorldMorph morph : worldMorphs)
        {
            morph.render(event.getPartialTicks());
        }

        Minecraft.getMinecraft().entityRenderer.disableLightmap();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
    }
}