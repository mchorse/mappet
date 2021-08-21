package mchorse.mappet.api.huds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class HUDStage
{
    public Map<String, HUDScene> scenes = new LinkedHashMap<String, HUDScene>();

    private List<HUDMorph> renderOrtho = new ArrayList<HUDMorph>();
    private List<HUDMorph> renderPerpsective = new ArrayList<HUDMorph>();
    private boolean ignoreF1;

    public HUDStage(boolean ignoreF1)
    {
        this.ignoreF1 = ignoreF1;
    }

    public void reset()
    {
        this.scenes.clear();
    }

    public void update(boolean allowExpiring)
    {
        this.scenes.values().removeIf((scene) -> scene.update(allowExpiring));
    }

    public void render(ScaledResolution resolution, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();

        this.renderOrtho.clear();

        this.enableGLStates();

        int w = resolution.getScaledWidth();
        int h = resolution.getScaledHeight();
        float lastX = OpenGlHelper.lastBrightnessX;
        float lastY = OpenGlHelper.lastBrightnessY;

        /* Changing projection mode to perspective. In order for this to
         * work, depth buffer must also be cleared. Thanks to Gegy for
         * pointing this out (depth buffer)! */
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        float rx = (float) Math.ceil(mc.displayWidth / (double) w);
        float ry = (float) Math.ceil(mc.displayHeight / (double) h);

        int vx = (int) (0 * rx);
        int vy = (int) (mc.displayHeight - (0 + h) * ry);
        int vw = (int) (w * rx);
        int vh = (int) (h * ry);

        float aspect = (float) vw / (float) vh;
        float lastFov = Float.MIN_VALUE;

        GlStateManager.viewport(vx, vy, vw, vh);

        /* Default camera transformations */
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, -1, -2);

        /* Drawing begins */
        for (HUDScene scene : this.scenes.values())
        {
            if (mc.gameSettings.hideGUI && scene.hide && !this.ignoreF1)
            {
                continue;
            }

            if (lastFov != scene.fov)
            {
                GlStateManager.matrixMode(GL11.GL_PROJECTION);
                GlStateManager.loadIdentity();
                Project.gluPerspective(scene.fov, aspect, 0.05F, 1000);
                GlStateManager.matrixMode(GL11.GL_MODELVIEW);

                lastFov = scene.fov;
            }

            this.renderPerpsective.clear();

            for (HUDMorph morph : scene.morphs)
            {
                if (morph.ortho)
                {
                    this.renderOrtho.add(morph);
                }
                else
                {
                    this.renderPerpsective.add(morph);
                }
            }

            this.renderPerpsective.sort(this::depthSort);

            for (HUDMorph morph : this.renderPerpsective)
            {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

                morph.render(resolution, partialTicks);
            }
        }

        GlStateManager.popMatrix();

        this.setupOrtho(mc, w, h, true);

        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        this.renderOrtho.sort(this::depthSort);

        for (HUDMorph morph : this.renderOrtho)
        {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

            morph.render(resolution, partialTicks);
        }

        this.disableGLStates();
        this.setupOrtho(mc, w, h, false);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);
    }

    private int depthSort(HUDMorph a, HUDMorph b)
    {
        float diff = a.translate.z - b.translate.z;

        if (diff == 0)
        {
            return 0;
        }

        return diff < 0 ? -1 : 1;
    }

    private void setupOrtho(Minecraft mc, int w, int h, boolean flip)
    {
        /* Return back to orthographic projection */
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();

        if (flip)
        {
            GlStateManager.ortho(0, w, 0, h, 1000, 3000000);
        }
        else
        {
            GlStateManager.ortho(0, w, h, 0, 1000, 3000000);
        }

        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
    }

    private void enableGLStates()
    {
        /* Enable rendering states */
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void disableGLStates()
    {
        /* Disable rendering states */
        GlStateManager.enableCull();
        GlStateManager.disableDepth();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableAlpha();
        RenderHelper.disableStandardItemLighting();

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}