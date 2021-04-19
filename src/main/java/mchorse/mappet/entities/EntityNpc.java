package mchorse.mappet.entities;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcDrop;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.ai.EntityAIFollowTarget;
import mchorse.mappet.entities.ai.EntityAIPatrol;
import mchorse.mappet.entities.ai.EntityAIReturnToPost;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcMorph;
import mchorse.mclib.utils.Interpolations;
import mchorse.mclib.utils.MathUtils;
import mchorse.metamorph.api.Morph;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
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
    private String npcId = "";
    private boolean unique;
    private double pathDistance = 16;

    private int lastDamageTime;

    public float smoothYawHead;
    public float prevSmoothYawHead;

    public EntityNpc(World worldIn)
    {
        super(worldIn);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
    }

    @Override
    protected void initEntityAI()
    {
        super.initEntityAI();

        this.tasks.taskEntries.clear();

        if (this.state != null)
        {
            if (this.state.canSwim)
            {
                this.tasks.addTask(0, new EntityAISwimming(this));
            }

            if (!this.state.follow.isEmpty())
            {
                this.tasks.addTask(6, new EntityAIFollowTarget(this, 1, 2, 10));
            }
            else if (this.state.hasPost && this.state.postPosition != null)
            {
                this.tasks.addTask(6, new EntityAIReturnToPost(this, this.state.postPosition, 0.5F, this.state.postRadius));
            }
            else if (!this.state.patrol.isEmpty())
            {
                this.tasks.addTask(6, new EntityAIPatrol(this, 0.5F));
            }

            if (this.state.lookAround)
            {
                this.tasks.addTask(8, new EntityAILookIdle(this));
            }

            if (this.state.lookAtPlayer)
            {
                this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
            }
        }

        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.25D));
    }

    /* Getter and setters */

    public void setNpc(String id, Npc npc, NpcState state)
    {
        this.npcId = id;
        this.unique = npc.unique;
        this.pathDistance = npc.pathDistance;

        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(this.pathDistance);
        this.navigator = this.createNavigator(this.world);

        this.setState(state, false);
    }

    public String getId()
    {
        return this.npcId;
    }

    public NpcState getState()
    {
        return this.state;
    }

    public void setState(NpcState state, boolean notify)
    {
        this.state = new NpcState();
        this.state.deserializeNBT(state.serializeNBT());

        double max = this.getMaxHealth();
        double health = this.getHealth();

        /* Set health */
        this.setMaxHealth(state.maxHealth);
        this.setHealth((float) MathHelper.clamp(state.maxHealth * (health / max), 1, state.maxHealth));

        this.isImmuneToFire = !this.state.canGetBurned;

        this.morph.set(state.morph);

        if (notify)
        {
            Dispatcher.sendToTracked(this, new PacketNpcMorph(this));
        }

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
                return this.world.getPlayerEntityByUUID(UUID.fromString(this.state.follow));
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
        super.onUpdate();

        if (!this.morph.isEmpty())
        {
            this.morph.get().update(this);
        }

        if (this.state.regenDelay > 0)
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
    protected void damageEntity(DamageSource damage, float damageAmount)
    {
        super.damageEntity(damage, damageAmount);

        if (!this.isEntityInvulnerable(damage))
        {
            this.lastDamageTime = 0;
        }
    }

    @Override
    protected boolean canDespawn()
    {
        return this.unique;
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
        if (!this.state.canFallDamage && source == DamageSource.FALL)
        {
            return true;
        }

        return super.isEntityInvulnerable(source);
    }

    /* NBT (de)serialization */

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        if (!this.morph.isEmpty())
        {
            tag.setTag("Morph", this.morph.get().toNBT());
        }

        tag.setString("NpcId", this.npcId);
        tag.setTag("State", this.state.serializeNBT());
        tag.setBoolean("Unique", this.unique);
        tag.setDouble("PathDistance", this.pathDistance);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        if (tag.hasKey("Morph"))
        {
            this.morph.fromNBT(tag.getCompoundTag("Morph"));
        }

        NpcState state = new NpcState();

        state.deserializeNBT(tag.getCompoundTag("State"));

        this.npcId = tag.getString("NpcId");
        this.setState(state, false);
        this.unique = tag.getBoolean("Unique");

        if (tag.hasKey("PathDistance"))
        {
            this.pathDistance = tag.getDouble("PathDistance");
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