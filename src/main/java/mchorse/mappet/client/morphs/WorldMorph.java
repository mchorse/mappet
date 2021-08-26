package mchorse.mappet.client.morphs;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.utils.DummyEntity;
import mchorse.mclib.utils.Interpolations;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldMorph implements IMessage
{
    public AbstractMorph morph;
    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public Entity entity;
    public int expiration;
    public boolean rotate;

    private int entityId;
    private DummyEntity dummy;

    @SideOnly(Side.CLIENT)
    public void render(float partialTicks)
    {
        Entity camera = Minecraft.getMinecraft().getRenderViewEntity();

        if (camera == null)
        {
            return;
        }

        EntityLivingBase dummy = this.getDummy();

        double x = Interpolations.lerp(dummy.prevPosX, dummy.posX, partialTicks);
        double y = Interpolations.lerp(dummy.prevPosY, dummy.posY, partialTicks);
        double z = Interpolations.lerp(dummy.prevPosZ, dummy.posZ, partialTicks);

        x -= Interpolations.lerp(camera.prevPosX, camera.posX, partialTicks);
        y -= Interpolations.lerp(camera.prevPosY, camera.posY, partialTicks);
        z -= Interpolations.lerp(camera.prevPosZ, camera.posZ, partialTicks);

        int combinedBrightness = dummy.getBrightnessForRender();
        int brightnessX = combinedBrightness % 65536;
        int brightnessY = combinedBrightness / 65536;

        GlStateManager.color(1, 1, 1, 1);
        RenderHelper.enableStandardItemLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightnessX, brightnessY);

        MorphUtils.render(this.morph, dummy, x, y, z, 0, partialTicks);

        RenderHelper.disableStandardItemLighting();
    }

    @SideOnly(Side.CLIENT)
    public boolean update()
    {
        EntityLivingBase dummy = this.getDummy();

        this.morph.update(dummy);
        dummy.ticksExisted += 1;

        return dummy.ticksExisted > this.expiration;
    }

    @SideOnly(Side.CLIENT)
    private EntityLivingBase getDummy()
    {
        if (this.dummy == null)
        {
            this.dummy = new DummyEntity(Minecraft.getMinecraft().world);
        }

        Entity entity = this.getEntity();

        if (entity == null)
        {
            this.dummy.setPosition(this.x, this.y, this.z);
            this.dummy.prevPosX = this.dummy.posX;
            this.dummy.prevPosY = this.dummy.posY;
            this.dummy.prevPosZ = this.dummy.posZ;
            this.dummy.lastTickPosX = this.dummy.posX;
            this.dummy.lastTickPosY = this.dummy.posY;
            this.dummy.lastTickPosZ = this.dummy.posZ;
            this.dummy.rotationYaw = this.dummy.prevRotationYaw = this.yaw;
            this.dummy.rotationPitch = this.dummy.prevRotationPitch = this.pitch;
            this.dummy.rotationYawHead = this.dummy.prevRotationYawHead = this.yaw;
            this.dummy.renderYawOffset = this.dummy.prevRenderYawOffset = this.yaw;
        }
        else
        {
            this.dummy.setPosition(entity.posX + this.x, entity.posY + this.y, entity.posZ + this.z);

            if (entity.isDead)
            {
                this.dummy.prevPosX = this.dummy.posX;
                this.dummy.prevPosY = this.dummy.posY;
                this.dummy.prevPosZ = this.dummy.posZ;
                this.dummy.lastTickPosX = this.dummy.posX;
                this.dummy.lastTickPosY = this.dummy.posY;
                this.dummy.lastTickPosZ = this.dummy.posZ;

                if (this.rotate)
                {
                    this.dummy.rotationYaw = this.dummy.prevRotationYaw = entity.rotationYaw + this.yaw;
                    this.dummy.rotationPitch = this.dummy.prevRotationPitch = entity.rotationPitch + this.pitch;

                    if (entity instanceof EntityLivingBase)
                    {
                        EntityLivingBase livingBase = (EntityLivingBase) entity;

                        this.dummy.rotationYawHead = this.dummy.prevRotationYawHead = livingBase.rotationYawHead + this.yaw;
                        this.dummy.renderYawOffset = this.dummy.prevRenderYawOffset = livingBase.renderYawOffset + this.yaw;
                    }
                }
            }
            else
            {
                this.dummy.prevPosX = entity.prevPosX + this.x;
                this.dummy.prevPosY = entity.prevPosY + this.y;
                this.dummy.prevPosZ = entity.prevPosZ + this.z;
                this.dummy.lastTickPosX = entity.lastTickPosX + this.x;
                this.dummy.lastTickPosY = entity.lastTickPosY + this.y;
                this.dummy.lastTickPosZ = entity.lastTickPosZ + this.z;

                if (this.rotate)
                {
                    this.dummy.rotationYaw = entity.rotationYaw + this.yaw;
                    this.dummy.rotationPitch = entity.rotationPitch + this.pitch;
                    this.dummy.prevRotationYaw = entity.prevRotationYaw + this.yaw;
                    this.dummy.prevRotationPitch = entity.prevRotationPitch + this.pitch;

                    if (entity instanceof EntityLivingBase)
                    {
                        EntityLivingBase livingBase = (EntityLivingBase) entity;

                        this.dummy.rotationYawHead = livingBase.rotationYawHead + this.yaw;
                        this.dummy.renderYawOffset = livingBase.renderYawOffset + this.yaw;
                        this.dummy.prevRotationYawHead = livingBase.prevRotationYawHead + this.yaw;
                        this.dummy.prevRenderYawOffset = livingBase.prevRenderYawOffset + this.yaw;
                    }
                }
            }
        }

        return this.dummy;
    }

    @SideOnly(Side.CLIENT)
    private Entity getEntity()
    {
        if (this.entityId >= 0 && this.entity == null)
        {
            this.entity = Minecraft.getMinecraft().world.getEntityByID(this.entityId);
        }

        return this.entity;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.morph = MorphUtils.morphFromBuf(buf);
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.expiration = buf.readInt();
        this.rotate = buf.readBoolean();
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        MorphUtils.morphToBuf(buf, this.morph);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        buf.writeInt(this.expiration);
        buf.writeBoolean(this.rotate);
        buf.writeInt(this.entity == null ? -1 : this.entity.getEntityId());
    }
}