package mchorse.mappet.api.huds;

import mchorse.mclib.utils.DummyEntity;
import mchorse.mclib.utils.NBTUtils;
import mchorse.metamorph.api.Morph;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;

public class HUDMorph implements INBTSerializable<NBTTagCompound>
{
    public Morph morph = new Morph();
    public boolean ortho;
    public float orthoX;
    public float orthoY;
    public int expire;
    public Vector3f translate = new Vector3f();
    public Vector3f scale = new Vector3f(1, 1, 1);
    public Vector3f rotate = new Vector3f();

    @SideOnly(Side.CLIENT)
    private DummyEntity entity;

    private int tick;

    @SideOnly(Side.CLIENT)
    public DummyEntity getEntity()
    {
        if (this.entity == null)
        {
            this.entity = new DummyEntity(Minecraft.getMinecraft().world);
            this.entity.rotationYaw = this.entity.prevRotationYaw = 0.0F;
            this.entity.rotationPitch = this.entity.prevRotationPitch = 0.0F;
            this.entity.rotationYawHead = this.entity.prevRotationYawHead = 0.0F;
            this.entity.renderYawOffset = this.entity.prevRenderYawOffset = 0.0F;
            this.entity.onGround = true;
        }

        return this.entity;
    }

    @SideOnly(Side.CLIENT)
    public void render(ScaledResolution resolution, float partialTicks)
    {
        if (this.morph.isEmpty())
        {
            return;
        }

        float tx = this.translate.x;
        float ty = this.translate.y;
        float tz = this.translate.z;
        float sx = this.scale.x;
        float sy = this.scale.y;
        float sz = this.scale.z;
        float rx = this.rotate.x;
        float ry = this.rotate.y;
        float rz = this.rotate.z;

        if (this.ortho)
        {
            tx = resolution.getScaledWidth() * this.orthoX + tx;
            ty = resolution.getScaledHeight() * this.orthoY + ty;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(tx, ty, tz);
        GlStateManager.rotate(rz, 0, 0, 1);
        GlStateManager.rotate(ry, 0, 1, 0);
        GlStateManager.rotate(rx, 1, 0, 0);
        GlStateManager.scale(sx, sy, sz);

        this.morph.get().render(this.getEntity(), 0, 0, 0, 0, partialTicks);

        GlStateManager.popMatrix();
    }

    @SideOnly(Side.CLIENT)
    public boolean update(boolean allowExpiring)
    {
        DummyEntity entity = this.getEntity();

        if (!this.morph.isEmpty())
        {
            this.morph.get().update(entity);
        }

        entity.ticksExisted += 1;
        this.tick += 1;

        if (!allowExpiring)
        {
            return false;
        }

        return this.expire > 0 && this.tick >= this.expire;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound morph = this.morph.toNBT();

        if (morph != null)
        {
            tag.setTag("Morph", morph);
        }

        tag.setBoolean("Ortho", this.ortho);
        tag.setFloat("OrthoX", this.orthoX);
        tag.setFloat("OrthoY", this.orthoY);
        tag.setInteger("Expire", this.expire);

        if (this.translate.x != 0 || this.translate.y != 0 || this.translate.z != 0)
        {
            tag.setTag("Translate", NBTUtils.writeFloatList(new NBTTagList(), this.translate));
        }

        if (this.scale.x != 1 || this.scale.y != 1 || this.scale.z != 1)
        {
            tag.setTag("Scale", NBTUtils.writeFloatList(new NBTTagList(), this.scale));
        }

        if (this.rotate.x != 0 || this.rotate.y != 0 || this.rotate.z != 0)
        {
            tag.setTag("Rotate", NBTUtils.writeFloatList(new NBTTagList(), this.rotate));
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Morph"))
        {
            this.morph.fromNBT(tag.getCompoundTag("Morph"));
        }

        this.ortho = tag.getBoolean("Ortho");
        this.orthoX = tag.getFloat("OrthoX");
        this.orthoY = tag.getFloat("OrthoY");
        this.expire = tag.getInteger("Expire");

        NBTUtils.readFloatList(tag.getTagList("Translate", 5), this.translate);
        NBTUtils.readFloatList(tag.getTagList("Scale", 5), this.scale);
        NBTUtils.readFloatList(tag.getTagList("Rotate", 5), this.rotate);
    }
}