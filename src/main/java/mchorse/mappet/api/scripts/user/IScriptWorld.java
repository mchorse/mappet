package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.code.mappet.blocks.MappetBlockBBModel;
import mchorse.mappet.api.scripts.code.mappet.blocks.MappetBlockConditionModel;
import mchorse.mappet.api.scripts.code.mappet.blocks.MappetBlockRegion;
import mchorse.mappet.api.scripts.code.mappet.MappetSchematic;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.blocks.IScriptTileEntity;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptEntityItem;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetSchematic;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.List;

/**
 * This interface represent a world passed in the event.
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var world = c.getWorld();
 *
 *        // Do something with world...
 *    }
 * }</pre>
 */
public interface IScriptWorld
{
    /**
     * Get Minecraft world instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var seaLevel = c.getWorld().getMinecraftWorld().field_181546_a //Sea level
     *
     *     c.send(seaLevel)
     * }
     * }</pre>
     */
    public World getMinecraftWorld();

    /**
     * Set a block at XYZ, use {@link IScriptFactory#createBlockState(String, int)}
     * to get the block state.
     *
     * <pre>{@code
     *    var coarse_dirt = mappet.createBlockState("minecraft:dirt", 1);
     *
     *    c.getWorld().setBlock(coarse_dirt, 214, 3, 509);
     * }</pre>
     */
    public void setBlock(IScriptBlockState state, int x, int y, int z);

    /**
     * Remove a block at given XYZ.
     *
     * <pre>{@code
     *   c.getWorld().removeBlock(214, 3, 509);
     * }</pre>
     */
    public void removeBlock(int x, int y, int z);

    /**
     * Get block state at given XYZ.
     *
     * <pre>{@code
     *    var block = c.getWorld().getBlock(214, 3, 509);
     *
     *    c.send("Block at (214, 3, 509) is " + block.getBlockId());
     * }</pre>
     *
     * @return a block state at given XYZ, or null if the chunk isn't loaded
     */
    public IScriptBlockState getBlock(int x, int y, int z);

    /**
     * Get block state at given XYZ.
     *
     * <pre>{@code
     *    var block = c.getWorld().getBlock(mappet.vector3(214, 3, 509));
     *
     *    c.send("Block at (214, 3, 509) is " + block.getBlockId());
     * }</pre>
     *
     * @return a block state at given XYZ, or null if the chunk isn't loaded
     */
    public IScriptBlockState getBlock(ScriptVector pos);

    /**
     * Whether a tile entity is present at given XYZ.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var hasTileEntity = c.getWorld().hasTileEntity(0, 80, 0)
     *
     *     c.send(hasTileEntity)
     * }
     * }</pre>
     */
    public boolean hasTileEntity(int x, int y, int z);

    /**
     * Replace all blocks in the given area with the given block state.
     *
     * <pre>{@code
     * c.getWorld().replaceBlocks(
     *    mappet.createBlockState("minecraft:dirt", 0),
     *    mappet.createBlockState("minecraft:dirt", 1),
     *    mappet.vector3(214, 3, 509),
     *    mappet.vector3(214, 3, 509)
     * );
     * }</pre>
     */
    public void replaceBlocks(IScriptBlockState blockToBeReplaced, IScriptBlockState newBlock, Vector3d pos1, Vector3d pos2);

    /**
     * Replace all blocks in the given area with the given block state and tile entity data.
     *
     * <pre>{@code
     * c.getWorld().replaceBlocks(
     *    mappet.createBlockState("minecraft:dirt", 0),
     *    mappet.createBlockState("blockbuster:model", 0),
     *    mappet.createCompound('{Morph:{Settings:{Hands:1b},Name:"blockbuster.fred"},id:"minecraft:blockbuster_model_tile_entity"}'),
     *    mappet.vector3(171, 61, 279),
     *    mappet.vector3(176, 64, 276)
     * );
     * }</pre>
     */
    public void replaceBlocks(IScriptBlockState blockToBeReplaced, IScriptBlockState newBlock, INBTCompound tileData, Vector3d pos1, Vector3d pos2);

