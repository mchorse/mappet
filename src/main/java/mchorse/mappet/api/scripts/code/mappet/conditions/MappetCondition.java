package mchorse.mappet.api.scripts.code.mappet.conditions;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockExpression;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockState;
import mchorse.mappet.api.scripts.user.mappet.conditions.IMappetCondition;
import mchorse.mappet.api.scripts.user.mappet.conditions.IMappetConditionBlock;

import java.util.ArrayList;
import java.util.List;

public class MappetCondition implements IMappetCondition
{

    public List<AbstractConditionBlock> blocks;
    public Checker checker;

    public MappetCondition(Checker checker)
    {
        this.checker = checker;
        this.checker.mode = Checker.Mode.CONDITION;
        this.blocks = checker.condition.blocks;
    }

    @Override
    public MappetCondition set(MappetCondition condition)
    {
        this.blocks = condition.blocks;
        this.checker = condition.checker;
        return this;
    }

    @Override
    public MappetCondition setExpression(String expression)
    {
        this.checker.mode = Checker.Mode.EXPRESSION;
        this.checker.expression = expression;
        return this;
    }

    @Override
    public List<IMappetConditionBlock> getAllBlocks()
    {
        List<IMappetConditionBlock> allConditionBlocks = new ArrayList<>();
        for (AbstractConditionBlock block : blocks)
        {
            allConditionBlocks.add(MappetConditionBlock.create(block, blocks));
        }
        return allConditionBlocks;
    }

    @Override
    public MappetConditionBlockExpression addExpressionBlock()
    {
        ExpressionConditionBlock block = new ExpressionConditionBlock();
        this.blocks.add(block);
        return new MappetConditionBlockExpression(block, blocks);
    }

    @Override
    public MappetConditionBlockState addStateBlock()
    {
        StateConditionBlock block = new StateConditionBlock();
        this.blocks.add(block);
        return new MappetConditionBlockState(block, blocks);
    }
}
