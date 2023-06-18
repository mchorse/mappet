package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.common.tileentity.TileEntityModel;
import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockBBModel;
import mchorse.mappet.utils.ScriptUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MappetBlockBBModel implements IMappetBlockBBModel {
    public TileEntityModel bbModelBlock;
    public TileEntityModelSettings settings;

    public static MappetBlockBBModel create()
    {
        return new MappetBlockBBModel();
    }

    public MappetBlockBBModel()
    {
        this.bbModelBlock = new TileEntityModel();
        this.settings = this.bbModelBlock.getSettings();
    }

    public MappetBlockBBModel(World world, BlockPos pos)
    {
        this.bbModelBlock = (TileEntityModel) world.getTileEntity(pos);
        assert this.bbModelBlock != null;
        this.settings = this.bbModelBlock.getSettings();
    }

    @Override
    public MappetBlockBBModel place(IScriptWorld world, int x, int y, int z)
    {
        return ScriptUtils.place(
                world.getMinecraftWorld(),
                new BlockPos(x, y, z),
                Blockbuster.modelBlock,
                TileEntityModel.class,
                (TileEntityModel tileEntity) -> this.bbModelBlock = tileEntity,
                () -> this);
    }

    @Override
    public MappetBlockBBModel notifyUpdate()
    {
        ScriptUtils.sendTileUpdatePacket(this.bbModelBlock);
        return this;
    }

    @Override
    public MappetBlockBBModel setMorph(AbstractMorph morph)
    {
        this.bbModelBlock.setMorph(morph);
        return this;
    }

    @Override
    public AbstractMorph getMorph()
    {
        return this.bbModelBlock.morph.get();
    }

    @Override
    public MappetBlockBBModel clearMorph()
    {
        this.bbModelBlock.setMorph(null);
        return this;
    }

    @Override
    public MappetModelSettings getSettings()
    {
        return new MappetModelSettings(this);
    }
}
