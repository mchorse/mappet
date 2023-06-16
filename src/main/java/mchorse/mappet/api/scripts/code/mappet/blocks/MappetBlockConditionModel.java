package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.utils.ConditionModel;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class MappetBlockConditionModel implements IMappetBlockConditionModel
{
    private TileConditionModel conditionModelBlock;
    private List<ConditionModel> list = new ArrayList<>();
    private TileEntityModelSettings settings;

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
    public MappetBlockConditionModel setGlobal(boolean isGlobal)
    {
        conditionModelBlock.isGlobal = isGlobal;
        return this;
    }

    @Override
    public MappetBlockConditionModel setShadow(boolean isShadow)
    {
        conditionModelBlock.isShadow = isShadow;
        return this;
    }

    @Override
    public MappetBlockConditionModel setFrequency(int frequency)
    {
        conditionModelBlock.frequency = frequency;
        return this;
    }

    @Override
    public MappetBlockConditionModel translate(double x, double y, double z)
    {
        this.settings.setX((float) x);
        this.settings.setY((float) y);
        this.settings.setZ((float) z);
        return this;
    }

    @Override
    public MappetBlockConditionModel rotate(double x, double y, double z)
    {
        this.settings.setRotateBody((float) x);
        this.settings.setRotatePitch((float) y);
        this.settings.setRotateYawHead((float) z);
        return this;
    }

    @Override
    public MappetBlockConditionModel scale(double x, double y, double z)
    {
        this.settings.setSx((float) x);
        this.settings.setSy((float) y);
        this.settings.setSz((float) z);
        return this;
    }

    @Override
    public MappetBlockConditionModel scale(double xyz)
    {
        return this.scale(xyz, xyz, xyz);
    }

    @Override
    public MappetBlockConditionModel addModel(AbstractMorph morph, MappetCondition condition)
    {
        ConditionModel model = new ConditionModel();
        model.morph = morph;
        model.checker = condition.checker;

        this.list.add(model);
        return this;
    }


    @Override
    public MappetBlockConditionModel removeModel(AbstractMorph morph)
    {
        this.conditionModelBlock.list.removeIf(model -> model.morph.equals(morph));
        return this;
    }

    @Override
    public MappetBlockConditionModel clearModels()
    {
        this.conditionModelBlock.list.clear();
        return this;
    }

}