    /**
     * Get tile entity at given XYZ.
     *
     * <pre>{@code
     *    var tile = c.getWorld().getTileEntity(214, 3, 509);
     *
     *    if (tile)
     *    {
     *        c.send("Tile entity at (214, 3, 509) is " + tile.getId());
     *    }
     *    else
     *    {
     *        c.send("There is no tile entity at (214, 3, 509)");
     *    }
     * }</pre>
     */
    public IScriptTileEntity getTileEntity(int x, int y, int z);

    /**
     * Check whether there is an inventory tile entity at given XYZ.
     *
     * <pre>{@code
     *    var world = c.getWorld();
     *
     *    if (world.hasInventory(214, 4, 512))
     *    {
     *        var inventory = world.getInventory(214, 4, 512);
     *
     *        inventory.setStack(0, mappet.createItem("minecraft:diamond_hoe"));
     *        c.send("There is a surprise for you in chest at (214, 4, 512) :)");
     *    }
     *    else
     *    {
     *        c.send("There is no chest at (214, 4, 512)...");
     *    }
     * }</pre>
     */
    public boolean hasInventory(int x, int y, int z);

    /**
     * Get inventory tile entity at given XYZ.
     *
     * <pre>{@code
     *    var world = c.getWorld();
     *    var inventory = world.getInventory(214, 4, 512);
     *
     *    if (world.hasInventory(214, 4, 512))
     *    {
     *        var inventory = world.getInventory(214, 4, 512);
     *
     *        inventory.clear();
     *    }
     * }</pre>
     *
     * @return an inventory at given XYZ, or <code>null</code> if an inventory tile entity isn't present.
     */
    public IScriptInventory getInventory(int x, int y, int z);

    /**
     * Check whether it's raining in the world.
     *
     * <pre>{@code
     *    var world = c.getWorld();
     *    var pos = c.getSubject().getPosition();
     *
     *    // If it's raining in the world, then drop a diamond
     *    // If not, then drop a dirt block
     *    if (world.isRaining())
     *    {
     *        world.dropItemStack(mappet.createItem("minecraft:diamond"), pos.x, pos.y + 3, pos.z);
     *    }
     *    else
     *    {
     *        world.dropItemStack(mappet.createItem("minecraft:dirt"), pos.x, pos.y + 3, pos.z);
     *    }
     * }</pre>
     */
    public boolean isRaining();

    /**
     * Set raining state.
     *
     * <pre>{@code
     *    c.getWorld().setRaining(true);
     *    c.send("The ritual dance got successfully completed!");
     * }</pre>
     */
    public void setRaining(boolean raining);

    /**
     * Get current time of day (the one that is set by <code>/time set</code> command).
     *
     * <pre>{@code
     *    if (c.getWorld().getTime() % 24000 > 12000)
     *    {
     *        c.getSubject().send("Good night!");
     *    }
     *    else
     *    {
     *        c.getSubject().send("Good day!");
     *    }
     * }</pre>
     */
    public long getTime();

    /**
     * Set current time of day.
     *
     * <pre>{@code
     *    c.getWorld().setTime(14000);
     *    c.send("Another ritual dance got successfully completed!");
     * }</pre>
     */
    public void setTime(long time);

    /**
     * Get total time that this world existed for (in ticks).
     *
     * <pre>{@code
     *    if (c.getWorld().getTotalTime() > 20 * 600)
     *    {
     *        c.send("You had only 10 minutes to complete the map...");
     *        c.send("Initiating SELF-DESTRUCT mode!");
     *
     *        // TODO: implement self-destruction
     *    }
     * }</pre>
     */
    public long getTotalTime();

    /**
     * Get world's dimension ID.
     *
     * <pre>{@code
     *    if (c.getWorld().getDimensionId() == 0)
     *    {
     *        c.getSubject().send("You're in overworld!");
     *    }
     *    else
     *    {
     *        c.getSubject().send("*shrugs*");
     *    }
     * }</pre>
     */
    public int getDimensionId();

