package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.api.scripts.code.ScriptRayTrace;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.states.States;
import mchorse.mappet.client.morphs.WorldMorph;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mappet.network.common.scripts.PacketWorldMorph;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.utils.RayTracing;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class ScriptEntity <T extends Entity> implements IScriptEntity
{
    protected T entity;
    protected IMappetStates states;

    public static IScriptEntity create(Entity entity)
    {
        if (entity instanceof EntityPlayerMP)
        {
            return new ScriptPlayer((EntityPlayerMP) entity);
        }
        else if (entity instanceof EntityNpc)
        {
            return new ScriptNpc((EntityNpc) entity);
        }
        else if (entity != null)
        {
            return new ScriptEntity<Entity>(entity);
        }

        return null;
    }

    protected ScriptEntity(T entity)
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
        return new ScriptVector(this.entity.motionX, this.entity.motionY, this.entity.motionZ);
    }

    @Override
    public void setMotion(double x, double y, double z)
    {
        this.entity.motionX = x;
        this.entity.motionY = y;
        this.entity.motionZ = z;
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

        if (!this.isPlayer())
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
    public float getWidth()
    {
        return this.entity.width;
    }

    @Override
    public float getHeight()
    {
        return this.entity.height;
    }

    @Override
    public float getHp()
    {
        if (this.isLivingBase())
        {
            return ((EntityLivingBase) this.entity).getHealth();
        }

        return 0;
    }

    @Override
    public void setHp(float hp)
    {
        if (this.isLivingBase())
        {
            ((EntityLivingBase) this.entity).setHealth(hp);
        }
    }

    @Override
    public float getMaxHp()
    {
        if (this.isLivingBase())
        {
            return ((EntityLivingBase) this.entity).getMaxHealth();
        }

        return 0;
    }

    @Override
    public boolean isBurning()
    {
        return this.entity.isBurning();
    }

    @Override
    public void setBurning(int seconds)
    {
        if (seconds <= 0)
        {
            this.entity.extinguish();
        }
        else
        {
            this.entity.setFire(seconds);
        }
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
        if (this.isLivingBase())
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getHeldItemMainhand());
        }

        return ScriptItemStack.EMPTY;
    }

    @Override
    public void setMainItem(IScriptItemStack stack)
    {
        this.setItem(EnumHand.MAIN_HAND, stack);
    }

    @Override
    public IScriptItemStack getOffItem()
    {
        if (this.isLivingBase())
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getHeldItemOffhand());
        }

        return ScriptItemStack.EMPTY;
    }

    @Override
    public void setOffItem(IScriptItemStack stack)
    {
        this.setItem(EnumHand.OFF_HAND, stack);
    }

    private void setItem(EnumHand hand, IScriptItemStack stack)
    {
        if (stack == null)
        {
            stack = ScriptItemStack.EMPTY;
        }

        if (this.isLivingBase())
        {
            ((EntityLivingBase) this.entity).setHeldItem(hand, stack.getMinecraftItemStack().copy());
        }
    }

    /* Entity meta */

    @Override
    public void setSpeed(float speed)
    {
        if (this.isLivingBase())
        {
            ((EntityLivingBase) this.entity).getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) speed);
        }
    }

    @Override
    public IScriptEntity getTarget()
    {
        if (this.entity instanceof EntityLiving)
        {
            return ScriptEntity.create(((EntityLiving) this.entity).getAttackTarget());
        }

        return null;
    }

    @Override
    public void setTarget(IScriptEntity entity)
    {
        if (this.entity instanceof EntityLiving && entity != null && entity.isLivingBase())
        {
            ((EntityLiving) this.entity).setAttackTarget((EntityLivingBase) entity.getMinecraftEntity());
        }
    }

    @Override
    public boolean isAIEnabled()
    {
        if (this.isLivingBase())
        {
            return !((EntityLiving) this.entity).isAIDisabled();
        }

        return false;
    }

    @Override
    public void setAIEnabled(boolean enabled)
    {
        if (this.isLivingBase())
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
    public int getTicks()
    {
        return this.entity.ticksExisted;
    }

    @Override
    public String getName()
    {
        return this.entity.getName();
    }

    @Override
    public INBTCompound getFullData()
    {
        return new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void setFullData(INBTCompound data)
    {
        this.entity.readFromNBT(data.getNBTTagComound());
    }

    @Override
    public INBTCompound getEntityData()
    {
        return new ScriptNBTCompound(this.entity.getEntityData());
    }

    @Override
    public boolean isPlayer()
    {
        return this.entity instanceof EntityPlayer;
    }

    @Override
    public boolean isNpc()
    {
        return this.entity instanceof EntityNpc;
    }

    @Override
    public boolean isLivingBase()
    {
        return this.entity instanceof EntityLivingBase;
    }

    @Override
    public boolean isSame(IScriptEntity entity)
    {
        return this.entity == entity.getMinecraftEntity();
    }

    @Override
    public void damage(float health)
    {
        if (this.isLivingBase())
        {
            this.entity.attackEntityFrom(DamageSource.OUT_OF_WORLD, health);
        }
    }

    @Override
    public void damageAs(IScriptEntity entity, float damage)
    {
        if (this.isLivingBase() && entity.isLivingBase())
        {
            EntityLivingBase target = (EntityLivingBase) this.entity;
            EntityLivingBase source = (EntityLivingBase) entity.getMinecraftEntity();

            target.attackEntityFrom(DamageSource.causeMobDamage(source), damage);
        }
    }

    @Override
    public void damageWithItemsAs(IScriptPlayer player)
    {
        player.getMinecraftPlayer().attackTargetEntityWithCurrentItem(this.entity);
    }

    @Override
    public float getFallDistance()
    {
        return this.entity.fallDistance;
    }

    @Override
    public void setFallDistance(float distance)
    {
        this.entity.fallDistance = distance;
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

    @Override
    public void swingArm(int arm)
    {
        if (this.isLivingBase())
        {
            ((EntityLivingBase) this.entity).swingArm(arm == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        }
    }

    /* Potion effects */

    @Override
    public void applyPotion(Potion potion, int duration, int amplifier, boolean particles)
    {
        if (this.isLivingBase())
        {
            PotionEffect effect = new PotionEffect(potion, duration, amplifier, false, particles);

            ((EntityLivingBase) this.entity).addPotionEffect(effect);
        }
    }

    @Override
    public boolean hasPotion(Potion potion)
    {
        if (this.isLivingBase())
        {
            return ((EntityLivingBase) this.entity).isPotionActive(potion);
        }

        return false;
    }

    @Override
    public boolean removePotion(Potion potion)
    {
        if (this.isLivingBase())
        {
            EntityLivingBase entity = (EntityLivingBase) this.entity;
            int size = entity.getActivePotionMap().size();

            entity.removePotionEffect(potion);

            return size != entity.getActivePotionMap().size();
        }

        return false;
    }

    @Override
    public void clearPotions()
    {
        if (this.isLivingBase())
        {
            ((EntityLivingBase) this.entity).clearActivePotions();
        }
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
    public boolean setMorph(AbstractMorph morph)
    {
        return false;
    }

    @Override
    public void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch, boolean rotate)
    {
        if (morph == null)
        {
            return;
        }

        WorldMorph worldMorph = new WorldMorph();

        worldMorph.morph = morph;
        worldMorph.expiration = expiration;
        worldMorph.rotate = rotate;
        worldMorph.x = x;
        worldMorph.y = y;
        worldMorph.z = z;
        worldMorph.yaw = yaw;
        worldMorph.pitch = pitch;
        worldMorph.entity = this.entity;

        PacketWorldMorph message = new PacketWorldMorph(worldMorph);

        Dispatcher.sendToTracked(this.entity, message);

        if (this.isPlayer())
        {
            Dispatcher.sendTo(message, (EntityPlayerMP) this.entity);
        }
    }
}