package mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks;

import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetConditionBlock;
import mchorse.mappet.api.scripts.user.mappet.conditions.conditionBlocks.IMappetConditionBlockState;
import mchorse.mappet.api.utils.ComparisonMode;
import mchorse.mappet.api.utils.TargetMode;

import java.util.List;

public class MappetConditionBlockState extends MappetConditionBlock<AbstractConditionBlock> implements IMappetConditionBlockState
{
    public MappetConditionBlockState(AbstractConditionBlock block, List<AbstractConditionBlock> blocks)
    {
        super(block, blocks);
    }

    @Override
    public StateConditionBlock getMinecraftConditionBlock()
    {
        return (StateConditionBlock) this.block;
    }

    @Override
    public MappetConditionBlockState setStateKey(String stateKey)
    {
        this.getMinecraftConditionBlock().id = stateKey;
        return this;
    }

    @Override
    public MappetConditionBlockState setTargetMode(String mode, String selector) {
        switch(mode.toUpperCase()) {
            case "GLOBAL":
                this.getMinecraftConditionBlock().target.mode = TargetMode.GLOBAL;
                break;
            case "SUBJECT":
                this.getMinecraftConditionBlock().target.mode = TargetMode.SUBJECT;
                break;
            case "OBJECT":
                this.getMinecraftConditionBlock().target.mode = TargetMode.OBJECT;
                break;
            case "PLAYER":
                this.getMinecraftConditionBlock().target.mode = TargetMode.PLAYER;
                break;
            case "NPC":
                this.getMinecraftConditionBlock().target.mode = TargetMode.NPC;
                break;
            case "SELECTOR":
                this.getMinecraftConditionBlock().target.mode = TargetMode.SELECTOR;
                this.getMinecraftConditionBlock().target.selector = selector;
                break;
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        return this;
    }

    @Override
    public MappetConditionBlockState setTargetMode(String mode) {
        return this.setTargetMode(mode, null);
    }

    @Override
    public MappetConditionBlockState setComparator(String comparator) {
        switch(comparator.toUpperCase()) {
            case "<":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.LESS;
                break;
            case "<=":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.LESS_THAN;
                break;
            case "==":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.EQUALS;
                break;
            case ">=":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.GREATER_THAN;
                break;
            case ">":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.GREATER;
                break;
            case "IS_TRUE":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.IS_TRUE;
                break;
            case "IS_FALSE":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.IS_FALSE;
                break;
            case "EXPRESSION":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.EXPRESSION;
                break;
            case "EQUALS_TO_STRING":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.EQUALS_TO_STRING;
                break;
            case "CONTAINS_STRING":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.CONTAINS_STRING;
                break;
            case "REGEXP_STRING":
                this.getMinecraftConditionBlock().comparison.comparison = ComparisonMode.REGEXP_STRING;
                break;
            case "":
                this.getMinecraftConditionBlock().comparison.comparison = null;
                break;
            default:
                throw new IllegalArgumentException("Invalid comparator: " + comparator);
        }
        return this;
    }

    @Override
    public MappetConditionBlockState setComparisonExpression(String expression) {
        switch (this.getMinecraftConditionBlock().comparison.comparison) {
            case EXPRESSION:
            case EQUALS_TO_STRING:
            case CONTAINS_STRING:
            case REGEXP_STRING:
                this.getMinecraftConditionBlock().comparison.expression = expression;
                break;
            default:
                throw new IllegalArgumentException("Invalid comparison: " + this.getMinecraftConditionBlock().comparison.comparison);
        }
        return this;
    }

    @Override
    public MappetConditionBlockState setComparisonValue(double value) {
        switch (this.getMinecraftConditionBlock().comparison.comparison) {
            case LESS:
            case LESS_THAN:
            case EQUALS:
            case GREATER_THAN:
            case GREATER:
                this.getMinecraftConditionBlock().comparison.value = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid comparison: " + this.getMinecraftConditionBlock().comparison.comparison);
        }
        return this;
    }
}