    /**
     * Spawn vanilla particles.
     *
     * <pre>{@code
     *    var explode = mappet.getParticleType("explode");
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnParticles(explode, false, pos.x, pos.y, pos.z, 10, 0.1, 0.1, 0.1, 0.1);
     * }</pre>
     *
     * @param type Particle type, you can use {@link IScriptFactory#getParticleType(String)}
     * to get the desired particle type.
     * @param longDistance Whether particles should be spawned regardless of the distance
     * @param x X coordinate of position where particles should be spawned
     * @param y Y coordinate of position where particles should be spawned
     * @param z Z coordinate of position where particles should be spawned
     * @param n How many particles of given type should be spawned
     * @param dx X random offset that shift particle relative to X coordinate where it spawned
     * @param dy Y random offset that shift particle relative to Y coordinate where it spawned
     * @param dz Z random offset that shift particle relative to Z coordinate where it spawned
     * @param speed The speed of particle, different particles might use this argument differently
     * @param args Additional arguments that can be passed into a particle, for example for
     * "iconcrack" particle you can pass item numeric ID to spawn item particle for
     * a specific item (F3 + H shows the numeric ID of an item)
     */
    public void spawnParticles(EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args);

    /**
     * Spawn vanilla particles only to a specific player.
     *
     * <pre>{@code
     *    var explode = mappet.getParticleType("explode");
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnParticles(c.getSubject(), explode, false, pos.x, pos.y, pos.z, 10, 0.1, 0.1, 0.1, 0.1);
     * }</pre>
     *
     * @param player The player that you want to limit seeing the particle only to
     * @param type Particle type, you can use {@link IScriptFactory#getParticleType(String)}
     * to get the desired particle type.
     * @param longDistance Whether particles should be spawned regardless of the distance
     * @param x X coordinate of position where particles should be spawned
     * @param y Y coordinate of position where particles should be spawned
     * @param z Z coordinate of position where particles should be spawned
     * @param n How many particles of given type should be spawned
     * @param dx X random offset that shift particle relative to X coordinate where it spawned
     * @param dy Y random offset that shift particle relative to Y coordinate where it spawned
     * @param dz Z random offset that shift particle relative to Z coordinate where it spawned
     * @param speed The speed of particle, different particles might use this argument differently
     * @param args Additional arguments that can be passed into a particle, for example for
     * "iconcrack" particle you can pass item numeric ID to spawn item particle for
     * a specific item (F3 + H shows the numeric ID of an item)
     */
    public void spawnParticles(IScriptPlayer player, EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args);

    /**
     * Spawn an entity at given position.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    // Make an explosion at player's feet
     *    c.getWorld().spawnEntity("minecraft:tnt", pos.x, pos.y, pos.z);
     * }</pre>
     */
    public default IScriptEntity spawnEntity(String id, double x, double y, double z)
    {
        return this.spawnEntity(id, x, y, z, null);
    }

    /**
     * Spawn an entity at given position with additional data.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    // Spawn a baby zombie
     *    c.getWorld().spawnEntity("minecraft:zombie", pos.x, pos.y + 3, pos.z, mappet.createCompound("{IsBaby:1b}"));
     * }</pre>
     */
    public IScriptEntity spawnEntity(String id, double x, double y, double z, INBTCompound compound);

    /**
     * Spawn an NPC at given position with default state.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnNpc("herobrine", pos.x, pos.y, pos.z);
     * }</pre>
     */
    public default IScriptNpc spawnNpc(String id, double x, double y, double z)
    {
        return this.spawnNpc(id, "default", x, y, z);
    }

    /**
     * Spawn an NPC at given position with given state.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnNpc("herobrine", "dabbing", pos.x, pos.y, pos.z);
     * }</pre>
     */
    public IScriptNpc spawnNpc(String id, String state, double x, double y, double z);

