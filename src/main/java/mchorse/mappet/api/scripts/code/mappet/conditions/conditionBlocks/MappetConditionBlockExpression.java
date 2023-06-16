package mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks;

import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetConditionBlock;
import mchorse.mappet.api.scripts.user.mappet.conditions.conditionBlocks.IMappetConditionBlockExpression;

import java.util.List;

public class MappetConditionBlockExpression extends MappetConditionBlock<AbstractConditionBlock> implements IMappetConditionBlockExpression
{

    public MappetConditionBlockExpression(AbstractConditionBlock block, List<AbstractConditionBlock> blocks)
    {
        super(block, blocks);
    }

    @Override
    public ExpressionConditionBlock getMinecraftConditionBlock()
    {
        return (ExpressionConditionBlock) this.block;
    }

    @Override
    public MappetConditionBlockExpression set(String expression)
    {
        this.getMinecraftConditionBlock().expression = expression;
        return this;
    }
}
