package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.MappetQuests;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.utils.RayTracing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class ScriptEntity implements IScriptEntity
{
    private Entity entity;
    private IMappetStates states;
    private IMappetQuests quests;

    public ScriptEntity(Entity entity)
    {
        this.entity = entity;
    }

    @Override
    public Entity getMinecraftEntity()
    {
        return this.entity;
    }

    /* Entity properties */

    @Override
    public ScriptVector getPosition()
    {
        return new ScriptVector(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        this.entity.setPositionAndUpdate(x, y, z);
    }

    @Override
    public ScriptVector getMotion()
    {
        return new ScriptVector(this.entity.motionX, this.entity.motionY, this.entity.posZ);
    }

    @Override
    public void setMotion(double x, double y, double z)
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
    public ScriptVector getRotations()
    {
        return new ScriptVector(this.getPitch(), this.getYaw(), this.getYawHead());
    }

    @Override
    public void setRotations(float pitch, float yaw, float yawHead)
    {
        this.entity.setLocationAndAngles(this.entity.posX, this.entity.posY, this.entity.posZ, yaw, pitch);
        this.entity.setRotationYawHead(yawHead);

        if (this.isPlayer())
        {
            ((EntityPlayerMP) this.entity).connection.setPlayerLocation(this.entity.posX, this.entity.posY, this.entity.posZ, yaw, pitch);
        }
        else
        {
            EntityTracker tracker = ((WorldServer) this.entity.world).getEntityTracker();

            for (EntityPlayer player : tracker.getTrackingPlayers(this.entity))
            {
                Dispatcher.sendTo(new PacketEntityRotations(this.entity.getEntityId(), yaw, yawHead, pitch), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public float getPitch()
    {
        return this.entity.rotationPitch;
    }

    @Override
    public float getYaw()
    {
        return this.entity.rotationYaw;
    }

    @Override
    public float getYawHead()
    {
        return this.entity.getRotationYawHead();
    }

    @Override
    public ScriptVector getLook()
    {
        return new ScriptVector(this.entity.getLookVec());
    }

    @Override
    public float getHp()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ((EntityLivingBase) this.entity).getHealth();
        }

        return 0;
    }

    @Override
    public void setHp(float hp)
    {
        if (this.entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase) this.entity).setHealth(hp);
        }
    }

    @Override
    public float getMaxHp()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ((EntityLivingBase) this.entity).getMaxHealth();
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

    /* Ray tracing */

    @Override
    public IScriptRayTrace rayTrace(double maxDistance)
    {
        return new ScriptRayTrace(RayTracing.rayTraceWithEntity(this.entity, maxDistance));
    }

    @Override
    public IScriptRayTrace rayTraceBlock(double maxDistance)
    {
        return new ScriptRayTrace(RayTracing.rayTrace(this.entity, maxDistance, 0));
    }

    /* Items */

    @Override
    public IScriptItemStack getMainItem()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return new ScriptItemStack(((EntityLivingBase) this.entity).getHeldItemMainhand());
        }

        return ScriptItemStack.EMPTY;
    }

    @Override
    public IScriptItemStack getOffItem()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return new ScriptItemStack(((EntityLivingBase) this.entity).getHeldItemOffhand());
        }

        return ScriptItemStack.EMPTY;
    }

    /* Entity meta */

    @Override
    public boolean isAIEnabled()
    {
        if (this.entity instanceof EntityLiving)
        {
            return !((EntityLiving) this.entity).isAIDisabled();
        }

        return false;
    }

    @Override
    public void setAIEnabled(boolean enabled)
    {
        if (this.entity instanceof EntityLiving)
        {
            ((EntityLiving) this.entity).setNoAI(!enabled);
        }
    }

    @Override
    public String getUniqueId()
    {
        return this.entity.getCachedUniqueIdString();
    }

    @Override
    public String getEntityId()
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
    public boolean isLivingBase()
    {
        return this.entity instanceof EntityLivingBase;
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
    public IMappetStates getStates()
    {
        if (this.states == null)
        {
            States states = EntityUtils.getStates(this.entity);

            if (states != null)
            {
                this.states = new MappetStates(states);
            }
        }

        return this.states;
    }

    @Override
    public IMappetQuests getQuests()
    {
        if (this.quests == null && this.isPlayer())
        {
            EntityPlayer player = (EntityPlayer) this.entity;

            this.quests = new MappetQuests(Character.get(player).getQuests(), player);
        }

        return this.quests;
    }
}