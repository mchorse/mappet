package mchorse.mappet.api.scripts.user.mappet.conditions.conditionBlocks;

import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.scripts.code.mappet.conditions.conditionBlocks.MappetConditionBlockState;

/**
 * This section covers Mappet's states condition blocks.
 */
public interface IMappetConditionBlockState
{
    /**
     * Get the Minecraft condition block
     *
     * @return Minecraft condition block
     */
    StateConditionBlock getMinecraftConditionBlock();

    /**
     * Set the state key
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
     * regionConditionStateBlock.setStateKey("can_pass"); // <--- This is the state key
     * regionConditionStateBlock.setComparator("==");
     * regionConditionStateBlock.setComparisonValue(1);
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param stateKey State key
     * @return this
     */
    MappetConditionBlockState setStateKey(String stateKey);

    /**
     * Set the target mode and target selector
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
     * regionConditionStateBlock.setTargetMode("selector", "@e[type=player]"); // <--- This is the target mode and selector
     * // Available target modes:
     * // SELECTOR
     * regionConditionStateBlock.setStateKey("can_pass");
     * regionConditionStateBlock.setComparator("==");
     * regionConditionStateBlock.setComparisonValue(1);
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param mode Target mode
     * @param selector Target selector
     * @return this
     */
    MappetConditionBlockState setTargetMode(String mode, String selector);

    /**
     * Set the target mode
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
     * regionConditionStateBlock.setTargetMode("global"); // <--- This is the target mode
     * // Available target modes:
     * // GLOBAL, SUBJECT, OBJECT, PLAYER, NPC
     * regionConditionStateBlock.setStateKey("can_pass");
     * regionConditionStateBlock.setComparator("==");
     * regionConditionStateBlock.setComparisonValue(1);
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param mode Target mode
     * @return this
     */
    MappetConditionBlockState setTargetMode(String mode);

    /**
     * Set the comparator
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
     * regionConditionStateBlock.setComparator("=="); // <--- This is the comparator
     * // Available comparators:
     * // ==, !=, <, >, <=, >=,
     * // EQUALS_TO_STRING, CONTAINS_STRING, EXPRESSION, REGEXP_STRING
     *
     * regionConditionStateBlock.setComparisonValue(1);
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param comparator Comparator
     * @return this
     */
    MappetConditionBlockState setComparator(String comparator);

    /**
     * Set the comparison expression
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
     * regionConditionStateBlock.setComparator("EQUALS_TO_STRING");
     * regionConditionStateBlock.setComparisonExpression("yes"); // <--- comparison expression
     * // (works with comparators: EQUALS_TO_STRING, CONTAINS_STRING, EXPRESSION, REGEXP_STRING)
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param expression Comparison expression
     * @return this
     */
    MappetConditionBlockState setComparisonExpression(String expression);

    /**
     * Set the comparison value
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
     * regionConditionStateBlock.setComparisonValue(1); // <--- comparison value
     * // (works with comparators: ==, >, >=, <, <=)
     *
     * regionBlock.place(c.getWorld(), -125, 88, 128)
     * }</pre>
     *
     * @param value Comparison value
     * @return this
     */
    MappetConditionBlockState setComparisonValue(double value);
}