    /**
     * Spawn an NPC at given position with given state and rotation.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnNpc("herobrine", "dabbing", pos.x, pos.y, pos.z, 0, 0, 0);
     * }</pre>
     */
    IScriptNpc spawnNpc(String id, String state, double x, double y, double z, float yaw, float pitch, float headYaw);

    /**
     * Get entities within the box specified by given coordinates in this world.
     * This method limits to scanning entities only within <b>100 blocks</b>
     * in any direction. If the box provided has any of its sizes that is longer
     * than 100 blocks, then it will simply return an empty list.
     *
     * <pre>{@code
     *    // Y position is at the feet, while X and Z is at center
     *    var pos = c.getSubject().getPosition();
     *    var entities = c.getWorld().getEntities(pos.x - 2, pos.y - 1, pos.z - 2, pos.x + 2, pos.y + 3, pos.z + 2);
     *
     *    for (var i in entities)
     *    {
     *        var entity = entities[i];
     *
     *        if (!entity.isSame(c.getSubject()))
     *        {
     *            entity.damage(2.0);
     *        }
     *    }
     * }</pre>
     */
    public List<IScriptEntity> getEntities(double x1, double y1, double z1, double x2, double y2, double z2);

    /**
     * Get entities within the box specified by given coordinates in this world ignoring the volume limit.
     * This method does not limit to scanning entities only within <b>100 blocks</b>
     *
     * <pre>{@code
     *    // Y position is at the feet, while X and Z is at center
     *    var pos = c.getSubject().getPosition();
     *    var entities = c.getWorld().getEntities(pos.x - 30, pos.y - 30, pos.z - 30, pos.x + 30, pos.y + 30, pos.z + 30, true);
     *
     *    for (var i in entities)
     *    {
     *        var entity = entities[i];
     *
     *        if (!entity.isSame(c.getSubject()))
     *        {
     *            entity.damage(2.0);
     *        }
     *    }
     * }</pre>
     */
    public List<IScriptEntity> getEntities(double x1, double y1, double z1, double x2, double y2, double z2, boolean ignoreVolumeLimit);

    /**
     * Get entities within the sphere specified by given coordinates and radius in
     * this world. This method limits to scanning entities only within <b>50 blocks
     * radius</b> in any direction. If the sphere provided has the radius that is
     * longer than 100 blocks, then it will simply return an empty list.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *    var entities = c.getWorld().getEntities(pos.x, pos.y + 1, pos.z, 3);
     *
     *    for (var i in entities)
     *    {
     *        var entity = entities[i];
     *
     *        if (!entity.isSame(c.getSubject()))
     *        {
     *            entity.damage(2.0);
     *        }
     *    }
     * }</pre>
     */
    public List<IScriptEntity> getEntities(double x, double y, double z, double radius);

    /**
     * Play a sound event in the world.
     *
     * <p>For all possible sound event IDs, please refer to either <code>/playsound</code>
     * command, or script editor's sound picker.</p>
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().playSound("minecraft:entity.pig.ambient", pos.x, pos.y, pos.z);
     * }</pre>
     */
    public default void playSound(String event, double x, double y, double z)
    {
        this.playSound(event, x, y, z, 1F, 1F);
    }

    /**
     * Play a sound event in the world with volume and pitch.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().playSound("minecraft:entity.pig.ambient", pos.x, pos.y, pos.z, 1.0, 0.8);
     * }</pre>
     */
    public void playSound(String event, double x, double y, double z, float volume, float pitch);

    /**
     * Stop all playing sound events for every player.
     *
     * <pre>{@code
     *    c.getWorld().stopAllSounds();
     * }</pre>
     */
    public default void stopAllSounds()
    {
        this.stopSound("", "");
    }

    /**
     * Stop specific sound event for every player.
     *
     * <pre>{@code
     *    c.getWorld().stopSound("minecraft:entity.pig.ambient");
     * }</pre>
     */
    public default void stopSound(String event)
    {
        this.stopSound(event, "");
    }

