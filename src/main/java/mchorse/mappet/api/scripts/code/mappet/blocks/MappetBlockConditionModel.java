package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.utils.ConditionModel;
import mchorse.mappet.utils.Utils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MappetBlockConditionModel implements IMappetBlockConditionModel
{
    public TileConditionModel conditionModelBlock;
    private List<ConditionModel> list = new ArrayList<>();
    public TileEntityModelSettings settings;

    public static MappetBlockConditionModel create()
    {
        MappetBlockConditionModel conditionModelBlock = new MappetBlockConditionModel();
        return conditionModelBlock;
    }

    public MappetBlockConditionModel()
    {
        this.conditionModelBlock = new TileConditionModel();
        this.list = this.conditionModelBlock.list;
        this.settings = this.conditionModelBlock.getSettings();
    }

    public MappetBlockConditionModel(World world, BlockPos pos)
    {
        this.conditionModelBlock = (TileConditionModel) world.getTileEntity(pos);
        assert this.conditionModelBlock != null;
        this.list = this.conditionModelBlock.list;
    }

    @Override
    public MappetBlockConditionModel place(IScriptWorld world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        World mcWorld = world.getMinecraftWorld();

        if (mcWorld.getBlockState(pos).getBlock() != Blocks.AIR)
        {
            mcWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 4);
        }

        mcWorld.setBlockState(pos, Mappet.conditionModelBlock.getDefaultState(), 2 | 4);

        if (mcWorld.getBlockState(pos).getBlock() == Mappet.conditionModelBlock)
        {
            TileConditionModel tileConditionModel = (TileConditionModel) mcWorld.getTileEntity(pos);
            tileConditionModel.list = this.list;
            mcWorld.setTileEntity(pos, tileConditionModel);
            tileConditionModel.markDirty();
            this.conditionModelBlock = tileConditionModel;
        }

        return this;
    }

    @Override
    public MappetBlockConditionModel addModel(AbstractMorph morph, MappetCondition condition)
    {
        ConditionModel model = new ConditionModel();
        model.morph = morph;
        model.checker = condition.checker;

        this.list.add(model);
        Utils.sendModelUpdatePacket( this.conditionModelBlock);
        return this;
    }


    @Override
    public MappetBlockConditionModel removeModel(AbstractMorph morph)
    {
        this.conditionModelBlock.list.removeIf(model -> model.morph.equals(morph));
        Utils.sendModelUpdatePacket( this.conditionModelBlock);
        return this;
    }

    @Override
    public MappetBlockConditionModel clearModels()
    {
        this.conditionModelBlock.list.clear();
        Utils.sendModelUpdatePacket( this.conditionModelBlock);
        return this;
    }

    @Override
    public MappetModelSettings getSettings()
    {
        return new MappetModelSettings(this);
    }
}
