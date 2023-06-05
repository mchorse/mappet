package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.util.EnumParticleTypes;

import java.util.List;

/**
 * This interface represent a world passed in the event.
 * It's a second interface for fancy functions.
 *
 * <pre>{@code
 * fun main(c: IScriptEvent) {
 *     val world : IScriptFancyWorld = c.getFancyWorld();
 *
 *     // Do something fancy with world...
 * }
 * }</pre>
 */
public interface IScriptFancyWorld {
    /**
     * Transforms a block to a falling block in specific coordinates.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.explode(-2, 100, -2, 2, 100, 2, 100)
     * }
     * }</pre>
     * @return The falling block entities in a list.
     */
    public List<IScriptEntity> explode(int x1, int y1, int z1, int x2, int y2, int z2, int blocksPercentage);

    /**
     * Transforms a block to a falling block in specific coordinates.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.explode(0, 100, 0, 3, 100)
     * }
     * }</pre>
     * @return The falling block entities in a list.
     */
    public List<IScriptEntity> explode(int x, int y, int z, int radius, int blocksPercentage);

    /**
     * Explodes the blocks in the range by teleporting them randomly in an explosive way.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.tpExplode(0, 100, 0, 2, 100, 2, 100)
     * }
     * }</pre>
     */
    public void tpExplode(int x1, int y1, int z1, int x2, int y2, int z2, int blocksPercentage);

    /**
     * Explodes the blocks in the range by teleporting them randomly in an explosive way.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     val block : IScriptBlockState = mappet.createBlockState("minecraft:air", 1)
     *     world.fill("5", block, 539, 151, 548, 555, 160, 570, 1, mappet.getParticleType("cloud"), 2, "", 0.3, 0.8)
     * }
     * }</pre>
     */
    public void tpExplode(int x, int y, int z, int radius, int blocksPercentage);

    /**
     * Sets a block in specific coordinates with a fancy effect.
     */
    public void setBlock(IScriptBlockState state, int x, int y, int z, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch);

    /**
     * Sets a block in specific coordinates with a fancy effect.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.fill(
     *         "du", //du, ud, ns, sn, we, ew,
     *         mappet.createBlockState("minecraft:air"),
     *         494, 88, 2762,
     *         530, 100, 2762,
     *         1, //delayBetweenLayers
     *         mappet.getParticleType("cloud"), 2,
     *         "minecraft:block.wood.break", 0.3f, 0.8f
     *     )
     * }
     * }</pre>
     */
    public void fill(String mode, IScriptBlockState state, int x1, int y1, int z1, int x2, int y2, int z2, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch);

    /**
     * Sets a tile entity in specific block with a fancy effect.
     */
    public void setTileEntity(int x, int y, int z, IScriptBlockState blockState, INBTCompound tileData, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch);

    /**
     * Sets a tile entity in specific block with a fancy effect.
     */
    public void fillTileEntities(String mode, int x1, int y1, int z1, int x2, int y2, int z2, IScriptBlockState state, INBTCompound tileData, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch);

    /**
     * Clones a block to a specific coordinates with a fancy effect.
     */
    public void clone(int x, int y, int z, int xNew, int yNew, int zNew, EnumParticleTypes particleType, int particlesAmount, String soundEvent, float volume, float pitch);

    /**
     * Clones a coordinates range to a specific coordinates with a fancy effect.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.clone(
     *         "du", //du, ud, ns, sn, we, ew,
     *         527, 150, 549,
     *         536, 155, 545,
     *         527, 160, 549,
     *         20, //delayBetweenLayers
     *         mappet.getParticleType("cloud"), 20,
     *         "minecraft:block.wood.place", 0.3, 0.8
     *     )
     * }
     * c.getFancyWorld().clone(2, 527, 150, 549, 536, 155, 545, 527, 160, 549, 20, mappet.getParticleType("cloud"), 20, "minecraft:block.wood.place", 0.3, 0.8);
     * }</pre>
     */
    public void clone(String mode, int x1, int y1, int z1, int x2, int y2, int z2, int xNew, int yNew, int zNew, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch);

    /**
     * Loads a schematic to a specific coordinates with a fancy effect.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.loadSchematic("5",
     *          "my_schematics",
     *          500, 166, 569,
     *          20,
     *          mappet.getParticleType("cloud"), 3,
     *          "minecraft:block.wood.place", 0.3, 0.8
     *     )
     * }
     * }</pre>
     */
    public void loadSchematic(String mode, String name, int target_x, int target_y, int target_z, int delayBetweenLayers, EnumParticleTypes particleType, int particlesPerBlock, String soundEvent, float volume, float pitch);

    /**
     * Spawns a NPC in specific coordinates with a fancy effect.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val world : IScriptFancyWorld = c.getFancyWorld()
     *     world.spawnNpc(
     *          "McHorse", "default",
     *          500, 166, 569,
     *          0, 90, 0,
     *          mappet.getParticleType("cloud"), 0.1, 20,
     *          "minecraft:entity.zombie.infect", 0.3, 0.8
     *     );
     * }
     * }</pre>
     * @return The spawned NPC.
     */
    public IScriptNpc spawnNpc(String id, String state, double x, double y, double z, float yaw, float pitch, float yawHead, EnumParticleTypes particleType, double particleSpeed, int particlesAmount, String soundEvent, float volume, float volumePitch);
}