    /**
     * <p>Stop specific sound event in given sound category for every player.</p>
     *
     * <p>For list of sound categories, type into chat
     * <code>/playsound minecraft:entity.pig.ambient</code>, press space, and press
     * Tab key. The list of sounds categories will be displayed.</p>
     *
     * <pre>{@code
     *    c.getWorld().stopSound("minecraft:entity.pig.ambient", "master");
     * }</pre>
     */
    public void stopSound(String event, String category);

    /**
     * Drop item stack at given XYZ position with no velocity applied.
     *
     * <pre>{@code
     *    var item = mappet.createItem("minecraft:diamond_hoe");
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     * }</pre>
     */
    public default IScriptEntityItem dropItemStack(IScriptItemStack stack, double x, double y, double z)
    {
        return this.dropItemStack(stack, x, y, z, 0, 0, 0);
    }

    /**
     * Drop an item stack at given XYZ position in this world with desired velocity.
     *
     * <pre>{@code
     *    var item = mappet.createItem("minecraft:diamond_hoe");
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z, 0, 1, 0);
     * }</pre>
     */
    public IScriptEntityItem dropItemStack(IScriptItemStack stack, double x, double y, double z, double mx, double my, double mz);

    /**
     * Make an explosion in this world at given coordinates, and distance that
     * destroys blocks, damages entities but not places fire. See {@link IScriptWorld#explode(IScriptEntity, double, double, double, float, boolean, boolean)}
     * for more thorough definition of arguments.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var pos = s.getPosition();
     *
     *     c.getWorld().explode(pos.x, pos.y, pos.z, 10)
     * }
     * }</pre>
     */
    public default void explode(double x, double y, double z, float distance)
    {
        this.explode(null, x, y, z, distance, false, true);
    }

    /**
     * Make an explosion in this world at given coordinates, and distance with
     * options to place fire and destroy blocks. See {@link IScriptWorld#explode(IScriptEntity, double, double, double, float, boolean, boolean)}
     * for more thorough definition of arguments.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var pos = s.getPosition();
     *
     *     c.getWorld().explode(pos.x, pos.y, pos.z, 10, false, false)
     * }
     * }</pre>
     */
    public default void explode(double x, double y, double z, float distance, boolean blazeGround, boolean destroyTerrain)
    {
        this.explode(null, x, y, z, distance, blazeGround, destroyTerrain);
    }

    /**
     * Make an explosion in this world at given coordinates, distance, and entity
     * that caused the explosion.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var pos = s.getPosition();
     *
     *     c.getWorld().explode(s, pos.x, pos.y, pos.z, 10, false, false)
     * }
     * }</pre>
     *
     * @param exploder Entity that causes explosion that won't receive damage from it.
     * @param x X coordinate in the world at which explosion must be caused.
     * @param y Y coordinate in the world at which explosion must be caused.
     * @param z Z coordinate in the world at which explosion must be caused.
     * @param distance Radius (in blocks of the explosion).
     * @param blazeGround Whether fire blocks should be placed randomly on top of remaining blocks.
     * @param destroyTerrain Whether blocks should be destroyed by the explosion.
     */
    public void explode(IScriptEntity exploder, double x, double y, double z, float distance, boolean blazeGround, boolean destroyTerrain);

    /**
     * Ray trace in this world, between two given points (including any entity intersection).
     * Check {@link IScriptRayTrace} for an example.
     *
     * @param x1 X coordinate of the first point.
     * @param y1 Y coordinate of the first point.
     * @param z1 Z coordinate of the first point.
     * @param x2 X coordinate of the second point.
     * @param y2 Y coordinate of the second point.
     * @param z2 Z coordinate of the second point.
     */
    public IScriptRayTrace rayTrace(double x1, double y1, double z1, double x2, double y2, double z2);

