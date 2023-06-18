package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.blockbuster.common.tileentity.TileEntityModelSettings;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.utils.ConditionModel;
import mchorse.mappet.utils.ScriptUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
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
        return ScriptUtils.place(
                world.getMinecraftWorld(),
                new BlockPos(x, y, z),
                Mappet.conditionModelBlock,
                TileConditionModel.class,
                (TileConditionModel tileEntity) -> {
                    tileEntity.list = this.list;
                    this.conditionModelBlock = tileEntity;
                },
                () -> this);
    }

    @Override
    public MappetBlockConditionModel notifyUpdate()
    {
        ScriptUtils.sendTileUpdatePacket(this.conditionModelBlock);
        return this;
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

    @Override
    public MappetModelSettings getSettings()
    {
        return new MappetModelSettings(this);
    }
}
