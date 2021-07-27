package mchorse.mappet.api.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
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

    private List<HUDMorph> renderPerspective = new ArrayList<HUDMorph>();
    private List<HUDMorph> renderOrtho = new ArrayList<HUDMorph>();

    public void reset()
    {
        this.scenes.clear();
    }

    public void update(boolean expire)
    {
        this.scenes.values().removeIf((scene) -> scene.update(expire));
    }

    public void render(ScaledResolution resolution, float partialTicks)
    {
        this.renderPerspective.clear();
        this.renderOrtho.clear();

        for (HUDScene scene : this.scenes.values())
        {
            scene.fill(this.renderPerspective, this.renderOrtho);
        }

        Minecraft mc = Minecraft.getMinecraft();
        int w = resolution.getScaledWidth();
        int h = resolution.getScaledHeight();

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

        GlStateManager.viewport(vx, vy, vw, vh);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        Project.gluPerspective(70, (float) vw / (float) vh, 0.05F, 1000);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);

        /* Enable rendering states */
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        /* Setup transformations */
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, -1, -2);

        /* Drawing begins */
        for (HUDMorph morph : this.renderPerspective)
        {
            morph.render(partialTicks);
        }

        GlStateManager.popMatrix();

        /* Return back to orthographic projection */
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, w, 0, h, 1000, 3000000);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);

        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);

        for (HUDMorph morph : this.renderOrtho)
        {
            morph.render(partialTicks);
        }

        /* Disable rendering states */
        GlStateManager.enableCull();
        GlStateManager.disableDepth();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableAlpha();
        RenderHelper.disableStandardItemLighting();

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        /* Return back to orthographic projection */
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, w, h, 0, 1000, 3000000);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
    }
}