    /**
     * Ray trace in this world, between two given points (excluding entities).
     * Check {@link IScriptRayTrace} for an example.
     *
     * @param x1 X coordinate of the first point.
     * @param y1 Y coordinate of the first point.
     * @param z1 Z coordinate of the first point.
     * @param x2 X coordinate of the second point.
     * @param y2 Y coordinate of the second point.
     * @param z2 Z coordinate of the second point.
     */
    public IScriptRayTrace rayTraceBlock(double x1, double y1, double z1, double x2, double y2, double z2);


    /**
     * Return whether a button, plate or lever is active or not.
     *
     * <pre>{@code
     *     c.getWorld().isActive(0, 4, 0);
     * }</pre>
     */
    public boolean isActive(int x, int y, int z);

    /**
     * Test for a specific block and meta in a specific coordinates.
     *
     * <pre>{@code
     *     function main(c)
     *     {
     *         var pos = c.getSubject().getPosition()
     *
     *         if (c.getWorld().testForBlock(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z), "minecraft:light_weighted_pressure_plate", 1))
     *         {
     *             c.send("Prussure Plate is pressed.")
     *         }
     *     }
     * }</pre>
     */
    public boolean testForBlock(int x, int y, int z, String blockId, int meta);

    /**
     * Fill a 3D area with a block.
     *
     * <pre>{@code
     *     var coarse_dirt = mappet.createBlockState("minecraft:dirt", 1);
     *
     *     c.getWorld().fill(coarse_dirt, -3, 100, -3, 3, 100, 3);
     * }</pre>
     *
     * @param state The block to fill the area with.
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param z1 The first z coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @param z2 The second z coordinate.
     */
    public void fill(IScriptBlockState state, int x1, int y1, int z1, int x2, int y2, int z2);

    /**
     * Summon a falling block with a specific block id and meta.
     *
     * <pre>{@code
     *     c.getWorld().summonFallingBlock(0, 100, 0, "minecraft:dirt", 1);
     * }</pre>
     *
     * @return The falling block entity.
     */
    public IScriptEntity summonFallingBlock(double x, double y, double z, String blockId, int meta);

    /**
     * Transform a block to a falling block in specific coordinates.
     *
     * <pre>{@code
     *     c.getWorld().setFallingBlock(0, 100, 0);
     * }</pre>
     *
     * @return The falling block entity.
     */
    public IScriptEntity setFallingBlock(int x, int y, int z);

    /**
     * Sets a tile entity.
     *
     * <pre>{@code
     * c.getWorld().setTileEntity(
     *     530, 152, 546,
     *     mappet.createBlockState("blockbuster:model", 0),
     *     mappet.createCompound('{Morph:{Settings:{Hands:1b},Name:"blockbuster.fred"},id:"minecraft:blockbuster_model_tile_entity"}')
     * );
     *   }</pre>
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     */
    public void setTileEntity(int x, int y, int z, IScriptBlockState blockState, INBTCompound tileData);

    /**
     * Fills a range with tile entities.
     *
     * <pre>{@code
     * c.getWorld().fillTileEntities(530, 152, 546, mappet.createBlockState("blockbuster:model", 0), mappet.createCompound('{Morph:{Settings:{Hands:1b},Name:"blockbuster.fred"},id:"minecraft:blockbuster_model_tile_entity"}'));
     *   }</pre>
     *
     * @param x1 X coordinate
     * @param y1 Y coordinate
     * @param z1 Z coordinate
     * @param x2 X coordinate
     * @param y2 Y coordinate
     * @param z2 Z coordinate
     */
    public void fillTileEntities(int x1, int y1, int z1, int x2, int y2, int z2, IScriptBlockState blockState, INBTCompound tileData);

