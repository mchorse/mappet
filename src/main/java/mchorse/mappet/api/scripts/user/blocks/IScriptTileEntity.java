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
     */
    public TileEntity getMinecraftTileEntity();

    /**
     * Get tile entity's ID.
     */
    public String getId();

    /**
     * Check whether this tile entity is invalid (i.e. was removed from the world
     * or unavailable for some reason).
     */
    public boolean isInvalid();

    /**
     * Get (a copy of) this tile entity's NBT data.
     */
    public INBTCompound getData();

    /**
     * Overwrite NBT data of this tile entity. <b>WARNING</b>: use it only if you
     * know what are you doing as this method can corrupt tile entities.
     */
    public void setData(INBTCompound compound);

    /**
     * Get Forge's custom tag compound in which you can story any
     * data you want.
     *
     * <p>There is no setter method as you can directly work with returned
     * NBT compound. Any changes to returned compound <b>will be reflected
     * upon tile entity's data</b>.</p>
     */
    public INBTCompound getTileData();
}