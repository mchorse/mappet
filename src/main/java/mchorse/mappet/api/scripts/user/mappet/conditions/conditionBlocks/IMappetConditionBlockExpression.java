package mchorse.mappet.api.scripts.user.mappet.conditions.conditionBlocks;

import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockExpression;

/**
 * This section covers Mappet's expressions condition blocks.
 */
public interface IMappetConditionBlockExpression
{
    /**
     * Get the Minecraft condition block
     *
     * @return Minecraft condition block
     */
    ExpressionConditionBlock getMinecraftConditionBlock();

    /**
     * Set the expression of the condition block
     *
     * <pre>{@code
     * var regionBlock = mappet.createRegionBlock();
     *
     * regionBlock.setPassable(false);
     *
     * var regionBlockCondition = regionBlock.getCondition().addExpressionBlock();
     * regionBlockCondition.set("1==1");
     *
     * regionBlock.place(c.getWorld(), 0, 4, 0);
     * }</pre>
     *
     * @param expression Expression
     */
    MappetConditionBlockExpression set(String expression);
}