    /**
     * Clones am area to another area.
     *
     * <pre>{@code
     * c.getWorld().clone(0, 100, 0, 3, 100, 3, 0, 101, 0);
     * }</pre>
     *
     * @param x1 The first x coordinate.
     * @param y1 The first y coordinate.
     * @param z1 The first z coordinate.
     * @param x2 The second x coordinate.
     * @param y2 The second y coordinate.
     * @param z2 The second z coordinate.
     * @param xNew The new x coordinate.
     * @param yNew The new y coordinate.
     * @param zNew The new z coordinate.
     */
    public void clone(int x1, int y1, int z1, int x2, int y2, int z2, int xNew, int yNew, int zNew);

    /**
     * Clones the block to another coordinates.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     c.getWorld().clone(0, 80, 0, 0, 83, 0)
     * }
     * }</pre>
     */
    public void clone(int x, int y, int z, int xNew, int yNew, int zNew);


    /**
     * Gets the block stack at given position, including tile entity data.
     *
     * <pre>{@code
     *    var x= 0, y = 100, z = 0;
     *    var world = c.getWorld();
     *    var blockItemStack = world.getBlockStackWithTile(x, y, z);
     *    world.setBlock(mappet.createBlockState("minecraft:air", 0), x, y, z)
     *    world.dropItemStack(blockItemStack, x+0.5, y+0.5, z+0.5);
     * }</pre>
     */
    public IScriptItemStack getBlockStackWithTile(int x, int y, int z);

    /* Mappet stuff */

    /**
     * Returns a new empty Schematic object.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var schematic = c.world.createSchematic();
     *     schematic.loadFromWorld(0, 4, 0, 4, 8, 4).saveToFile("mySchematic").place(0, 4, 4).place(0, 4, 8);
     * }
     * }</pre>
     *
     * @return {@link IMappetSchematic}
     */
    public MappetSchematic createSchematic();

