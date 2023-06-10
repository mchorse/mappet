package mchorse.mappet.client.renders.tile;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.client.gui.panels.GuiConditionModelPanel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mclib.client.Draw;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.input.GuiTransformations;
import mchorse.mclib.utils.MatrixUtils;
import mchorse.mclib.utils.RenderingUtils;
import mchorse.metamorph.api.EntityUtils;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.util.List;

public class TileConditionModelRenderer extends TileEntitySpecialRenderer<TileConditionModel>
{
    public RenderShadow renderer;

    public TileConditionModelRenderer()
    {
    }

    public void render(TileConditionModel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (!Blockbuster.modelBlockDisableRendering.get())
        {
            renderModel(te, x, y, z, partialTicks, destroyStage, alpha);
        }

        if (mc.gameSettings.showDebugInfo && (!mc.gameSettings.hideGUI || (Boolean) Blockbuster.modelBlockRenderDebuginf1.get()))
        {
            renderDebug(te, x, y, z);
        }
    }

    public void renderModel(TileConditionModel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Minecraft mc = Minecraft.getMinecraft();

        float xx;
        float yy;
        float zz;

        if (this.renderer == null)
        {
            this.renderer = new RenderShadow(mc.getRenderManager());
        }
        TileEntityModelSettings teSettings = te.getSettings();
        EntityLivingBase entity = te.entity;

        if (entity == null)
        {
            te.createEntity(mc.world);
            entity = te.entity;
        }

        AbstractMorph newMorph = EntityUtils.getMorph(entity);

        if (newMorph == null)
        {
            return;
        }

        BlockPos pos = te.getPos();
        entity.setPositionAndRotation(pos.getX() + 0.5F + teSettings.getX(), pos.getY() + teSettings.getY(), pos.getZ() + 0.5F + teSettings.getZ(), 0.0F, 0.0F);
        entity.setLocationAndAngles(pos.getX() + 0.5F + teSettings.getX(), pos.getY() + teSettings.getY(), pos.getZ() + 0.5F + teSettings.getZ(), 0.0F, 0.0F);
        entity.rotationYawHead = entity.prevRotationYawHead;
        entity.rotationYaw = entity.prevRotationYaw = 0.0F;
        entity.rotationPitch = entity.prevRotationPitch;
        entity.renderYawOffset = entity.prevRenderYawOffset;
        entity.setVelocity(0.0, 0.0, 0.0);
        xx = (float) x + 0.5F + teSettings.getX();
        yy = (float) y + teSettings.getY();
        zz = (float) z + 0.5F + teSettings.getZ();
        GlStateManager.pushMatrix();
        GlStateManager.translate(xx, yy, zz);
        boolean wasSet = MatrixUtils.captureMatrix();
        this.transform(te);
        MorphUtils.render(newMorph, entity, 0.0, 0.0, 0.0, 0.0F, partialTicks);
        this.drawAxis(te);
        GlStateManager.popMatrix();
        if (te.isShadow)
        {
            this.renderer.setShadowSize(newMorph.getWidth(entity) * 0.8F);
            this.renderer.doRenderShadowAndFire(te.entity, xx, yy, zz, 0.0F, partialTicks);
        }

        if (wasSet)
        {
            MatrixUtils.releaseMatrix();
        }
    }

