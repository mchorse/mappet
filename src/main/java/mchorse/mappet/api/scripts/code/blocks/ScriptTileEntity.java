package mchorse.mappet.api.scripts.code.blocks;

import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.blocks.IScriptTileEntity;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ScriptTileEntity implements IScriptTileEntity
{
    private TileEntity tile;

    public ScriptTileEntity(TileEntity tile)
    {
        this.tile = tile;
    }

    @Override
    public TileEntity getMinecraftTileEntity()
    {
        return this.tile;
    }

    @Override
    public String getId()
    {
        ResourceLocation key = TileEntity.getKey(this.tile.getClass());

        return key == null ? "" : key.toString();
    }

    @Override
    public boolean isInvalid()
    {
        return this.tile.isInvalid();
    }

    @Override
    public INBTCompound getData()
    {
        return new ScriptNBTCompound(this.tile.serializeNBT());
    }

    @Override
    public void setData(INBTCompound compound)
    {
        this.tile.readFromNBT(compound.getNBTTagCompound());
        this.tile.markDirty();
    }

    @Override
    public INBTCompound getTileData()
    {
        return new ScriptNBTCompound(this.tile.getTileData());
    }
}