    /**
     * Display a world morph to all players around 64 blocks away from given point.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var morph = mappet.createMorph('{Name:"item"}');
     *     var pos = s.getPosition();
     *
     *     c.getWorld().displayMorph(morph, 100, pos.x, pos.y + s.getHeight() + 0.5, pos.z);
     * }
     * }</pre>
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z)
    {
        this.displayMorph(morph, expiration, x, y, z, 0, 0);
    }

    /**
     * Display a world morph to all players at given point.
     *
     * <pre>{@code
     *    var s = c.getSubject();
     *    var morph = mappet.createMorph('{Name:"item"}');
     *    var pos = s.getPosition();
     *
     *    // This will display a diamond hoe morph on top of the
     *    // player's head (but it won't track player's movement)
     *    c.getWorld().displayMorph(morph, 100, pos.x, pos.y + s.getHeight() + 0.5, pos.z, 64);
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param range How many blocks far away will this send to players around given point.
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, int range)
    {
        this.displayMorph(morph, expiration, x, y, z, 0, 0, range);
    }

    /**
     * Display a world morph to all players at given point with rotation.
     *
     * <pre>{@code
     *    var s = c.getSubject();
     *    var morph = mappet.createMorph('{Name:"item"}');
     *    var pos = s.getPosition();
     *
     *    // This will display a diamond hoe morph on top of the
     *    // player's head (but it won't track player's movement)
     *    // oriented at west
     *    c.getWorld().displayMorph(morph, 100, pos.x, pos.y + s.getHeight() + 0.5, pos.z, 90, 0);
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param yaw Horizontal rotation in degrees.
     * @param pitch Vertical rotation in degrees.
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch)
    {
        this.displayMorph(morph, expiration, x, y, z, yaw, pitch, 64);
    }

    /**
     * Display a world morph to all players at given point with rotation
     * some blocks away in this world.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var morph = mappet.createMorph('{Name:"item"}');
     *     var pos = s.getPosition();
     *
     *     var yaw = s.getYaw()
     *     var pitch = s.getPitch()
     *
     *     c.getWorld().displayMorph(morph, 100, pos.x, pos.y + s.getHeight() + 0.5, pos.z, yaw, pitch, 90);
     * }
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param yaw Horizontal rotation in degrees.
     * @param pitch Vertical rotation in degrees.
     * @param range How many blocks far away will this send to players around given point.
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch, int range)
    {
        this.displayMorph(morph, expiration, x, y, z, yaw, pitch, range, null);
    }

    /**
     * Display a world morph to all players at given point with rotation
     * some blocks away in this world only to given player.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var morph = mappet.createMorph('{Name:"item"}');
     *     var pos = s.getPosition();
     *
     *     var yaw = s.getYaw()
     *     var pitch = s.getPitch()
     *
     *     c.getWorld().displayMorph(morph, 100, pos.x, pos.y + s.getHeight() + 0.5, pos.z, yaw, pitch, 90, s);
     * }
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param yaw Horizontal rotation in degrees.
     * @param pitch Vertical rotation in degrees.
     * @param range How many blocks far away will this send to players around given point.
     * @param player The player that only should see the morph, or null for everyone.
     */
    public void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch, int range, IScriptPlayer player);

    /**
     * Gets the region block at given coordinates to be edited programmatically.
     *
     * <pre>{@code
     * function main(c) {
     *     var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     *     var allTriggerBlocks = regionBlock.getOnTickTrigger().getAllBlocks();
     *     if (allTriggerBlocks.size() > 0) {
     *         for each (var triggerBlock in allTriggerBlocks) {
     *             var triggerType = triggerBlock.getType();
     *             if (triggerType === "script") {
     *                 triggerBlock.remove();
     *             }
     *         }
     *     }
     * }
     * }</pre>
     *
     * @param x X coordinate of the region block.
     * @param y Y coordinate of the region block.
     * @param z Z coordinate of the region block.
     * @return Region block at given coordinates.
     */
    public MappetBlockRegion getRegionBlock(int x, int y, int z);

    /**
     * Gets the condition block at given coordinates to be edited programmatically.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var conditionBlock = c.getWorld().getConditionBlock(-127, 88, 129);
     *
     *     var condition = mappet.createCondition();
     *     var conditionStateBlock = condition.addStateBlock();
     *     conditionStateBlock.setStateKey("can_pass");
     *     conditionStateBlock.setTargetMode("global");
     *     conditionStateBlock.setComparator("==");
     *     conditionStateBlock.setComparisonValue(1);
     *
     *     var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *     conditionBlock.addModel(morph, condition);
     * }
     * }</pre>
     *
     * @param x X coordinate of the condition block.
     * @param y Y coordinate of the condition block.
     * @param z Z coordinate of the condition block.
     * @return Condition block at given coordinates.
     */
    public MappetBlockConditionModel getConditionBlock(int x, int y, int z);

    /* BlockBuster stuff */

    /**
     * Gets the Blockbuster model block at given coordinates to be edited programmatically.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var bbModelBlock = c.getWorld().getBBModelBlock(-127, 88, 129);
     *
     *     var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *     bbModelBlock.setMorph(morph);
     * }
     * }</pre>
     *
     * @param x X coordinate of the model block.
     * @param y Y coordinate of the model block.
     * @param z Z coordinate of the model block.
     * @return Blockbuster model block at given coordinates.
     */
    public MappetBlockBBModel getBBModelBlock(int x, int y, int z);

    /**
     * Shoots a gun projectile entity.
     *
     * <pre>{@code
     *     var projectile = c.getWorld().shootBBGunProjectile(c.getSubject(), 547, 160, 497, 0, -90, '{Gun:{TickCommand:"particle heart ~ ~1 ~ 0.2 0.2 0.2 1",StoredAmmo:0,Ticking:1,Damage:1.0f,Projectile:{Meta:0b,Block:"minecraft:stone",Name:"block"},Gravity:0.0f}}')
     *     c.scheduleScript(20, function (c){
     *         projectile.executeCommand("summon tnt ~ ~ ~")
     *         projectile.remove()
     *     });
     * }</pre>
     */
    public IScriptEntity shootBBGunProjectile(IScriptEntity shooter, double x, double y, double z, double yaw, double pitch, String gunPropsNbtString);
}