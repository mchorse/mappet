package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.ResourceLocation;

public class ScriptEntity implements IScriptEntity
{
    private Entity entity;
    private IMappetStates states;

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
    public void position(double x, double y, double z)
    {
        this.entity.setPositionAndUpdate(x, y, z);
    }

    @Override
    public ScriptVector motion()
    {
        return new ScriptVector(this.entity.motionX, this.entity.motionY, this.entity.posZ);
    }

    @Override
    public void motion(double x, double y, double z)
    {
        this.entity.motionX = x;
        this.entity.motionY = y;
        this.entity.motionZ = z;

        if (this.isPlayer())
        {
            ((EntityPlayerMP) this.entity).connection.sendPacket(new SPacketEntityVelocity(this.entity.getEntityId(), x, y, z));
        }
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

    @Override
    public void remove()
    {
        this.entity.setDead();
    }

    @Override
    public void kill()
    {
        this.entity.onKillCommand();
    }

    /* Mappet stuff */

    @Override
    public IMappetStates states()
    {
        if (this.states == null)
        {
            States states = WorldUtils.getStates(this.entity);

            if (states != null)
            {
                this.states = new MappetStates(states);
            }
        }

        return this.states;
    }
}