    public void renderDebug(TileConditionModel te, double x, double y, double z)
    {
        float xx;
        float yy;
        float zz;

        TileEntityModelSettings teSettings = te.getSettings();

        int shader = GL11.glGetInteger(35725);
        if (shader != 0)
        {
            OpenGlHelper.glUseProgram(0);
        }

        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        xx = 0.0F;
        yy = 1.0F;
        zz = 0.5F;
        if (te.entity != null && !te.entity.morph.isEmpty() && te.entity.morph.get().errorRendering)
        {
            xx = 1.0F;
            zz = 0.0F;
            yy = 0.0F;
        }

        Draw.cube(buffer, x + 0.25, y + 0.25, z + 0.25, x + 0.75, y + 0.75, z + 0.75, xx, yy, zz, 0.35F);
        Draw.cube(buffer, x + 0.44999998807907104 + (double) teSettings.getX(), y + (double) teSettings.getY(), z + 0.44999998807907104 + (double) teSettings.getZ(), x + 0.550000011920929 + (double) teSettings.getX(), y + 0.10000000149011612 + (double) teSettings.getY(), z + 0.550000011920929 + (double) teSettings.getZ(), 1.0F, 1.0F, 1.0F, 0.85F);
        double distance = MathHelper.sqrt(Vec3d.ZERO.squareDistanceTo(teSettings.getX(), teSettings.getY(), teSettings.getZ()));
        if (distance > 0.1)
        {
            Draw.cube(buffer, x + 0.44999998807907104, y, z + 0.44999998807907104, x + 0.550000011920929, y + 0.10000000149011612, z + 0.550000011920929, 1.0F, 1.0F, 1.0F, 0.85F);
            tessellator.draw();
            double horizontalDistance = MathHelper.sqrt(teSettings.getX() * teSettings.getX() + teSettings.getZ() * teSettings.getZ());
            double yaw = 180.0 - MathHelper.atan2(teSettings.getZ(), teSettings.getX()) * 180.0 / Math.PI + 90.0;
            double pitch = MathHelper.atan2(teSettings.getY(), horizontalDistance) * 180.0 / Math.PI;
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 0.05000000074505806, z + 0.5);
            GL11.glRotated(yaw, 0.0, 1.0, 0.0);
            GL11.glRotated(pitch, 1.0, 0.0, 0.0);
            Draw.cube(-0.02500000037252903, -0.02500000037252903, 0.0, 0.02500000037252903, 0.02500000037252903, -distance, 0.0F, 0.0F, 0.0F, 0.5F);
            GL11.glPopMatrix();
        }
        else
        {
            tessellator.draw();
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        if (shader != 0)
        {
            OpenGlHelper.glUseProgram(shader);
        }
    }

    @Override
    public boolean isGlobalRenderer(TileConditionModel te)
    {
        return te.isGlobal;
    }

    private void drawAxis(TileConditionModel te)
    {
        List<GuiConditionModelPanel> childList = GuiBase.getCurrentChildren(GuiConditionModelPanel.class);
        if (childList != null)
        {
            GuiConditionModelPanel modelBlockPanel = (GuiConditionModelPanel) childList.get(0);
            if (modelBlockPanel != null && modelBlockPanel.isOpened() && modelBlockPanel.isSelected(te))
            {
                GlStateManager.pushMatrix();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.disableTexture2D();
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                Draw.point(0.0, 0.0, 0.0);
                if (GuiTransformations.GuiStaticTransformOrientation.getOrientation() == GuiTransformations.TransformOrientation.GLOBAL)
                {
                    TileEntityModelSettings teSettings = te.getSettings();
                    Vector3d rotation = new Vector3d(Math.toRadians((double) teSettings.getRx()), Math.toRadians((double) teSettings.getRy()), Math.toRadians((double) teSettings.getRz()));
                    Vector3d scale = new Vector3d((double) teSettings.getSx(), (double) teSettings.getSy(), (double) teSettings.getSz());
                    RenderingUtils.glRevertRotationScale(rotation, scale, teSettings.getOrder());
                }

                Draw.axis(0.25F);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.enableTexture2D();
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }

    public void transform(TileConditionModel te)
    {
        TileEntityModelSettings teSettings = te.getSettings();
        if (teSettings.getOrder() == MatrixUtils.RotationOrder.ZYX)
        {
            GlStateManager.rotate(teSettings.getRx(), 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(teSettings.getRy(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(teSettings.getRz(), 0.0F, 0.0F, 1.0F);
        }
        else
        {
            GlStateManager.rotate(teSettings.getRz(), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(teSettings.getRy(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(teSettings.getRx(), 1.0F, 0.0F, 0.0F);
        }

        if (teSettings.isUniform())
        {
            GlStateManager.scale(teSettings.getSx(), teSettings.getSx(), teSettings.getSx());
        }
        else
        {
            GlStateManager.scale(teSettings.getSx(), teSettings.getSy(), teSettings.getSz());
        }
    }

    public static class RenderShadow extends Render<Entity>
    {
        protected RenderShadow(RenderManager renderManager)
        {
            super(renderManager);
        }

        protected ResourceLocation getEntityTexture(Entity entity)
        {
            return null;
        }

        public void setShadowSize(float size)
        {
            this.shadowSize = size;
            this.shadowOpaque = 0.8F;
        }
    }
}
