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
        // super.doRender(entity, x, y, z, entityYaw, partialTicks);
        MorphUtils.render(entity.getMorph(), entity, x, y, z, entityYaw, partialTicks);
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