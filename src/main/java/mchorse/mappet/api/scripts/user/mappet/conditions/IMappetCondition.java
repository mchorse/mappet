package mchorse.mappet.api.scripts.user.mappet.conditions;

import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockExpression;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockState;

import java.util.List;

/**
 * This section covers Mappet's conditions. Here you can get or add different condition blocks types to given condition.
 */
public interface IMappetCondition
{
    /**
     * Sets the Minecraft condition from the given condition. (copy)
     *
     * <pre>{@code
     * // This example copies the region block's condition to another region block
     * var world = c.getWorld();
     * var regionBlock1 = world.getRegionBlock(0, 4, 0);
     * var regionBlock2 = world.getRegionBlock(0, 5, 0);
     * var regionBlock1Condition = regionBlock1.getCondition();
     * var regionBlock2Condition = regionBlock2.getCondition();
     * regionBlock2Condition.set(regionBlock1Condition);
     * }</pre>
     *
     * @param condition condition to copy
     * @return the condition instance
     */
    MappetCondition set(MappetCondition condition);

    /**
     * Set the expression for this condition
     *
     * <pre>{@code
     * var regionBlock = mappet.createRegionBlock();
     * regionBlock.setPassable(false);
     *
     * regionBlock.getCondition().setExpression("0==1")
     * regionBlock.place(c.getWorld(), 0, 4, 0);
     * }</pre>
     *
     * @param expression The expression to set
     */
    IMappetCondition setExpression(String expression);

    /**
     * Get all condition blocks for this condition
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     *
     * regionBlock.getCondition().getAllBlocks().forEach(function(block) {
     *     block.remove();
     * });
     * }</pre>
     *
     * @return All condition blocks for this condition
     */
    List<IMappetConditionBlock> getAllBlocks();

    /**
     * Add an expression condition block to this condition
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.getCondition().addExpressionBlock().set("0==1");
     * }</pre>
     *
     * @return The newly created expression condition block
     */
    MappetConditionBlockExpression addExpressionBlock();

    /**
     * Add a state condition block to this condition
     *
     * <pre>{@code
     * var regionBlock = mappet.createRegionBlock();
     *
     * regionBlock
     *     .addBoxShape(1, 1, 1)
     *     .setCheckEntities(true)
     *     .setPassable(false);
     *
     * var regionConditionStateBlock = regionBlock.getCondition().addStateBlock();
     * regionConditionStateBlock.setTargetMode("global");
     * regionConditionStateBlock.setStateKey("can_pass");
     * regionConditionStateBlock.setComparator("==");
     * regionConditionStateBlock.setComparisonValue(1);
     *
     * regionBlock.place(c.getWorld(), 0, 4, 0)
     * }</pre>
     *
     * @return The newly created state condition block
     */
    MappetConditionBlockState addStateBlock();
}
