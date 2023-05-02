package mchorse.mappet.api.npcs;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.utils.NBTUtils;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class NpcState implements INBTSerializable<NBTTagCompound>
{
    /* Meta */
    public String stateName = "";

    public String id = "";

    public States states = new States();

    /**
     * Unique
     */
    public boolean unique;

    /**
     * Path distance
     */
    public float pathDistance = 32;

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

    /* Damage */

    /**
     * What is this NPC's melee damage
     */
    public float damage = 2F;

    /**
     * NPC attack delay
     */
    public int damageDelay = 10;

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
    public float speed = 1F;

    /**
     * NPC's jumping power when it's steered
     */
    public float jumpPower = 0.5F;

    /**
     * Can NPC move around in the water
     */
    public boolean canSwim = true;

    /**
     * Can NPC be pushed by players moving through an NPC
     */
    public boolean immovable;

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
    public float fallback = 15F;

    /**
     * Whether patrol points should circulate rather than ping-pong
     */
    public boolean patrolCirculate;

    /**
     * List of position through which an NPC must patrol through
     */
    public List<BlockPos> patrol = new ArrayList<BlockPos>();

    /**
     *  List of triggers on each patrol point
     */
    public List<Trigger> patrolTriggers = new ArrayList<Trigger>();

    /**
     * The UUID of the player that must be followed
     */
    public String follow = "";

    /* General */

    /**
     * To which faction does this NPC belongs to
     */
    public String faction = "";

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

    /**
     * NPC shadow size
     */
    public float shadowSize = 0.6F;

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

    /**
     * NPC will pick up items if this option is enabled and Gamerule canPickupItems is true
     */
    public boolean canPickUpLoot;

    /**
     * Whether NPC has gravity or not
     */
    public boolean hasNoGravity = false;

    /**
     * Whether NPC can be steered by a player
     */
    public boolean canBeSteered = false;

    /* Triggers */

    public Trigger triggerDied = new Trigger();
    public Trigger triggerDamaged = new Trigger();
    public Trigger triggerInteract = new Trigger();
    public Trigger triggerTick = new Trigger();
    public Trigger triggerTarget = new Trigger();
    public Trigger triggerInitialize = new Trigger();
    public Trigger triggerRespawn = new Trigger();
    public Trigger triggerEntityCollision = new Trigger();

    /* Respawn */

    /**
     * Is NPC respawns with provided delay
     */
    public boolean respawn;

    /**
     * Delay to respawn
     */
    public int respawnDelay;

    /**
     * Whether NPC respawns at the specified coordinates
     */
    public boolean respawnOnCoordinates;

    /**
     * X coordinate to respawn
     */
    public double respawnPosX;

    /**
     * Y coordinate to respawn
     */
    public double respawnPosY;

    /**
     * Z coordinate to respawn
     */
    public double respawnPosZ;

    /**
     * Whether NPC respawns with same UUID
     */
    public boolean respawnSaveUUID;

    /**
     * I should've used my Value API for this, but now it's too late
     */
    public boolean edit(String property, String value)
    {
        /* Meta */
        if (property.equals("id"))
        {
            this.id = value;
        }
        else if (property.equals("unique"))
        {
            this.unique = Boolean.parseBoolean(value);
        }
        else if (property.equals("state_name"))
        {
            this.stateName = value;
        }
        else if (property.equals("path_distance"))
        {
            this.pathDistance = Float.parseFloat(value);
        }
        /* Health */
        else if (property.equals("max_health"))
        {
            this.maxHealth = Float.parseFloat(value);
        }
        else if (property.equals("health"))
        {
            this.health = Float.parseFloat(value);
        }
        else if (property.equals("regen_delay"))
        {
            this.regenDelay = Integer.parseInt(value);
        }
        else if (property.equals("regen_frequency"))
        {
            this.regenFrequency = Integer.parseInt(value);
        }
        /* Damage */
        else if (property.equals("damage"))
        {
            this.damage = Float.parseFloat(value);
        }
        else if (property.equals("damage_delay"))
        {
            this.damageDelay = Integer.parseInt(value);
        }
        else if (property.equals("can_ranged"))
        {
            this.canRanged = Boolean.parseBoolean(value);
        }
        else if (property.equals("can_fall_damage"))
        {
            this.canFallDamage = Boolean.parseBoolean(value);
        }
        else if (property.equals("can_get_burned"))
        {
            this.canGetBurned = Boolean.parseBoolean(value);
        }
        else if (property.equals("invincible"))
        {
            this.invincible = Boolean.parseBoolean(value);
        }
        else if (property.equals("killable"))
        {
            this.killable = Boolean.parseBoolean(value);
        }
        /* Movement */
        else if (property.equals("speed"))
        {
            this.speed = Float.parseFloat(value);
        }
        else if (property.equals("jump_power"))
        {
            this.jumpPower = Float.parseFloat(value);
        }
        else if (property.equals("can_swim"))
        {
            this.canSwim = Boolean.parseBoolean(value);
        }
        else if (property.equals("immovable"))
        {
            this.immovable = Boolean.parseBoolean(value);
        }
        else if (property.equals("has_post"))
        {
            this.hasPost = Boolean.parseBoolean(value);
        }
        else if (property.equals("post"))
        {
            String[] splits = value.split(" ");

            if (splits.length >= 3)
            {
                int x = Integer.parseInt(splits[0]);
                int y = Integer.parseInt(splits[1]);
                int z = Integer.parseInt(splits[2]);

                this.postPosition = new BlockPos(x, y, z);
            }
            else
            {
                return false;
            }
        }
        else if (property.equals("fallback"))
        {
            this.fallback = Float.parseFloat(value);
        }
        else if (property.equals("patrol_circulate"))
        {
            this.patrolCirculate = Boolean.parseBoolean(value);
        }
        else if (property.equals("follow"))
        {
            this.follow = value;
        }
        /* General */
        else if (property.equals("morph"))
        {
            try
            {
                this.morph = MorphManager.INSTANCE.morphFromNBT(JsonToNBT.getTagFromJson(value));
            }
            catch (Exception e)
            {
                return false;
            }
        }
        else if (property.equals("xp"))
        {
            this.xp = Integer.parseInt(value);
        }
        else if (property.equals("has_no_gravity"))
        {
            this.hasNoGravity = Boolean.parseBoolean(value);
        }
        else if (property.equals("can_be_steered"))
        {
            this.canBeSteered = Boolean.parseBoolean(value);
        }
        else if (property.equals("shadow_size"))
        {
            this.shadowSize = Float.parseFloat(value);
        }
        /* Behavior */
        else if (property.equals("look_at_player"))
        {
            this.lookAtPlayer = Boolean.parseBoolean(value);
        }
        else if (property.equals("look_around"))
        {
            this.lookAround = Boolean.parseBoolean(value);
        }
        else if (property.equals("wander"))
        {
            this.wander = Boolean.parseBoolean(value);
        }
        else if (property.equals("flee"))
        {
            this.flee = Float.parseFloat(value);
        }
        else if (property.equals("can_pick_up_loot"))
        {
            this.canPickUpLoot = Boolean.parseBoolean(value);
        }
        else if (property.equals("respawn"))
        {
            this.respawn = Boolean.parseBoolean(value);
        }
        else if (property.equals("respawn_delay"))
        {
            this.respawnDelay = Integer.parseInt(value);
        }
        else if (property.equals("respawn_on_coordinates"))
        {
            this.respawnOnCoordinates = Boolean.parseBoolean(value);
        }
        else if (property.equals("respawn_pos_x"))
        {
            this.respawnPosX = Double.parseDouble(value);
        }
        else if (property.equals("respawn_pos_y"))
        {
            this.respawnPosY = Double.parseDouble(value);
        }
        else if (property.equals("respawn_pos_z"))
        {
            this.respawnPosZ = Double.parseDouble(value);
        }
        else if (property.equals("respawn_save_uuid"))
        {
            this.respawnSaveUUID = Boolean.parseBoolean(value);
        }
        else
        {
            return false;
        }

        return true;
    }

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

        /* Meta */
        if (all || options.contains("id")) tag.setString("Id", this.id);
        if (all || options.contains("states")) tag.setTag("States", this.states.serializeNBT());
        if (all || options.contains("unique")) tag.setBoolean("Unique", this.unique);
        if (all || options.contains("path_distance")) tag.setFloat("PathDistance", this.pathDistance);
        if (all || options.contains("state_name")) tag.setString("StateName", this.stateName);

        /* Health */
        if (all || options.contains("max_health")) tag.setFloat("MaxHealth", this.maxHealth);
        if (all || options.contains("health")) tag.setFloat("Health", this.health);
        if (all || options.contains("regen_delay")) tag.setInteger("RegenDelay", this.regenDelay);
        if (all || options.contains("regen_frequency")) tag.setInteger("RegenFrequency", this.regenFrequency);

        /* Damage */
        if (all || options.contains("damage")) tag.setFloat("Damage", this.damage);
        if (all || options.contains("damage_delay")) tag.setInteger("DamageDelay", this.damageDelay);
        if (all || options.contains("can_ranged")) tag.setBoolean("CanRanged", this.canRanged);
        if (all || options.contains("can_fall_damage")) tag.setBoolean("CanFallDamage", this.canFallDamage);
        if (all || options.contains("can_get_burned")) tag.setBoolean("CanGetBurned", this.canGetBurned);
        if (all || options.contains("invincible")) tag.setBoolean("Invincible", this.invincible);
        if (all || options.contains("killable")) tag.setBoolean("Killable", this.killable);

        /* Movement */
        if (all || options.contains("speed")) tag.setFloat("Speed", this.speed);
        if (all || options.contains("jump_power")) tag.setFloat("JumpPower", this.jumpPower);
        if (all || options.contains("can_swim")) tag.setBoolean("CanSwim", this.canSwim);
        if (all || options.contains("immovable")) tag.setBoolean("Immovable", this.immovable);
        if (all || options.contains("has_post")) tag.setBoolean("HasPost", this.hasPost);
        if ((all || options.contains("post")))
        {
            tag.setTag("Post", this.postPosition == null ? new NBTTagList() : NBTUtils.blockPosTo(this.postPosition));
        }
        if (all || options.contains("post_radius")) tag.setFloat("PostRadius", this.postRadius);
        if (all || options.contains("fallback")) tag.setFloat("Fallback", this.fallback);
        if (all || options.contains("patrol_circulate")) tag.setBoolean("PatrolCirculate", this.patrolCirculate);
        if ((all || options.contains("patrol")))
        {
            NBTTagList points = new NBTTagList();
            NBTTagList triggers = new NBTTagList();

            for (int i = 0; i < this.patrol.size(); i++)
            {
                points.appendTag(NBTUtils.blockPosTo(this.patrol.get(i)));
            }

            for (int i = 0; i < this.patrolTriggers.size(); i++)
            {
                triggers.appendTag(this.patrolTriggers.get(i).serializeNBT());
            }

            tag.setTag("Patrol", points);
            tag.setTag("PatrolTriggers", triggers);
        }
        if ((all || options.contains("follow"))) tag.setString("Follow", this.follow);

        /* General */
        if (all || options.contains("faction")) tag.setString("Faction", this.faction);
        if ((all || options.contains("morph"))) tag.setTag("Morph", this.morph == null ? new NBTTagCompound() : this.morph.toNBT());
        if (all || options.contains("sight_distance")) tag.setFloat("SightDistance", this.sightDistance);
        if (all || options.contains("sight_radius")) tag.setFloat("SightRadius", this.sightRadius);
        if ((all || options.contains("drops")))
        {
            NBTTagList drops = new NBTTagList();

            for (NpcDrop drop : this.drops)
            {
                drops.appendTag(drop.serializeNBT());
            }

            tag.setTag("Drops", drops);
        }
        if (all || options.contains("xp")) tag.setInteger("Xp", this.xp);
        if (all || options.contains("has_no_gravity")) tag.setBoolean("HasNoGravity", this.hasNoGravity);
        if (all || options.contains("can_be_steered")) tag.setBoolean("CanBeSteered", this.canBeSteered);
        if (all || options.contains("shadow_size")) tag.setFloat("ShadowSize", this.shadowSize);

        /* Behavior */
        if (all || options.contains("look_at_player")) tag.setBoolean("LookAtPlayer", this.lookAtPlayer);
        if (all || options.contains("look_around")) tag.setBoolean("LookAround", this.lookAround);
        if (all || options.contains("wander")) tag.setBoolean("Wander", this.wander);
        if (all || options.contains("flee")) tag.setFloat("Flee", this.flee);
        if (all || options.contains("can_pick_up_loot")) tag.setBoolean("CanPickUpLoot", this.canPickUpLoot);

        /* Triggers */
        if (all || options.contains("trigger_died")) tag.setTag("TriggerDied", this.triggerDied.serializeNBT());
        if (all || options.contains("trigger_damaged")) tag.setTag("TriggerDamaged", this.triggerDamaged.serializeNBT());
        if (all || options.contains("trigger_interact")) tag.setTag("TriggerInteract", this.triggerInteract.serializeNBT());
        if (all || options.contains("trigger_tick")) tag.setTag("TriggerTick", this.triggerTick.serializeNBT());
        if (all || options.contains("trigger_target")) tag.setTag("TriggerTarget", this.triggerTarget.serializeNBT());
        if (all || options.contains("trigger_initialize")) tag.setTag("TriggerInitialize", this.triggerInitialize.serializeNBT());
        if (all || options.contains("trigger_respawn")) tag.setTag("TriggerRespawn", this.triggerRespawn.serializeNBT());
        if (all || options.contains("trigger_entity_collision")) tag.setTag("TriggerEntityCollision", this.triggerEntityCollision.serializeNBT());

        /* Respawn */
        if (all || options.contains("respawn")) tag.setBoolean("Respawn", this.respawn);
        if (all || options.contains("respawn_delay")) tag.setInteger("RespawnDelay", this.respawnDelay);
        if (all || options.contains("respawn_on_coordinates")) tag.setBoolean("RespawnOnCoordinates", this.respawnOnCoordinates);
        if (all || options.contains("respawn_pos_x")) tag.setDouble("RespawnPosX", this.respawnPosX);
        if (all || options.contains("respawn_pos_y")) tag.setDouble("RespawnPosY", this.respawnPosY);
        if (all || options.contains("respawn_pos_z")) tag.setDouble("RespawnPosZ", this.respawnPosZ);
        if (all || options.contains("respawn_save_uuid")) tag.setBoolean("RespawnSaveUUID", this.respawnSaveUUID);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        /* Meta */
        if (tag.hasKey("Id")) this.id = tag.getString("Id");
        if (tag.hasKey("States")) this.states.deserializeNBT(tag.getCompoundTag("States"));
        if (tag.hasKey("Unique")) this.unique = tag.getBoolean("Unique");
        if (tag.hasKey("PathDistance")) this.pathDistance = tag.getFloat("PathDistance");
        if (tag.hasKey("StateName")) this.stateName = tag.getString("StateName");

        /* Health */
        if (tag.hasKey("MaxHealth")) this.maxHealth = tag.getFloat("MaxHealth");
        if (tag.hasKey("Health")) this.health = tag.getFloat("Health");
        if (tag.hasKey("RegenDelay")) this.regenDelay = tag.getInteger("RegenDelay");
        if (tag.hasKey("RegenFrequency")) this.regenFrequency = tag.getInteger("RegenFrequency");

        /* Damage */
        if (tag.hasKey("Damage")) this.damage = tag.getFloat("Damage");
        if (tag.hasKey("DamageDelay")) this.damageDelay = tag.getInteger("DamageDelay");
        if (tag.hasKey("CanRanged")) this.canRanged = tag.getBoolean("CanRanged");
        if (tag.hasKey("CanFallDamage")) this.canFallDamage = tag.getBoolean("CanFallDamage");
        if (tag.hasKey("CanGetBurned")) this.canGetBurned = tag.getBoolean("CanGetBurned");
        if (tag.hasKey("Invincible")) this.invincible = tag.getBoolean("Invincible");
        if (tag.hasKey("Killable")) this.killable = tag.getBoolean("Killable");

        /* Movement */
        if (tag.hasKey("Speed")) this.speed = tag.getFloat("Speed");
        if (tag.hasKey("JumpPower")) this.jumpPower = tag.getFloat("JumpPower");
        if (tag.hasKey("CanSwim")) this.canSwim = tag.getBoolean("CanSwim");
        if (tag.hasKey("Immovable")) this.immovable = tag.getBoolean("Immovable");
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

            NBTTagList triggers = tag.getTagList("PatrolTriggers", Constants.NBT.TAG_COMPOUND);

            this.patrolTriggers.clear();

            for (int i = 0; i < triggers.tagCount(); i++)
            {
                Trigger trigger = new Trigger();
                trigger.deserializeNBT(triggers.getCompoundTagAt(i));

                this.patrolTriggers.add(trigger);
            }
        }
        if (tag.hasKey("Follow", Constants.NBT.TAG_STRING)) this.follow = tag.getString("Follow");

        /* General */
        if (tag.hasKey("Faction")) this.faction = tag.getString("Faction");
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
        if (tag.hasKey("HasNoGravity")) this.hasNoGravity = tag.getBoolean("HasNoGravity");
        if (tag.hasKey("CanBeSteered")) this.canBeSteered = tag.getBoolean("CanBeSteered");
        if (tag.hasKey("ShadowSize")) this.shadowSize = tag.getFloat("ShadowSize");

        /* Behavior */
        if (tag.hasKey("LookAtPlayer")) this.lookAtPlayer = tag.getBoolean("LookAtPlayer");
        if (tag.hasKey("LookAround")) this.lookAround = tag.getBoolean("LookAround");
        if (tag.hasKey("Wander")) this.wander = tag.getBoolean("Wander");
        if (tag.hasKey("Flee")) this.flee = tag.getFloat("Flee");
        if (tag.hasKey("CanPickUpLoot")) this.canPickUpLoot = tag.getBoolean("CanPickUpLoot");

        /* Triggers */
        if (tag.hasKey("TriggerDied")) this.triggerDied.deserializeNBT(tag.getCompoundTag("TriggerDied"));
        if (tag.hasKey("TriggerDamaged")) this.triggerDamaged.deserializeNBT(tag.getCompoundTag("TriggerDamaged"));
        if (tag.hasKey("TriggerInteract")) this.triggerInteract.deserializeNBT(tag.getCompoundTag("TriggerInteract"));
        if (tag.hasKey("TriggerTick")) this.triggerTick.deserializeNBT(tag.getCompoundTag("TriggerTick"));
        if (tag.hasKey("TriggerTarget")) this.triggerTarget.deserializeNBT(tag.getCompoundTag("TriggerTarget"));
        if (tag.hasKey("TriggerInitialize")) this.triggerInitialize.deserializeNBT(tag.getCompoundTag("TriggerInitialize"));
        if (tag.hasKey("TriggerRespawn")) this.triggerRespawn.deserializeNBT(tag.getCompoundTag("TriggerRespawn"));
        if (tag.hasKey("TriggerEntityCollision")) this.triggerEntityCollision.deserializeNBT(tag.getCompoundTag("TriggerEntityCollision"));

        /* Respawn */
        if (tag.hasKey("Respawn")) this.respawn = tag.getBoolean("Respawn");
        if (tag.hasKey("RespawnDelay")) this.respawnDelay = tag.getInteger("RespawnDelay");
        if (tag.hasKey("RespawnOnCoordinates")) this.respawnOnCoordinates = tag.getBoolean("RespawnOnCoordinates");
        if (tag.hasKey("RespawnPosX")) this.respawnPosX = tag.getDouble("RespawnPosX");
        if (tag.hasKey("RespawnPosY")) this.respawnPosY = tag.getDouble("RespawnPosY");
        if (tag.hasKey("RespawnPosZ")) this.respawnPosZ = tag.getDouble("RespawnPosZ");
        if (tag.hasKey("RespawnSaveUUID")) this.respawnSaveUUID = tag.getBoolean("RespawnSaveUUID");
    }

    public void writeToBuf(ByteBuf buf) {
        buf.writeFloat(shadowSize);
        buf.writeFloat(jumpPower);
    }

    public void readFromBuf(ByteBuf buf) {
        shadowSize = buf.readFloat();
        jumpPower = buf.readFloat();
    }
}