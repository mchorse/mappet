package mchorse.mappet.api.npcs;

import mchorse.mappet.utils.NBTUtils;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class NpcState implements INBTSerializable<NBTTagCompound>
{
    /* Health */

    /**
     * Max health an NPC can have
     */
    public float maxHealth = 20;

    /**
     * The initial health
     */
    public float health = 20;

    /**
     * Regeneration delay (in ticks)
     */
    public int regenDelay = 80;

    /**
     * How frequently will an NPC regenerate one heart (in ticks)
     */
    public int regenFrequency = 20;

    /* Faction (coming soon...) */

    /* Damage */

    /**
     * What is this NPC's melee damage
     */
    public float damage = 2F;

    /**
     * Whether this NPC can use ranged weapons
     */
    public boolean canRanged;

    /**
     * Whether this NPC is resistible to fall damage
     */
    public boolean canFallDamage = true;

    /**
     * Whether this NPC is resistible to fire damage
     */
    public boolean canGetBurned = true;

    /**
     * Whether this NPC can be damaged
     */
    public boolean invincible;

    /**
     * If false, then NPCs can be killed only by a command.
     * Regardless of the state, killable allows to make this NPC damaged
     * until 0 health.
     */
    public boolean killable = true;

    /* Movement */

    /**
     * What is NPC's movement speed
     */
    public float speed = 0.1F;

    /**
     * Can NPC walk on the ground
     */
    public boolean canWalk = true;

    /**
     * Can NPC move around in the water
     */
    public boolean canSwim = true;

    /**
     * Can NPC move around on Y axis, i.e. fly
     */
    public boolean canFly;

    /**
     * Whether post options
     */
    public boolean hasPost;

    /**
     * Block position where the NPC must stay on the post
     */
    public BlockPos postPosition;

    /**
     * What distance away from the post will NPC will return into the
     * radius. This could be used if wandering option wandered NPC out of
     * the radius
     */
    public float postRadius = 1F;

    /**
     * After what distance from the post or patrol point the NPC will
     * stop chasing and come back
     */
    public float fallback = 30F;

    /**
     * Whether patrol points should circulate rather than ping-pong
     */
    public boolean patrolCirculate;

    /**
     * List of position through which an NPC must patrol through
     */
    public List<BlockPos> patrol = new ArrayList<BlockPos>();

    /**
     * The UUID of the player that must be followed
     */
    public String follow = "";

    /* General */

    /**
     * How does the NPC looks like
     */
    public AbstractMorph morph;

    /**
     * Sight distance, is how far away can an NPC find a target to attack
     */
    public float sightDistance = 25F;

    /**
     * Sight radius determines the angle of how narrow does the NPC has to look
     * in the direction of potential targets
     */
    public float sightRadius = 120F;

    /**
     * What kind of items will the NPC is going to drop
     */
    public List<NpcDrop> drops = new ArrayList<NpcDrop>();

    /**
     * How much XP drops an NPC after getting killed
     */
    public int xp = 0;

    /* Behavior */

    /**
     * Whether NPC looks at a player when it
     */
    public boolean lookAtPlayer;

    /**
     * Whether NPC pointlessly looks around in its idle state
     */
    public boolean lookAround;

    /**
     * NPCs can go around in different direction, this property
     * sets its maximum distance it can wander off at a time
     */
    public boolean wander;

    /**
     * The health threshold until NPC starts to run away
     */
    public float flee = 4F;

    @Override
    public NBTTagCompound serializeNBT()
    {
        return this.partialSerializeNBT(null);
    }

    public NBTTagCompound partialSerializeNBT(List<String> options)
    {
        boolean all = options == null;

        NBTTagCompound tag = new NBTTagCompound();

        if (!all && options.isEmpty())
        {
            return tag;
        }

        /* Health */
        if (all || options.contains("max_health")) tag.setFloat("MaxHealth", this.maxHealth);
        if (all || options.contains("health")) tag.setFloat("Health", this.health);
        if (all || options.contains("regen_delay")) tag.setInteger("RegenDelay", this.regenDelay);
        if (all || options.contains("regen_frequency")) tag.setInteger("RegenFrequency", this.regenFrequency);

        /* Damage */
        if (all || options.contains("damage")) tag.setFloat("Damage", this.damage);
        if (all || options.contains("can_ranged")) tag.setBoolean("CanRanged", this.canRanged);
        if (all || options.contains("can_fall_damage")) tag.setBoolean("CanFallDamage", this.canFallDamage);
        if (all || options.contains("can_get_burned")) tag.setBoolean("CanGetBurned", this.canGetBurned);
        if (all || options.contains("invincible")) tag.setBoolean("Invincible", this.invincible);
        if (all || options.contains("killable")) tag.setBoolean("Killable", this.killable);

        /* Movement */
        if (all || options.contains("speed")) tag.setFloat("Speed", this.speed);
        if (all || options.contains("can_walk")) tag.setBoolean("CanWalk", this.canWalk);
        if (all || options.contains("can_swim")) tag.setBoolean("CanSwim", this.canSwim);
        if (all || options.contains("can_fly")) tag.setBoolean("CanFly", this.canFly);
        if (all || options.contains("has_post")) tag.setBoolean("HasPost", this.hasPost);
        if ((all || options.contains("post")) && this.postPosition != null)
        {
            tag.setTag("Post", NBTUtils.blockPosTo(this.postPosition));
        }
        if (all || options.contains("post_radius")) tag.setFloat("PostRadius", this.postRadius);
        if (all || options.contains("fallback")) tag.setFloat("Fallback", this.fallback);
        if (all || options.contains("patrol_circulate")) tag.setBoolean("PatrolCirculate", this.patrolCirculate);
        if ((all || options.contains("patrol")) && !this.patrol.isEmpty())
        {
            NBTTagList points = new NBTTagList();

            for (BlockPos pos : this.patrol)
            {
                points.appendTag(NBTUtils.blockPosTo(pos));
            }

            tag.setTag("Patrol", points);
        }
        if ((all || options.contains("follow"))) tag.setString("Follow", this.follow);

        /* General */
        if ((all || options.contains("morph")) && this.morph != null) tag.setTag("Morph", this.morph.toNBT());
        if (all || options.contains("sight_distance")) tag.setFloat("SightDistance", this.sightDistance);
        if (all || options.contains("sight_radius")) tag.setFloat("SightRadius", this.sightRadius);
        if ((all || options.contains("drops")) && !this.drops.isEmpty())
        {
            NBTTagList drops = new NBTTagList();

            for (NpcDrop drop : this.drops)
            {
                drops.appendTag(drop.serializeNBT());
            }

            tag.setTag("Drops", drops);
        }
        if (all || options.contains("xp")) tag.setInteger("Xp", this.xp);

        /* Behavior */
        if (all || options.contains("look_at_player")) tag.setBoolean("LookAtPlayer", this.lookAtPlayer);
        if (all || options.contains("look_around")) tag.setBoolean("LookAround", this.lookAround);
        if (all || options.contains("wander")) tag.setBoolean("Wander", this.wander);
        if (all || options.contains("flee")) tag.setFloat("Flee", this.flee);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        /* Health */
        if (tag.hasKey("MaxHealth")) this.maxHealth = tag.getFloat("MaxHealth");
        if (tag.hasKey("Health")) this.health = tag.getFloat("Health");
        if (tag.hasKey("RegenDelay")) this.regenDelay = tag.getInteger("RegenDelay");
        if (tag.hasKey("RegenFrequency")) this.regenFrequency = tag.getInteger("RegenFrequency");

        /* Damage */
        if (tag.hasKey("Damage")) this.damage = tag.getFloat("Damage");
        if (tag.hasKey("CanRanged")) this.canRanged = tag.getBoolean("CanRanged");
        if (tag.hasKey("CanFallDamage")) this.canFallDamage = tag.getBoolean("CanFallDamage");
        if (tag.hasKey("CanGetBurned")) this.canGetBurned = tag.getBoolean("CanGetBurned");
        if (tag.hasKey("Invincible")) this.invincible = tag.getBoolean("Invincible");
        if (tag.hasKey("Killable")) this.killable = tag.getBoolean("Killable");

        /* Movement */
        if (tag.hasKey("Speed")) this.speed = tag.getFloat("Speed");
        if (tag.hasKey("CanWalk")) this.canWalk = tag.getBoolean("CanWalk");
        if (tag.hasKey("CanSwim")) this.canSwim = tag.getBoolean("CanSwim");
        if (tag.hasKey("CanFly")) this.canFly = tag.getBoolean("CanFly");
        if (tag.hasKey("HasPost")) this.hasPost = tag.getBoolean("HasPost");
        if (tag.hasKey("Post", Constants.NBT.TAG_LIST))
        {
            this.postPosition = NBTUtils.blockPosFrom(tag.getTag("Post"));
        }
        if (tag.hasKey("PostRadius")) this.postRadius = tag.getFloat("PostRadius");
        if (tag.hasKey("Fallback")) this.fallback = tag.getFloat("Fallback");
        if (tag.hasKey("PatrolCirculate")) this.patrolCirculate = tag.getBoolean("PatrolCirculate");
        if (tag.hasKey("Patrol", Constants.NBT.TAG_LIST))
        {
            NBTTagList points = tag.getTagList("Patrol", Constants.NBT.TAG_LIST);

            this.patrol.clear();

            for (int i = 0; i < points.tagCount(); i++)
            {
                BlockPos pos = NBTUtils.blockPosFrom(points.get(i));

                if (pos != null)
                {
                    this.patrol.add(pos);
                }
            }
        }
        if (tag.hasKey("Follow", Constants.NBT.TAG_STRING)) this.follow = tag.getString("Follow");

        /* General */
        if (tag.hasKey("Morph", Constants.NBT.TAG_COMPOUND)) this.morph = MorphManager.INSTANCE.morphFromNBT(tag.getCompoundTag("Morph"));
        if (tag.hasKey("SightDistance")) this.sightDistance = tag.getFloat("SightDistance");
        if (tag.hasKey("SightRadius")) this.sightRadius = tag.getFloat("SightRadius");
        if (tag.hasKey("Drops"))
        {
            NBTTagList drops = tag.getTagList("Drops", Constants.NBT.TAG_COMPOUND);

            this.drops.clear();

            for (int i = 0; i < drops.tagCount(); i++)
            {
                NBTTagCompound tagDrop = drops.getCompoundTagAt(i);
                NpcDrop drop = new NpcDrop();

                drop.deserializeNBT(tagDrop);

                if (drop.chance <= 0 || drop.stack.isEmpty())
                {
                    continue;
                }

                this.drops.add(drop);
            }
        }
        if (tag.hasKey("Xp")) this.xp = tag.getInteger("Xp");

        /* Behavior */
        if (tag.hasKey("LookAtPlayer")) this.lookAtPlayer = tag.getBoolean("LookAtPlayer");
        if (tag.hasKey("LookAround")) this.lookAround = tag.getBoolean("LookAround");
        if (tag.hasKey("Wander")) this.wander = tag.getBoolean("Wander");
        if (tag.hasKey("Flee")) this.flee = tag.getFloat("Flee");
    }
}
