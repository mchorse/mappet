package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.user.items.IScriptItemStack;


/**
 * EntityItem interface.
 *
 * <p>This interface represents an item entity.</p>
 */
public interface IScriptEntityItem extends IScriptEntity
{
    /**
     * Get entity's age.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     c.getSubject().send("Item's age: " + entityItem.getAge());
     * }
     * }</pre>
     * @return how many ticks entity exists
     */
    public int getAge();

    /**
     * Set entity's age.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     entityItem.setAge(5000);
     *     c.getSubject().send("Item's age: " + entityItem.getAge());
     * }
     * }</pre>
     *
     * @param age ticks
     */
    public void setAge(int age);

    /**
     * Get entity's pickup delay.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     c.getSubject().send("You can pick up this item in " + entityItem.getPickupDelay() + " ticks.");
     * }
     * }</pre>
     * @return How many ticks remains until someone can pick up this item
     */
    public int getPickupDelay();

    /**
     * Set entity's pickup delay.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     entityItem.setPickupDelay(100); // 5 seconds
     *     c.getSubject().send("You can pick up this item in " + entityItem.getPickupDelay() + " ticks.");
     * }
     * }</pre>
     *
     * @param delay In ticks
     */
    public void setPickupDelay(int delay);

    /**
     * Get entity's lifespan (max age).
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     c.getSubject().send("Item disappear in " + (entityItem.getLifespan() - entityItem.getAge()) + " ticks.");
     * }
     * }</pre>
     * @return How many ticks remains until someone can pick up this item
     */
    public int getLifespan();

    /**
     * Set entity's lifespan (max age).
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     entityItem.setLifespan(1200); // 60 seconds
     *     c.getSubject().send("Item disappear in " + (entityItem.getLifespan() - entityItem.getAge()) + " ticks.");
     * }
     * }</pre>
     *
     * @param lifespan In ticks
     */
    public void setLifespan(int lifespan);

    /**
     * Get entity's owner nickname (Who can pick up this item).
     * Returns empty string if anyone able to pick it up.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     c.getSubject().send(entityItem.getOwner());
     * }
     * }</pre>
     * @return Nickname of player who can pick up this item.
     */
    public String getOwner();

    /**
     * Set entity's owner nickname (Who can pick up this item).
     * use with empty string, if you want anyone to be able to pick it up.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     entityItem.setOwner(c.getSubject().getName());
     * }
     * }</pre>
     */
    public void setOwner(String owner);

    /**
     * Get entity's thrower nickname (Who throw this item).
     * Returns empty string if it spawns through code.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     c.getSubject().send(entityItem.getThrower());
     * }
     * }</pre>
     * @return Nickname of player who throw this item.
     */
    public String getThrower();

    /**
     * Set entity's thrower nickname (Who throw this item).
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond_hoe");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *     entityItem.setThrower(c.getSubject().getName());
     * }
     * }</pre>
     */
    public void setThrower(String thrower);

    /**
     * Get itemStack from this entity.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var pos = c.getSubject().getPosition();
     *     var entities = c.getWorld().getEntities(pos.x, pos.y + 1, pos.z, 3);
     *
     *     for (var i in entities)
     *     {
     *         var entity = entities[i];
     *
     *         if (entity.isItem())
     *         {
     *             c.player.send(entity.getItem().getItem().getId());
     *         }
     *     }
     * }
     * }</pre>
     */
    public IScriptItemStack getItem();

    /**
     * Get itemStack from this entity.
     *
     * <pre>{@code
     * // Midas :D
     * // Turns any item in radius of 3 blocks into golden nuggets.
     * function main(c)
     * {
     *     var pos = c.getSubject().getPosition();
     *     var entities = c.getWorld().getEntities(pos.x, pos.y + 1, pos.z, 3);
     *
     *     for (var i in entities)
     *     {
     *         var entity = entities[i];
     *
     *         if (entity.isItem())
     *         {
     *             entity.setItem(mappet.createItem("minecraft:golden_nugget"))
     *         }
     *     }
     * }
     * }</pre>
     */
    public void setItem(IScriptItemStack itemStack);

    /**
     * Makes item unaffordable.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *
     *     entityItem.setNoDespawn();
     *     entityItem.setInfinitePickupDelay();
     * }
     * }</pre>
     */
    public void setInfinitePickupDelay();

    /**
     * Set's default pick up delay (10, actually).
     */
    public void setDefaultPickupDelay();

    /**
     * Entity will not despawn.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var item = mappet.createItem("minecraft:diamond");
     *     var pos = c.getSubject().getPosition();
     *
     *     var entityItem = c.getWorld().dropItemStack(item, pos.x, pos.y + 3, pos.z);
     *
     *     entityItem.setNoDespawn();
     *     entityItem.setInfinitePickupDelay();
     * }
     * }</pre>
     */
    public void setNoDespawn();

    /**
     * Returns whether it's possible to pick up this item, or not.
     *
     * <pre>{@code
     * // Item magnet :D
     * function main(c)
     * {
     *     var player = c.getSubject();
     *     var pos = player.getPosition();
     *     var entities = c.getWorld().getEntities(pos.x, pos.y + 1, pos.z, 10);
     *
     *     for (var i in entities)
     *     {
     *         var entity = entities[i];
     *
     *         if (entity.isItem() && entity.canPickup())
     *         {
     *             player.giveItem(entity.getItem());
     *             entity.remove(); // despawn
     *         }
     *     }
     * }
     * }</pre>
     */
    public boolean canPickup();
}
