package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.common.tileentity.TileEntityModel;
import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockBBModel;
import mchorse.mappet.utils.Utils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.init.Blocks;
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
        BlockPos pos = new BlockPos(x, y, z);
        World mcWorld = world.getMinecraftWorld();

        if (mcWorld.getBlockState(pos).getBlock() != Blocks.AIR)
        {
            mcWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 4);
        }

        mcWorld.setBlockState(pos, Blockbuster.modelBlock.getDefaultState(), 2 | 4);

        if (mcWorld.getBlockState(pos).getBlock() == Blockbuster.modelBlock)
        {
            TileEntityModel tileBBModel = (TileEntityModel) mcWorld.getTileEntity(pos);
            mcWorld.setTileEntity(pos, tileBBModel);
            tileBBModel.markDirty();
            this.bbModelBlock = tileBBModel;
        }

        return this;
    }

    @Override
    public MappetBlockBBModel setMorph(AbstractMorph morph)
    {
        this.bbModelBlock.setMorph(morph);
        Utils.sendModelUpdatePacket(this.bbModelBlock);
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
        Utils.sendModelUpdatePacket(this.bbModelBlock);
        return this;
    }

    @Override
    public MappetModelSettings getSettings()
    {
        return new MappetModelSettings(this);
    }
}
