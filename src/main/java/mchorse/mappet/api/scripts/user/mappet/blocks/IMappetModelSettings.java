package mchorse.mappet.api.scripts.user.mappet.blocks;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.blocks.MappetModelSettings;

public interface IMappetModelSettings {

    /**
     * Translates the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setTranslate(0, 1, 0);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this condition model block instance
     */
    MappetModelSettings setTranslate(double x, double y, double z);

    /**
     * Scales the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setScale(1, 0.1, 1);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this condition model block instance
     */
    MappetModelSettings setScale(double x, double y, double z);

    /**
     * Scales the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setScale(0.5);
     * }</pre>
     *
     * @param xyz x, y and z coordinate
     * @return this condition model block instance
     */
    MappetModelSettings setScale(double xyz);

    /**
     * Rotates the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setRotate(0, 90, 0);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this condition model block instance
     */
    MappetModelSettings setRotate(double x, double y, double z);

    /**
     * Rotates the model around the given axis.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setRotateAxis(0, 1, 0);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this condition model block instance
     */
    MappetModelSettings setRotateAxis(double x, double y, double z);

    /**
     * Enables or disables the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setEnabled(false);
     * }</pre>
     *
     * @param enabled whether the model should be enabled or not
     * @return this condition model block instance
     */
    MappetModelSettings setEnabled(boolean enabled);

    /**
     * Sets the `global` flag of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setGlobalEnabled(true);
     * }</pre>
     *
     * @param enabled whether the model should be global or not
     * @return this condition model block instance
     */
    MappetModelSettings setGlobalEnabled(boolean enabled);

    /**
     * Enables or disables the shadow of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setShadowEnabled(false);
     * }</pre>
     *
     * @param enabled whether the shadow should be enabled or not
     * @return this condition model block instance
     */
    MappetModelSettings setShadowEnabled(boolean enabled);

    /**
     * Enables or disables the block hitbox of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setBlockHitboxEnabled(false);
     * }</pre>
     *
     * @param enabled whether the hitbox should be enabled or not
     * @return this condition model block instance
     */
    MappetModelSettings setBlockHitboxEnabled(boolean enabled);

    /**
     * Enables or disables the `render always` flag of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     *
     * conditionBlock.getSettings().setRenderAlwaysEnabled(true);
     * }</pre>
     *
     * @param enabled whether the render should be enabled or not
     * @return this condition model block instance
     */
    MappetModelSettings setRenderAlwaysEnabled(boolean enabled);

    /**
     * Enables or disables the `render last` flag of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     *
     * conditionBlock.getSettings().setRenderLastEnabled(true);
     * }</pre>
     *
     * @param enabled whether the render should be enabled or not
     * @return this condition model block instance
     */
    MappetModelSettings setRenderLastEnabled(boolean enabled);

    /**
     * Sets the light value of the model.
     *
     * <pre>{@code
     * var conditionBlock = c.getWorld().getConditionBlock(0, 4, 0);
     * conditionBlock.getSettings().setLightValue(15);
     * }</pre>
     *
     * @param value light value
     * @return this condition model block instance
     */
    MappetModelSettings setLightValue(int value);

    /**
     * Sets the item in the given slot of the model. (0 - 5)
     *
     * <pre>{@code
     * var bbModelBlock = c.getWorld().getBBModelBlock(-127, 75, 183);
     *
     * var bbBlockSettings = bbModelBlock.getSettings()
     *     .setSlot(mappet.createItem("minecraft:diamond_sword"), 0) //main hand
     *     .setSlot(mappet.createItem("minecraft:golden_apple"), 1) //off hand
     *     .setSlot(mappet.createItem("minecraft:diamond_boots"), 2) //boots
     *     .setSlot(mappet.createItem("minecraft:diamond_leggings"), 3) //leggings
     *     .setSlot(mappet.createItem("minecraft:diamond_chestplate"), 4) //chestplate
     *     .setSlot(mappet.createItem("minecraft:diamond_helmet"), 5); //helmet
     * }</pre>
     *
     * @param item item
     * @param slot slot
     * @return this condition model block instance
     */
    MappetModelSettings setSlot(ScriptItemStack item, int slot);

    /**
     * Copies the given model settings to this one.
     *
     * <pre>{@code
     * var world = c.getWorld();
     * var settings = mappet.createModelSettings()
     *     .setEnabled(true)
     *     .setShadowEnabled(false)
     *     .setGlobalEnabled(true)
     *
     * world.getConditionBlock(0, 4, 0).getSettings().set(settings);
     * world.getConditionBlock(0, 5, 0).getSettings().set(settings);
     * }</pre>
     *
     * @param source source model settings
     * @return this condition model block instance
     */
    MappetModelSettings set(MappetModelSettings source);
}
