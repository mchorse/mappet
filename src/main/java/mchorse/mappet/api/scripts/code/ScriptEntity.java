package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ScriptEntity implements IScriptEntity
{
    private Entity entity;

    public ScriptEntity(Entity entity)
    {
        this.entity = entity;
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    /* Entity properties */

    @Override
    public ScriptVector position()
    {
        return new ScriptVector(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        this.entity.setPositionAndUpdate(x, y, z);
    }

    @Override
    public float hp()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ((EntityLivingBase) this.entity).getHealth();
        }

        return 0;
    }

    @Override
    public boolean isSneaking()
    {
        return this.entity.isSneaking();
    }

    @Override
    public boolean isSprinting()
    {
        return this.entity.isSprinting();
    }

    /* Items */

    @Override
    public IScriptItemStack mainItem()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return new ScriptItemStack(((EntityLivingBase) this.entity).getHeldItemMainhand());
        }

        return ScriptItemStack.EMPTY;
    }

    @Override
    public IScriptItemStack offItem()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return new ScriptItemStack(((EntityLivingBase) this.entity).getHeldItemOffhand());
        }

        return ScriptItemStack.EMPTY;
    }

    /* Entity meta */

    @Override
    public String uniqueId()
    {
        return this.entity.getCachedUniqueIdString();
    }

    @Override
    public String entityId()
    {
        ResourceLocation rl = EntityList.getKey(this.entity);

        return rl == null ? "" : rl.toString();
    }

    @Override
    public boolean isPlayer()
    {
        return this.entity instanceof EntityPlayer;
    }
}