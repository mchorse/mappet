package mchorse.mappet.api.scripts.user.blocks;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Tile entity interface.
 *
 * <p>This interface represents Minecraft tile entities, which are special
 * kind of entities that exist within blocks (like crafting tables, chests,
 * furnance, etc.).</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var te = c.getWorld().getTileEntity(371, 4, -100);
 *
 *        if (te.getId() === "mappet:region")
 *        {
 *            var data = te.getData();
 *
 *            // Replace on enter trigger of the tile entity to /toggledownfall
 *            data.getCompound("Region").setNBT("OnEnter", '{Blocks:[{Command:"/toggledownfall",Type:"command"}]}');
 *            te.setData(data);
 *        }
 *    }
 * }</pre>
 */
public interface IScriptTileEntity
{
    /**
     * Get Minecraft tile entity instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var itemInFirstSlot = c.getWorld().getTileEntity(-218, 101, 199).getMinecraftTileEntity().func_70301_a(0)
     *
     *     c.send(itemInFirstSlot)
     * }
     * }</pre>
     */
    public TileEntity getMinecraftTileEntity();

    /**
     * Get tile entity's ID.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var BlockTileEntity = c.getWorld().getTileEntity(-218, 101, 199)
     *
     *     c.send(BlockTileEntity.getId())
     * }
     * }</pre>
     */
    public String getId();

    /**
     * Check whether this tile entity is invalid (i.e. was removed from the world
     * or unavailable for some reason).
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var BlockTileEntity = c.getWorld().getTileEntity(-218, 101, 199)
     *
     *     c.send(BlockTileEntity.isInvalid())
     * }
     * }</pre>
     */
    public boolean isInvalid();

    /**
     * Get (a copy of) this tile entity's NBT data.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var BlockTileEntity = c.getWorld().getTileEntity(-218, 101, 199)
     *
     *     c.send(BlockTileEntity.getData())
     * }
     * }</pre>
     */
    public INBTCompound getData();

    /**
     * Overwrite NBT data of this tile entity. <b>WARNING</b>: use it only if you
     * know what are you doing as this method can corrupt tile entities.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var BlockTileEntity = c.getWorld().getTileEntity(-218, 101, 199)
     *     var tag = mappet.createCompound('{CookTime:0,x:-218,BurnTime:0,y:101,z:199,Item:[],id:"minecraft:furnace",CookTimeTotal:0,Lock:""}')
     *
     *     BlockTileEntity.setData(tag)
     * }
     * }</pre>
     */
    public void setData(INBTCompound compound);

    /**
     * Get Forge's custom tag compound in which you can story any
     * data you want.
     *
     * <p>There is no setter method as you can directly work with returned
     * NBT compound. Any changes to returned compound <b>will be reflected
     * upon tile entity's data</b>.</p>
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var BlockTileEntity = c.getWorld().getTileEntity(-218, 101, 199)
     *
     *     c.send(BlockTileEntity.getTileData())
     * }
     * }</pre>
     */
    public INBTCompound getTileData();
}