package mchorse.mappet.api.scripts.user.mappet.blocks;

import mchorse.mappet.api.scripts.code.mappet.blocks.MappetBlockConditionModel;
import mchorse.mappet.api.scripts.code.mappet.blocks.MappetModelSettings;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.metamorph.api.morphs.AbstractMorph;

/**
 * Condition model block API.
 *
 * <pre>{@code
 * function main(c)
 * {
 *     var conditionBlock = mappet.createConditionBlock();
 *
 *     var conditionBlockSettings = conditionBlock.getSettings()
 *         .setGlobalEnabled(true)
 *         .setShadowEnabled(false)
 *         .scale(1, 0.1, 1);
 *
 *     var morph1 = mappet.createMorph("{Meta:5b,Block:\"minecraft:wool\",Name:\"block\"}");
 *     var condition1 = mappet.createCondition();
 *     var condition1StateBlock = condition1.addStateBlock()
 *         .setStateKey("money")
 *         .setTargetMode("subject")
 *         .setComparator(">=")
 *         .setComparisonValue(100);
 *     conditionBlock.addModel(morph1, condition1);
 *
 *     var morph2 = mappet.createMorph("{Meta:14b,Block:\"minecraft:wool\",Name:\"block\"}");
 *     var condition2 = mappet.createCondition();
 *     var condition2StateBlock = condition2.addStateBlock()
 *         .setStateKey("money")
 *         .setTargetMode("subject")
 *         .setComparator("<")
 *         .setComparisonValue(100);
 *     conditionBlock.addModel(morph2, condition2);
 *
 *     conditionBlock.place(c.getWorld(), 0, 4, 0);
 * }
 * }</pre>
 */
public interface IMappetBlockConditionModel
{
    /**
     * Place this condition model block in the world
     *
     * <pre>{@code
     * var conditionBlock = mappet.createConditionBlock();
     * // do something with the condition block...
     * conditionBlock.place(c.getWorld(), -127, 88, 129);
     * }</pre>
     *
     * @param world world to place the block in
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this condition model block instance
     */
    MappetBlockConditionModel place(IScriptWorld world, int x, int y, int z);


    /**
     * Adds a model to this condition model block.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var conditionBlock = mappet.createConditionBlock();
     *
     *     var morph1 = mappet.createMorph("{Meta:5b,Block:\"minecraft:wool\",Name:\"block\"}");
     *     var condition1 = mappet.createCondition();
     *     var conditionStateBlock = condition1.addStateBlock()
     *         .setStateKey("money")
     *         .setTargetMode("subject")
     *         .setComparator(">=")
     *         .setComparisonValue(100);
     *     conditionBlock.addModel(morph1, condition1);
     *
     *     var morph2 = mappet.createMorph("{Meta:14b,Block:\"minecraft:wool\",Name:\"block\"}");
     *     var condition2 = mappet.createCondition();
     *     var conditionStateBlock = condition2.addStateBlock()
     *         .setStateKey("money")
     *         .setTargetMode("subject")
     *         .setComparator("<")
     *         .setComparisonValue(100);
     *     conditionBlock.addModel(morph2, condition2);
     *
     *     conditionBlock.place(c.getWorld(), -127, 88, 129)
     * }
     * }</pre>
     *
     * @param morph morph to add
     * @param condition condition to add
     * @return this condition model block instance
     */
    MappetBlockConditionModel addModel(AbstractMorph morph, MappetCondition condition);

    /**
     * Removes a model from this condition model block.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.removeModel(mappet.createMorph("{Name:\"blockbuster.alex\"}"))
     * }</pre>
     *
     * @param morph morph to remove
     * @return this condition model block instance
     */
    MappetBlockConditionModel removeModel(AbstractMorph morph);

    /**
     * Removes all models from this condition model block.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.clearModels();
     * }</pre>
     *
     * @return this condition model block instance
     */
    MappetBlockConditionModel clearModels();

    /**
     * Get condition model block settings.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * var conditionBlockSettings = conditionBlock.getSettings()
     *     .setGlobalEnabled(true)
     *     .setShadowEnabled(false)
     *     .scale(1, 0.1, 1);
     * }</pre>
     *
     * @return condition model block settings
     */
    MappetModelSettings getSettings();
}
