package mchorse.mappet.api.scripts.code.entities;

import mchorse.blockbuster.common.GunProps;
import mchorse.blockbuster.common.entity.EntityActor;
import mchorse.blockbuster.common.entity.EntityGunProjectile;
import mchorse.blockbuster.network.common.PacketModifyActor;
import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptFancyWorld;
import mchorse.mappet.api.scripts.code.ScriptRayTrace;
import mchorse.mappet.api.scripts.code.ScriptWorld;
import mchorse.mappet.api.scripts.code.entities.ai.EntitiesAIPatrol;
import mchorse.mappet.api.scripts.code.entities.ai.EntityAILookAtTarget;
import mchorse.mappet.api.scripts.code.entities.ai.repeatingCommand.EntityAIRepeatingCommand;
import mchorse.mappet.api.scripts.code.entities.ai.repeatingCommand.RepeatingCommandDataStorage;
import mchorse.mappet.api.scripts.code.entities.ai.rotations.EntityAIRotations;
import mchorse.mappet.api.scripts.code.entities.ai.rotations.RotationDataStorage;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.IScriptFancyWorld;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.data.ScriptBox;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.client.morphs.WorldMorph;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mappet.network.common.scripts.PacketWorldMorph;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mappet.utils.RunnableExecutionFork;
import mchorse.mclib.utils.Interpolation;
import mchorse.mclib.utils.RayTracing;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.Optional;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        else if (entity instanceof EntityItem)
        {
            return new ScriptEntityItem((EntityItem) entity);
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

    @Override
    public IScriptWorld getWorld()
    {
        return new ScriptWorld(this.entity.world);
    }

    @Override
    public IScriptFancyWorld getFancyWorld()
    {
        return new ScriptFancyWorld(this.entity.world);
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
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z))
        {
            throw new IllegalArgumentException();
        }

        this.entity.setPositionAndUpdate(x, y, z);
        //fix if multiple players were teleported to same position, they appear bugged to each other:
        if (this.entity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)entity).connection.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
        }
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
    public void addMotion(double x, double y, double z)
    {
        this.entity.velocityChanged = true;
        this.entity.addVelocity(x, y, z);
    }

    @Override
    public ScriptVector getRotations()
    {
        return new ScriptVector(this.getPitch(), this.getYaw(), this.getYawHead());
    }

    @Override
    public void setRotations(float pitch, float yaw, float yawHead)
    {
        if (Float.isNaN(pitch) || Float.isNaN(yaw) || Float.isNaN(yawHead))
        {
            throw new IllegalArgumentException();
        }

        this.entity.setLocationAndAngles(this.entity.posX, this.entity.posY, this.entity.posZ, yaw, pitch);
        this.entity.setRotationYawHead(yawHead);
        this.entity.setRenderYawOffset(yawHead);

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
        //this.entity.getLookVec() is not used because it does not work on entities that are not moving (but for players)
        //after many tests, this is the best way to get the most accurate look vector for all entities, whether they are moving or not
        float f1 = -((entity.rotationPitch) * ((float)Math.PI / 180F));
        float f2 = (entity.getRotationYawHead() * ((float)Math.PI / 180F));
        float f3 = -MathHelper.sin(f2);
        float f4 = MathHelper.cos(f2);
        float f6 = MathHelper.cos(f1);
        return new ScriptVector(f3 * f6, this.entity.getLookVec().y, f4 * f6);
    }

    @Override
    public float getEyeHeight()
    {
        return EntityUtils.getEyeHeight(this.entity);
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
    public void setMaxHp(float hp)
    {
        if (this.isLivingBase() && hp > 0.0F)
        {
            ((EntityLivingBase) this.entity).getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(hp);
        }
    }

    @Override
    public boolean isInWater()
    {
        return this.entity.isInWater();
    }

    @Override
    public boolean isInLava()
    {
        return this.entity.isInLava();
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

    @Override
    public boolean isOnGround()
    {
        return this.entity.onGround;
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

    @Override
    public void giveItem(IScriptItemStack stack)
    {
        this.giveItem(stack, true);
    }

    @Override
    public void giveItem(IScriptItemStack stack, boolean playSound)
    {
        if (stack == null || stack.isEmpty())
        {
            return;
        }

        if (this.isPlayer())
        {
            EntityPlayer player = (EntityPlayer) this.entity;
            ItemStack itemStack = stack.getMinecraftItemStack().copy();
            boolean flag = player.inventory.addItemStackToInventory(itemStack);

            if (flag)
            {
                if (playSound)
                {
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                }

                player.inventoryContainer.detectAndSendChanges();
            }

            EntityItem entityItem;

            if (flag && itemStack.isEmpty())
            {
                itemStack.setCount(1);
                player.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, stack.getCount());
                entityItem = player.dropItem(itemStack, false);

                if (entityItem != null)
                {
                    entityItem.makeFakeItem();
                }
            }
            else
            {
                player.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, stack.getCount() - itemStack.getCount());
                entityItem = player.dropItem(itemStack, false);

                if (entityItem != null)
                {
                    entityItem.setNoPickupDelay();
                    entityItem.setOwner(player.getName());
                }
            }
        }
        else if (this.isLivingBase())
        {
            if (this.entity instanceof EntityLivingBase)
            {
                EntityLivingBase living = (EntityLivingBase) this.entity;

                if (living.getHeldItemMainhand().isEmpty())
                {
                    living.setHeldItem(EnumHand.MAIN_HAND, stack.getMinecraftItemStack().copy());
                }
                else if (living.getHeldItemOffhand().isEmpty())
                {
                    living.setHeldItem(EnumHand.OFF_HAND, stack.getMinecraftItemStack().copy());
                }
                else
                {
                    living.entityDropItem(stack.getMinecraftItemStack().copy(), getEyeHeight());
                }
            }
        }
    }

    @Override
    public IScriptItemStack getHelmet()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).copy());
        }

        return null;
    }

    @Override
    public IScriptItemStack getChestplate()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST).copy());
        }

        return null;
    }

    @Override
    public IScriptItemStack getLeggings()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getItemStackFromSlot(EntityEquipmentSlot.LEGS).copy());
        }

        return null;
    }

    @Override
    public IScriptItemStack getBoots()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            return ScriptItemStack.create(((EntityLivingBase) this.entity).getItemStackFromSlot(EntityEquipmentSlot.FEET).copy());
        }

        return null;
    }

    @Override
    public void setHelmet(IScriptItemStack itemStack)
    {
        this.entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, itemStack.getMinecraftItemStack());
    }

    @Override
    public void setChestplate(IScriptItemStack itemStack)
    {
        this.entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, itemStack.getMinecraftItemStack());
    }

    @Override
    public void setLeggings(IScriptItemStack itemStack)
    {
        this.entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, itemStack.getMinecraftItemStack());
    }

    @Override
    public void setBoots(IScriptItemStack itemStack)
    {
        this.entity.setItemStackToSlot(EntityEquipmentSlot.FEET, itemStack.getMinecraftItemStack());
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

        if (this.entity instanceof EntityLiving && entity == null)
        {
            EntityLiving livingBase = (EntityLiving) this.entity;

            /* This should be enough, but it does not work most of the time for some reason. */
            livingBase.setAttackTarget(null);
            livingBase.setRevengeTarget(null);

            /* So I solved it by spawning an armor stand and making the entity target it and removing it after 1 tick. */
            String id = "minecraft:armor_stand";
            double x = this.entity.getPosition().getX();
            double y = this.entity.getPosition().getY() - 1;
            double z = this.entity.getPosition().getZ();
            String nbt = "{Marker:1b,NoGravity:1,Invisible:1b,CustomName:\"target_canceler\"}";
            NBTTagCompound tag = new NBTTagCompound();

            try
            {
                tag = JsonToNBT.getTagFromJson(nbt);
            }
            catch (Exception e)
            {
            }

            INBTCompound compound = new ScriptNBTCompound(tag);
            ScriptWorld world = new ScriptWorld(this.entity.world);

            IScriptEntity targetCanceller = world.spawnEntity(id, x, y, z, compound);
            livingBase.setAttackTarget((EntityLivingBase) targetCanceller.getMinecraftEntity());
            livingBase.setRevengeTarget((EntityLivingBase) targetCanceller.getMinecraftEntity());
            targetCanceller.remove();
        }
        else if (this.entity instanceof EntityLiving && entity.isLivingBase())
        {
            EntityLiving livingBase = (EntityLiving) this.entity;

            livingBase.setAttackTarget((EntityLivingBase) entity.getMinecraftEntity());
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
    public int getCombinedLight()
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(this.entity.posX), 0, MathHelper.floor(this.entity.posZ));

        if (this.entity.world.isBlockLoaded(pos))
        {
            pos.setY(MathHelper.floor(this.entity.posY + this.entity.getEyeHeight()));

            return this.entity.world.getCombinedLight(pos, 0);
        }

        return 0;
    }

    @Override
    public String getName()
    {
        return this.entity.getName();
    }

    @Override
    public void setName(String name)
    {
        this.entity.setCustomNameTag(name);

        if (name.isEmpty())
        {
            this.entity.setAlwaysRenderNameTag(false);
        }
        else
        {
            this.entity.setAlwaysRenderNameTag(true);
        }
    }

    @Override
    public void setInvisible(boolean invisible)
    {
        this.entity.setInvisible(invisible);
    }

    @Override
    public INBTCompound getFullData()
    {
        return new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void setFullData(INBTCompound data)
    {
        this.entity.readFromNBT(data.getNBTTagCompound());
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
    public boolean isItem()
    {
        return this.entity instanceof EntityItem;
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
    public boolean isEntityInRadius(IScriptEntity entity, double radius)
    {
        return this.entity.getDistanceSq(entity.getMinecraftEntity()) <= radius * radius;
    }

    @Override
    public boolean isInArea(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2).intersects(this.entity.getEntityBoundingBox());
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
        Entity target = this.entity;
        Entity attacker = entity.getMinecraftEntity();
        if (attacker instanceof EntityLivingBase)
        {
            EntityLivingBase attackerLiving = (EntityLivingBase) attacker;
            attackerLiving.setLastAttackedEntity(target);
            target.attackEntityFrom(DamageSource.causeIndirectDamage(attacker, attackerLiving), damage);
        }
    }

    @Override
    public void damageWithItemsAs(IScriptPlayer player)
    {
        player.getMinecraftPlayer().attackTargetEntityWithCurrentItem(this.entity);
    }

    @Override
    public void mount(IScriptEntity entity)
    {
        this.entity.startRiding(entity.getMinecraftEntity(), true);
    }

    @Override
    public void dismount()
    {
        this.entity.dismountRidingEntity();
    }

    @Override
    public IScriptEntity getMount()
    {
        return ScriptEntity.create(this.entity.getRidingEntity());
    }

    @Override
    public ScriptBox getBoundingBox()
    {
        AxisAlignedBB aabb = this.entity.getEntityBoundingBox();

        return new ScriptBox(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
    }

    private ScriptEntityItem dropItemInternal(ItemStack itemStack)
    {
        if (itemStack.isEmpty())
        {
            return null;
        }

        EntityItem entityItem = new EntityItem(
                this.entity.world,
                this.getPosition().x,
                this.getPosition().y + this.getEyeHeight(),
                this.getPosition().z,
                itemStack
        );

        entityItem.setPickupDelay(40);
        if (this.isNpc())
        {
            entityItem.setThrower(((EntityNpc) this.entity).getId());
        }
        else
        {
            entityItem.setThrower(this.entity.getName());
        }


        entityItem.velocityChanged = true;
        entityItem.addVelocity(getLook().x / 3, getLook().y / 3, getLook().z / 3);

        if (this.entity.world.spawnEntity(entityItem))
        {
            return new ScriptEntityItem(entityItem);
        }
        else
        {
            return null;
        }
    }

    @Override
    public ScriptEntityItem dropItem(int amount)
    {
        ItemStack heldItemStack = this.getMainItem().getMinecraftItemStack();

        if (heldItemStack.isEmpty())
        {
            return null;
        }

        int count = heldItemStack.getCount();

        if (amount > count)
        {
            amount = count;
        }

        ItemStack droppedStack = heldItemStack.copy();
        droppedStack.setCount(amount);

        heldItemStack.shrink(amount);

        return dropItemInternal(droppedStack);
    }

    @Override
    public ScriptEntityItem dropItem()
    {
        return dropItem(1);
    }

    @Override
    public ScriptEntityItem dropItem(IScriptItemStack scriptItemStack)
    {
        return dropItemInternal(scriptItemStack.getMinecraftItemStack());
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

    @Override
    public List<IScriptEntity> getLeashedEntities()
    {
        List<IScriptEntity> entities = new ArrayList<>();
        World world = this.entity.world;

        for (Entity entity : world.loadedEntityList)
        {
            if (entity instanceof EntityLiving)
            {
                EntityLiving entityLiving = (EntityLiving) entity;
                if (entityLiving.getLeashed() && entityLiving.getLeashHolder() == this.entity)
                {
                    entities.add(ScriptEntity.create(entityLiving));
                }
            }
        }

        return entities;
    }

    /* Modifiers */

    @Override
    public void setModifier(String modifierName, double value)
    {
        if (this.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.entity;
            UUID uuid = entityLivingBase.getUniqueID();
            IAttributeInstance attribute = entityLivingBase.getAttributeMap().getAttributeInstanceByName(modifierName);

            if (attribute == null)
            {
                return;
            }

            AttributeModifier modifier = new AttributeModifier(uuid, "script." + modifierName, value, 0);

            if (attribute.hasModifier(modifier))
            {
                attribute.removeModifier(modifier);
            }

            attribute.applyModifier(modifier);
        }
    }

    @Override
    public double getModifier(String modifierName)
    {
        if (this.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.entity;
            IAttributeInstance attribute = entityLivingBase.getAttributeMap().getAttributeInstanceByName(modifierName);

            if (attribute != null)
            {
                AttributeModifier modifier = attribute.getModifier(entityLivingBase.getUniqueID());

                return modifier == null ? 0 : modifier.getAmount();
            }
        }

        return 0;
    }

    @Override
    public void removeModifier(String modifierName)
    {
        if (this.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.entity;
            IAttributeInstance attribute = entityLivingBase.getAttributeMap().getAttributeInstanceByName(modifierName);

            if (attribute != null)
            {
                AttributeModifier modifier = attribute.getModifier(entityLivingBase.getUniqueID());

                if (modifier != null)
                {
                    attribute.removeModifier(modifier);
                }
            }
        }
    }

    @Override
    public void removeAllModifiers()
    {
        if (this.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.entity;

            for (IAttributeInstance attribute : entityLivingBase.getAttributeMap().getAllAttributes())
            {
                attribute.removeModifier(entityLivingBase.getUniqueID());
            }
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
    public AbstractMorph getMorph()
    {
        if (this.entity instanceof IMorphProvider)
        {
            return ((IMorphProvider) this.entity).getMorph();
        }

        return null;
    }

    @Override
    public boolean setMorph(AbstractMorph morph)
    {
        if (Loader.isModLoaded("blockbuster"))
        {
            return this.setActorsMorph(morph);
        }

        return false;
    }

    @Optional.Method(modid = "blockbuster")
    private boolean setActorsMorph(AbstractMorph morph)
    {
        if (this.entity instanceof EntityActor)
        {
            EntityActor actor = (EntityActor) this.entity;
            actor.morph.setDirect(morph);

            PacketModifyActor message = new PacketModifyActor(actor);
            mchorse.blockbuster.network.Dispatcher.sendToTracked(actor, message);

            return true;
        }

        return false;
    }

    @Override
    public void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch, boolean rotate, IScriptPlayer player)
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

        if (player == null)
        {
            Dispatcher.sendToTracked(this.entity, message);

            if (this.isPlayer())
            {
                Dispatcher.sendTo(message, (EntityPlayerMP) this.entity);
            }
        }
        else
        {
            Dispatcher.sendTo(message, player.getMinecraftPlayer());
        }
    }

    @Override
    public IScriptEntity shootBBGunProjectile(String gunPropsNBT)
    {
        if (this.entity instanceof EntityLivingBase && Loader.isModLoaded("blockbuster"))
        {
            try
            {
                return this.shootBBGunProjectileMethod(gunPropsNBT);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Optional.Method(modid = "blockbuster")
    private IScriptEntity shootBBGunProjectileMethod(String gunPropsNBT) throws NBTException
    {
        if (this.entity instanceof EntityLivingBase)
        {
            EntityLivingBase entityLivingBase = (EntityLivingBase) this.entity;
            NBTTagCompound gunPropsNBTCompound = JsonToNBT.getTagFromJson(gunPropsNBT).getCompoundTag("Gun");
            GunProps gunProps = new GunProps(gunPropsNBTCompound.getCompoundTag("Projectile"));
            gunProps.fromNBT(gunPropsNBTCompound);
            EntityGunProjectile projectile = new EntityGunProjectile(entityLivingBase.world, gunProps, gunProps.projectileMorph);

            projectile.setPosition(entityLivingBase.posX, (entityLivingBase.posY + 1.8), entityLivingBase.posZ);
            projectile.shoot(entityLivingBase, entityLivingBase.rotationPitch, entityLivingBase.getRotationYawHead(), 0, gunProps.speed, 0);
            projectile.setInitialMotion();
            entityLivingBase.world.spawnEntity(projectile);

            return ScriptEntity.create(projectile);
        }

        return null;
    }

    @Override
    public void executeCommand(String command)
    {
        this.entity.world.getMinecraftServer().getCommandManager().executeCommand((ICommandSender) this.entity, command);
    }

    @Override
    public void executeScript(String scriptName)
    {
        executeScript(scriptName, "main");
    }

    @Override
    public void executeScript(String scriptName, String function)
    {
        DataContext context = new DataContext(entity);
        try
        {
            Mappet.scripts.execute(scriptName, function, context);
        }
        catch (ScriptException e)
        {
            String fileName = e.getFileName() == null ? scriptName : e.getFileName();

            e.printStackTrace();
            throw new RuntimeException("Script Error: " + fileName + " - Line: " + e.getLineNumber() + " - Column: " + e.getColumnNumber() + " - Message: " + e.getMessage(), e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Script Empty: " + scriptName + " - Error: " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void lockPosition(double x, double y, double z)
    {
        this.entity.getEntityData().setBoolean("positionLocked", true);
        this.entity.getEntityData().setDouble("lockX", x);
        this.entity.getEntityData().setDouble("lockY", y);
        this.entity.getEntityData().setDouble("lockZ", z);
    }

    @Override
    public void unlockPosition()
    {
        this.entity.getEntityData().setBoolean("positionLocked", false);
    }

    @Override
    public boolean isPositionLocked()
    {
        return this.entity.getEntityData().getBoolean("positionLocked");
    }

    @Override
    public void lockRotation(float yaw, float pitch, float yawHead)
    {
        if (this.entity instanceof EntityPlayer)
        {
            return;
        }
        this.entity.getEntityData().setBoolean("rotationLocked", true);
        this.entity.getEntityData().setFloat("lockYaw", yaw);
        this.entity.getEntityData().setFloat("lockPitch", pitch);
        this.entity.getEntityData().setFloat("lockYawHead", yawHead);
    }

    @Override
    public void unlockRotation()
    {
        if (this.entity instanceof EntityPlayer)
        {
            return;
        }
        this.entity.getEntityData().setBoolean("rotationLocked", false);
    }

    @Override
    public boolean isRotationLocked()
    {
        return this.entity.getEntityData().getBoolean("rotationLocked");
    }

    @Override
    public void moveTo(String interpolation, int durationTicks, double x, double y, double z, boolean disableAI)
    {
        if (disableAI)
        {
            this.setAIEnabled(false);
            this.moveTo(interpolation, durationTicks, x, y, z);
            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(durationTicks, () -> this.setAIEnabled(true)));
        }
        else
        {
            this.moveTo(interpolation, durationTicks, x, y, z);
        }
    }

    private void moveTo(String interpolation, int durationTicks, double x, double y, double z)
    {
        Interpolation interp = Interpolation.valueOf(interpolation.toUpperCase());
        double startX = this.entity.posX;
        double startY = this.entity.posY;
        double startZ = this.entity.posZ;

        for (int i = 0; i < durationTicks; i++)
        {
            double progress = (double) i / (double) durationTicks;
            double interpX = interp.interpolate(startX, x, progress);
            double interpY = interp.interpolate(startY, y, progress);
            double interpZ = interp.interpolate(startZ, z, progress);

            CommonProxy.eventHandler.addExecutable(new RunnableExecutionFork(i, () -> this.setPosition(interpX, interpY, interpZ)));
        }
    }

    /* Entity AI */

    @Override
    public void observe(IScriptEntity entity)
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;

            if (entity == null)
            {
                EntityAITasks.EntityAITaskEntry taskToRemove = null;

                for (EntityAITasks.EntityAITaskEntry task : entityLiving.tasks.taskEntries)
                {
                    if (task.action instanceof EntityAILookAtTarget)
                    {
                        taskToRemove = task;
                        break;
                    }
                }

                if (taskToRemove != null)
                {
                    entityLiving.tasks.removeTask(taskToRemove.action);
                }
            }
            else
            {
                entityLiving.tasks.addTask(8, new EntityAILookAtTarget(entityLiving, entity.getMinecraftEntity(), 1.0F));
            }
        }
    }

    public IScriptEntity getObservedEntity()
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;

            for (EntityAITasks.EntityAITaskEntry task : entityLiving.tasks.taskEntries)
            {
                Entity target = null;

                if (task.action instanceof EntityAILookAtTarget)
                {
                    EntityAILookAtTarget lookAtTask = (EntityAILookAtTarget) task.action;
                    target = lookAtTask.getTarget();
                }
                else if (task.action instanceof EntityAIWatchClosest)
                {
                    EntityAIWatchClosest watchClosestTask = (EntityAIWatchClosest) task.action;
                    target = getEntityFromWatchClosest(watchClosestTask);
                }

                if (target != null)
                {
                    return ScriptEntity.create(target);
                }
            }
        }

        return null;
    }

    private Entity getEntityFromWatchClosest(EntityAIWatchClosest watchClosestTask)
    {
        Entity target = null;
        try
        {
            target = ObfuscationReflectionHelper.getPrivateValue(EntityAIWatchClosest.class, watchClosestTask, "field_75334_a", "closestEntity");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return target;
    }


    @Override
    public void addEntityPatrol(double x, double y, double z, double speed, boolean shouldCirculate, String executeCommandOnArrival)
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;
            EntityAITasks.EntityAITaskEntry taskToRemove = findEntitiesAIPatrolTask(entityLiving);
            EntitiesAIPatrol patrolTask;

            if (taskToRemove != null)
            {
                patrolTask = (EntitiesAIPatrol) taskToRemove.action;
                entityLiving.tasks.removeTask(patrolTask);

                patrolTask.addPatrolPoint(new BlockPos(x, y, z), shouldCirculate, executeCommandOnArrival);
            }
            else
            {
                patrolTask = new EntitiesAIPatrol((EntityLiving) entity, speed, new BlockPos[] {new BlockPos(x, y, z)}, new boolean[] {shouldCirculate}, new String[] {executeCommandOnArrival});
            }

            entityLiving.tasks.addTask(1, patrolTask);
        }
    }

    @Override
    public void clearEntityPatrols()
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;
            EntityAITasks.EntityAITaskEntry taskToRemove = findEntitiesAIPatrolTask(entityLiving);

            if (taskToRemove != null)
            {
                entityLiving.tasks.removeTask(taskToRemove.action);
            }
        }
    }

    private EntityAITasks.EntityAITaskEntry findEntitiesAIPatrolTask(EntityLiving entityLiving)
    {
        for (EntityAITasks.EntityAITaskEntry task : entityLiving.tasks.taskEntries)
        {
            if (task.action instanceof EntitiesAIPatrol)
            {
                return task;
            }
        }
        return null;
    }

    @Override
    public void setRotationsAI(float yaw, float pitch, float yawHead)
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;
            EntityAITasks.EntityAITaskEntry taskToRemove = removeTaskIfExists(entityLiving, EntityAIRotations.class);
            entityLiving.tasks.addTask(9, new EntityAIRotations(entityLiving, yaw, pitch, yawHead, 1.0F));

            RotationDataStorage.getRotationDataStorage(entity.world).addRotationData(entityLiving.getUniqueID(), yaw, pitch, yawHead);
        }
    }

    @Override
    public void clearRotationsAI()
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;

            removeTaskIfExists(entityLiving, EntityAIRotations.class);
            RotationDataStorage.getRotationDataStorage(entity.world).removeRotationData(entityLiving.getUniqueID());
        }
    }

    private EntityAITasks.EntityAITaskEntry removeTaskIfExists(EntityLiving entityLiving, Class<?> taskClass)
    {
        EntityAITasks.EntityAITaskEntry taskToRemove = null;

        for (EntityAITasks.EntityAITaskEntry task : entityLiving.tasks.taskEntries)
        {
            if (task.action.getClass().equals(taskClass))
            {
                taskToRemove = task;
                break;
            }
        }

        if (taskToRemove != null)
        {
            entityLiving.tasks.removeTask(taskToRemove.action);
        }

        return taskToRemove;
    }


    @Override
    public void executeRepeatingCommand(String command, int frequency)
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;
            entityLiving.tasks.addTask(10, new EntityAIRepeatingCommand(entityLiving, command, frequency));
            RepeatingCommandDataStorage.getRepeatingCommandDataStorage(entity.world).addRepeatingCommandData(entityLiving.getUniqueID(), command, frequency);
        }
    }

    @Override
    public void clearAllRepeatingCommands()
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;

            removeTaskIfExists(entityLiving, EntityAIRepeatingCommand.class);
            RepeatingCommandDataStorage.getRepeatingCommandDataStorage(entity.world).removeRepeatingCommandData(entityLiving.getUniqueID());
        }
    }

    @Override
    public void removeRepeatingCommand(String command)
    {
        if (this.entity instanceof EntityLiving)
        {
            EntityLiving entityLiving = (EntityLiving) this.entity;

            removeSpecificRepeatingCommandTaskIfExists(entityLiving, command);
            RepeatingCommandDataStorage.getRepeatingCommandDataStorage(entity.world).removeSpecificRepeatingCommandData(entityLiving.getUniqueID(), command);
        }
    }

    private void removeSpecificRepeatingCommandTaskIfExists(EntityLiving entityLiving, String command)
    {
        List<EntityAITasks.EntityAITaskEntry> tasksToRemove = new ArrayList<>();

        for (EntityAITasks.EntityAITaskEntry task : entityLiving.tasks.taskEntries)
        {
            if (task.action instanceof EntityAIRepeatingCommand && ((EntityAIRepeatingCommand) task.action).getCommand().equals(command))
            {
                tasksToRemove.add(task);
            }
        }
        for (EntityAITasks.EntityAITaskEntry taskToRemove : tasksToRemove)
        {
            entityLiving.tasks.removeTask(taskToRemove.action);
        }
    }
}
