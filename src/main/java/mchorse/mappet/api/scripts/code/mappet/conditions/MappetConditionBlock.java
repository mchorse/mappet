package mchorse.mappet.api.scripts.code.mappet.conditions;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockExpression;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.mappet.conditions.IMappetConditionBlock;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;

import java.util.List;

public class MappetConditionBlock <T extends AbstractConditionBlock> implements IMappetConditionBlock
{
    protected T block;
    protected List<T> blocks;

    public static IMappetConditionBlock create(AbstractConditionBlock block, List<AbstractConditionBlock> blocks)
    {
        if (block instanceof ExpressionConditionBlock)
        {
            return new MappetConditionBlockExpression((ExpressionConditionBlock) block, blocks);
        }

        return null;
    }
    public MappetConditionBlock(T block, List<T> blocks)
    {
        this.block = (T) block;
        this.blocks = (List<T>) blocks;
    }


    @Override
    public String getType()
    {
        return CommonProxy.getConditionBlocks().getType(block);
    }

    @Override
    public boolean remove()
    {
        return blocks.remove(block);
    }

    @Override
    public INBTCompound toNBT()
    {
        return new ScriptNBTCompound(block.toNBT());
    }
}
