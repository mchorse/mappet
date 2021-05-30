package mchorse.mappet.entities;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.factions.FactionAttitude;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcDrop;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.entities.ai.EntityAIAttackNpcMelee;
import mchorse.mappet.entities.ai.EntityAIFollowTarget;
import mchorse.mappet.entities.ai.EntityAIPatrol;
import mchorse.mappet.entities.ai.EntityAIReturnToPost;
import mchorse.mappet.entities.utils.NpcDamageSource;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcMorph;
import mchorse.mappet.network.common.npc.PacketNpcState;
import mchorse.mclib.utils.Interpolations;
import mchorse.mclib.utils.MathUtils;
import mchorse.metamorph.api.Morph;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;

public class EntityNpc extends EntityCreature implements IEntityAdditionalSpawnData, IMorphProvider
{
    public static final int RENDER_DISTANCE = 160;

    private Morph morph = new Morph();
    private NpcState state = new NpcState();

    private int lastDamageTime;
    private boolean unkillableFailsafe = true;
    private Faction faction;

    public float smoothYawHead;
    public float prevSmoothYawHead;
    public float smoothBodyYawHead;
    public float prevSmoothBodyYawHead;

    public EntityNpc(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3125D);
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();

        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();

        double speed = 1D;

        if (this.state != null)
        {
            speed = this.state.speed;

            if (this.state.canSwim)
            {
                this.tasks.addTask(0, new EntityAISwimming(this));
            }

            if (!this.state.follow.isEmpty())
            {
                this.tasks.addTask(6, new EntityAIFollowTarget(this, speed, 2, 10));
            }
            else if (this.state.hasPost && this.state.postPosition != null)
            {
                this.tasks.addTask(6, new EntityAIReturnToPost(this, this.state.postPosition, speed, this.state.postRadius));
            }
            else if (!this.state.patrol.isEmpty())
            {
                this.tasks.addTask(6, new EntityAIPatrol(this, speed));
            }

            if (this.state.lookAround)
            {
                this.tasks.addTask(8, new EntityAILookIdle(this));
            }

            if (this.state.lookAtPlayer)
            {
                this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
            }

            if (this.state.wander)
            {
                this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, speed / 2D));
            }
        }

        this.tasks.addTask(4, new EntityAIAttackNpcMelee(this, speed, false));

        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityLivingBase>(this, EntityLivingBase.class, 10, true, false, this::targetCheck));
    }

    private boolean targetCheck(EntityLivingBase entity)
    {
        if (entity instanceof EntityNpc)
        {
            EntityNpc npc = (EntityNpc) entity;

            if (this.faction != null)
            {
                return this.faction.get(npc.getState().faction) == FactionAttitude.AGGRESSIVE;
            }
        }

        if (entity instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entity;

            if (player.isSpectator() || player.isCreative())
            {
                return false;
            }

            ICharacter character = Character.get(player);

            if (this.faction != null && character != null)
            {
                return this.faction.get(character.getStates()) == FactionAttitude.AGGRESSIVE;
            }
        }

        return this.faction != null && this.faction.othersAttitude == FactionAttitude.AGGRESSIVE;
    }

    public void initialize()
    {
        this.state.triggerInitialize.trigger(this);
    }

    /* Getter and setters */

    public void setNpc(Npc npc, NpcState state)
    {
        this.setState(state, false);

        if (this.state.id.isEmpty())
        {
            this.state.id = npc.getId();
        }
    }

    public String getId()
    {
        return this.state.id;
    }

    public NpcState getState()
    {
        return this.state;
    }

    public void setState(NpcState state, boolean notify)
    {
        this.state = new NpcState();
        this.state.deserializeNBT(state.serializeNBT());

        /* Set */
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.state.pathDistance);
        this.navigator = this.createNavigator(this.world);

        /* Set health */
        double max = this.getMaxHealth();
        double health = this.getHealth();

        this.setMaxHealth(state.maxHealth);
        this.setHealth((float) MathHelper.clamp(state.maxHealth * (health / max), 1, state.maxHealth));

        this.isImmuneToFire = !this.state.canGetBurned;
        this.experienceValue = this.state.xp;

        /* Morphing */
        this.morph.set(state.morph);

        if (notify)
        {
            Dispatcher.sendToTracked(this, new PacketNpcMorph(this));
        }

        this.faction = state.faction.isEmpty() ? null : Mappet.factions.load(state.faction);

        this.initEntityAI();
    }

    @Override
    public AbstractMorph getMorph()
    {
        return this.morph.get();
    }

    public EntityLivingBase getFollowTarget()
    {
        if (this.state.follow.isEmpty())
        {
            return null;
        }

        if (this.state.follow.equals("@r"))
        {
            List<EntityPlayer> players = this.world.playerEntities;
            int index = (int) MathUtils.clamp(Math.random() * players.size() - 1, 0, players.size() - 1);

            return players.isEmpty() ? null : players.get(index);
        }
        else
        {
            try
            {
                EntityPlayer player = this.world.getPlayerEntityByName(this.state.follow);

                return player == null ? this.world.getPlayerEntityByUUID(UUID.fromString(this.state.follow)) : player;
            }
            catch (Exception e)
            {}
        }

        return null;
    }

    public void setMorph(AbstractMorph morph)
    {
        this.morph.set(morph);
    }

    public void setMaxHealth(double value)
    {
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(value);
    }

    /* Other stuff */

    @Override
    public void onUpdate()
    {
        this.healthFailsafe();

        super.onUpdate();

        this.updateArmSwingProgress();

        if (!this.morph.isEmpty())
        {
            this.morph.get().update(this);
        }

        if (this.state.regenDelay > 0 && !this.world.isRemote)
        {
            if (this.lastDamageTime >= this.state.regenDelay && this.ticksExisted % this.state.regenFrequency == 0)
            {
                if (this.getHealth() > 0 && this.getHealth() < this.getMaxHealth())
                {
                    this.heal(1F);
                }
            }

            this.lastDamageTime += 1;
        }

        if (this.world.isRemote)
        {
            this.prevSmoothYawHead = this.smoothYawHead;
            this.smoothYawHead = Interpolations.lerpYaw(this.smoothYawHead, this.rotationYawHead, 0.5F);
            this.prevSmoothBodyYawHead = this.smoothBodyYawHead;
            this.smoothBodyYawHead = Interpolations.lerpYaw(this.smoothBodyYawHead, this.renderYawOffset, 0.5F);
        }
        else
        {
            this.state.triggerTick.trigger(this);
        }
    }

    @Override
    protected void onDeathUpdate()
    {
        if (this.state.killable || !this.unkillableFailsafe)
        {
            super.onDeathUpdate();
        }
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
        for (NpcDrop drop : this.state.drops)
        {
            if (this.rand.nextFloat() < drop.chance)
            {
                this.entityDropItem(drop.stack.copy(), 0F);
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        DamageSource source = Mappet.npcsPeacefulDamage.get() ? new NpcDamageSource(this) : DamageSource.causeMobDamage(this);

        entityIn.attackEntityFrom(source, this.state.damage);

        return super.attackEntityAsMob(entityIn);
    }

    @Override
    protected void damageEntity(DamageSource damage, float damageAmount)
    {
        super.damageEntity(damage, damageAmount);

        if (!this.isEntityInvulnerable(damage))
        {
            this.lastDamageTime = 0;
        }

        this.healthFailsafe();
        this.state.triggerDamaged.trigger(this);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
        if (this.state.invincible)
        {
            return !(source.isCreativePlayer() || source == DamageSource.OUT_OF_WORLD);
        }

        if (!this.state.canFallDamage && source == DamageSource.FALL)
        {
            return true;
        }

        return super.isEntityInvulnerable(source);
    }

    @Override
    public void onKillCommand()
    {
        this.unkillableFailsafe = false;

        super.onKillCommand();
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        this.state.triggerDied.trigger(this);
    }

    @Override
    protected boolean canDespawn()
    {
        return this.state.unique;
    }

    public void healthFailsafe()
    {
        if (!this.state.killable && this.getHealth() <= 0 && this.unkillableFailsafe)
        {
            this.setHealth(0.001F);
        }
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        if (!this.world.isRemote)
        {
            if (hand == EnumHand.MAIN_HAND)
            {
                if (player.getHeldItem(hand).getItem() == Mappet.npcTool)
                {
                    if (player.isSneaking())
                    {
                        this.setDead();
                    }
                    else
                    {
                        Dispatcher.sendTo(new PacketNpcState(this.getEntityId(), this.state.serializeNBT()), (EntityPlayerMP) player);
                    }

                    return true;
                }

                this.state.triggerInteract.trigger(new DataContext(this, player));
            }
        }

        return true;
    }

    /* NBT (de)serialization */

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        tag.setTag("State", this.state.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        NpcState state = new NpcState();

        state.deserializeNBT(tag.getCompoundTag("State"));
        this.setState(state, false);

        if (tag.hasKey("NpcId"))
        {
            state.id = tag.getString("NpcId");
        }
    }

    /* Network (de)serialization */

    @Override
    public void writeSpawnData(ByteBuf buf)
    {
        MorphUtils.morphToBuf(buf, this.morph.get());
    }

    @Override
    public void readSpawnData(ByteBuf buf)
    {
        this.morph.setDirect(MorphUtils.morphFromBuf(buf));

        this.prevRotationYawHead = this.rotationYawHead;
        this.smoothYawHead = this.rotationYawHead;
    }

    /* Client stuff */

    /**
     * Is actor in range in render distance
     *
     * This method is responsible for checking if this entity is
     * available for rendering. Rendering range is configurable.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * RENDER_DISTANCE;

        return distance < d0 * d0;
    }
}