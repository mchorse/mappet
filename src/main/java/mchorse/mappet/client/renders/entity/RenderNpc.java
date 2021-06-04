package mchorse.mappet.client.renders.entity;

import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import javax.annotation.Nullable;

public class RenderNpc extends RenderLiving<EntityNpc>
{
    public RenderNpc(RenderManager renderManager)
    {
        super(renderManager, null, 0.6F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityNpc entity)
    {
        return null;
    }

    @Override
    public void doRender(EntityNpc entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float yawHead = entity.rotationYawHead;
        float prevYawHead = entity.prevRotationYawHead;
        float bodyYaw = entity.renderYawOffset;
        float bodyPrevYaw = entity.prevRenderYawOffset;

        entity.rotationYawHead = entity.smoothYawHead;
        entity.prevRotationYawHead = entity.prevSmoothYawHead;
        entity.renderYawOffset = entity.smoothBodyYawHead;
        entity.prevRenderYawOffset = entity.prevSmoothBodyYawHead;

        MorphUtils.render(entity.getMorph(), entity, x, y, z, entityYaw, partialTicks);

        entity.rotationYawHead = yawHead;
        entity.prevRotationYawHead = prevYawHead;
        entity.renderYawOffset = bodyYaw;
        entity.prevRenderYawOffset = bodyPrevYaw;

        this.renderLeash(entity, x, y, z, yawHead, partialTicks);
    }

    public static class Factory implements IRenderFactory<EntityNpc>
    {
        @Override
        public RenderNpc createRenderFor(RenderManager manager)
        {
            return new RenderNpc(manager);
        }
    }
}