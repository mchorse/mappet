package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.data.ScriptBox;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.logs.IMappetLogger;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

/**
 * Scripting API factory that allows to initialize/create different stuff.
 *
 * <p>You can access it in the script as <code>mappet</code> global variable. Here is a
 * code example:</p>
 *
 * <pre>{@code
 *    function main(c) {
 *        // Create a diamond hoe using Mappet's factory
 *        var item = mappet.createItem("minecraft:diamond_hoe");
 *
 *        c.getSubject().setMainItem(item);
 *    }
 * }</pre>
 */
public interface IScriptFactory
{
    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}.
     *
     * <pre>{@code
     *    var fence = mappet.createBlockState("minecraft:fence", 0);
     *
     *    // minecraft:fence 0
     *    c.send(fence.getBlockId() + " " + fence.getMeta());
     * }</pre>
     */
    public IScriptBlockState createBlockState(String blockId, int meta);


    /**
     * Create a block state that can with the default meta value.
     *
     * <pre>{@code
     * var fence = mappet.createBlockState("minecraft:fence");
     *
     * // minecraft:fence 0
     * c.send(fence.getBlockId() + " " + fence.getMeta());
     * }</pre>
     */
    IScriptBlockState createBlockState(String blockId);

    /**
     * Create an empty NBT compound.
     *
     * <pre>{@code
     *    var tag = mappet.createCompound();
     *
     *    tag.setString("id", "minecraft:diamond_hoe");
     *    tag.setByte("Count", 1);
     *
     *    var item = mappet.createItemNBT(tag);
     *
     *    // {id:"minecraft:diamond_hoe",Count:1b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     */
    public default INBTCompound createCompound()
    {
        return this.createCompound(null);
    }

    /**
     * Parse an NBT compound data out of given string, if string NBT was
     * invalid then an empty compound will be returned.
     *
     * <pre>{@code
     *    var tag = mappet.createCompound("{id:\"minecraft:diamond_hoe\",Count:1b}");
     *    var item = mappet.createItemNBT(tag);
     *
     *    // {id:"minecraft:diamond_hoe",Count:1b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     */
    public INBTCompound createCompound(String nbt);

    /**
     * Turn a JS object into an NBT compound.
     *
     * <p><b>BEWARE</b>: when converting JS object to NBT keep in mind some
     * limitations of the NBT format:</p>
     *
     * <ul>
     *     <li>NBT supports multiple number storage formats (byte, short, int, long, float,
     *     double) so the converter will only be able to convert numbers to either
     *     integer or double NBT tags, depending on how did you got the number, <code>42</code>
     *     being an integer, and <code>42.0</code> being a double.</li>
     *     <li>NBT lists support only storage of a <b>single type</b> at once, so if you
     *     provide an JS array like <code>[0, 1, 2, "test", {a:1,b:2}, 4, [0, 0, 0], 5.5]</code>
     *     then <b>only the the first element's</b> type will be taken in account, and the
     *     resulted NBT list will turn out like <code>[0.0d, 1.0d, 2.0d, 4.0d, 5.5d]</code>.
     *     <b>In case with numbers</b> if you had first integers, and somewhere in the
     *     middle in the list you got a double, then the integer type <b>will get converted
     *     to double</b>!</li>
     * </ul>
     *
     * <pre>{@code
     *    var tag = mappet.createCompoundFromJS({id:"minecraft:diamond_hoe",Count:1});
     *    var item = mappet.createItemNBT(tag);
     *
     *    // {id:"minecraft:diamond_hoe",Count:1b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     */
    public INBTCompound createCompoundFromJS(Object jsObject);

    /**
     * Create an empty NBT list.
     *
     * <pre>{@code
     *    var list = mappet.createList();
     *
     *    list.addInt(1);
     *    list.addInt(2);
     *    list.addInt(3);
     *    list.addInt(4);
     *    list.addInt(5);
     *    list.addInt(6);
     *
     *    // [1,2,3,4,5,6]
     *    c.send(list.stringify());
     * }</pre>
     */
    public default INBTList createList()
    {
        return this.createList(null);
    }

    /**
     * Parse an NBT list data out of given string, if string NBT was
     * invalid then an empty list will be returned.
     *
     * <pre>{@code
     *    var list = mappet.createList("[1, 2, 3, 4, 5, 6]");
     *
     *    // [1,2,3,4,5,6]
     *    c.send(list.stringify());
     * }</pre>
     */
    public INBTList createList(String nbt);

    /**
     * Turn a JS object into an NBT compound.
     *
     * <p><b>Read carefully the description</b> of {@link #createCompoundFromJS(Object)}
     * for information about JS to NBT object conversion limitations!</p>
     *
     * <pre>{@code
     *    var list = mappet.createListFromJS([1, 2, 3, 4, 5, 6]);
     *
     *    // [1,2,3,4,5,6]
     *    c.send(list.stringify());
     * }</pre>
     */
    public INBTList createListFromJS(Object jsObject);

    /**
     * Create an item stack out of string NBT.
     *
     * <pre>{@code
     *    var item = mappet.createItemNBT("{id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:4s}]},Damage:0s}");
     *
     *    // It will output "minecraft:enchanted_book"
     *    c.send(item.getItem().getId());
     * }</pre>
     *
     * @return an item stack from the string NBT data, or an empty item stack
     * if the data doesn't have a valid reference to an existing item
     */
    public default IScriptItemStack createItemNBT(String nbt)
    {
        return this.createItem(this.createCompound(nbt));
    }

    /**
     * Create an item stack out of string NBT.
     *
     * <pre>{@code
     *    var tag = mappet.createCompound("{id:\"minecraft:diamond_hoe\",Count:1b}");
     *    var item = mappet.createItemNBT(tag);
     *
     *    // {id:"minecraft:diamond_hoe",Count:1b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     *
     * @return an item stack from the NBT data, or an empty item stack if the
     * data doesn't have a valid reference to an existing item
     */
    public IScriptItemStack createItem(INBTCompound compound);

    /**
     * Create an item stack with item ID.
     *
     * <pre>{@code
     *    var item = mappet.createItem("minecraft:diamond");
     *
     *    // {id:"minecraft:diamond",Count:1b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public default IScriptItemStack createItem(String itemId)
    {
        return this.createItem(itemId, 1);
    }

    /**
     * Create an item stack with item ID, count
     *
     * <pre>{@code
     *    var item = mappet.createItem("minecraft:diamond", 64);
     *
     *    // {id:"minecraft:diamond",Count:64b,Damage:0s}
     *    c.send(item.serialize());
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public default IScriptItemStack createItem(String itemId, int count)
    {
        return this.createItem(itemId, count, 0);
    }

    /**
     * Create an item stack with item ID, count and meta
     *
     * <pre>{@code
     *    var damaged_hoe = mappet.createItem("minecraft:diamond_hoe", 64, 5);
     *
     *    // {id:"minecraft:diamond_hoe",Count:64b,Damage:5s}
     *    c.send(damaged_hoe.serialize());
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public IScriptItemStack createItem(String itemId, int count, int meta);

    /**
     * Create an item stack with block ID.
     *
     * <pre>{@code
     *    var stone = mappet.createBlockItem("minecraft:stone");
     *
     *    // {id:"minecraft:stone",Count:1b,Damage:0s}
     *    c.send(stone.serialize());
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public default IScriptItemStack createBlockItem(String blockId)
    {
        return this.createItem(blockId, 1);
    }

    /**
     * Create an item stack with block ID and count.
     *
     * <pre>{@code
     *    var stone = mappet.createBlockItem("minecraft:stone", 64);
     *
     *    // {id:"minecraft:stone",Count:64b,Damage:0s}
     *    c.send(stone.serialize());
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public default IScriptItemStack createBlockItem(String blockId, int count)
    {
        return this.createItem(blockId, count, 0);
    }

    /**
     * Create an item stack with block ID, count and meta.
     *
     * <pre>{@code
     *    var andesite = mappet.createBlockItem("minecraft:stone", 64, 5);
     *
     *    // {id:"minecraft:stone",Count:64b,Damage:5s}
     *    c.send(andesite.serialize());
     * }</pre>
     *
     * @return an item stack with block specified by ID, or an empty item
     * stack if the block doesn't exist
     */
    public IScriptItemStack createBlockItem(String blockId, int count, int meta);

    /**
     * Get Minecraft particle type by its name.
     *
     * <p>You can find out all of the particle types by typing in <code>/particle</code>
     * command, and looking up the completion of the first argument (i.e. press tab after
     * typing in <code>/particle</code> and a space).</p>
     *
     * <pre>{@code
     *    var explode = mappet.getParticleType("explode");
     *    var pos = c.getSubject().getPosition();
     *
     *    c.getWorld().spawnParticles(explode, true, pos.x, pos.y + 1, pos.z, 50, 0.5, 0.5, 0.5, 0.1);
     * }</pre>
     */
    public EnumParticleTypes getParticleType(String type);

    /**
     * Get Minecraft potion effect by its name.
     *
     * <p>You can find out all of the particle types by typing in <code>/effect</code>
     * command, and looking up the completion of the second argument (i.e. press tab after
     * typing in <code>/particle Player</code> and a space).</p>
     *
     * <pre>{@code
     *    var slowness = mappet.getPotion("slowness");
     *
     *    c.getSubject().applyPotion(slowness, 200, 1, false);
     * }</pre>
     */
    public Potion getPotion(String type);

    /**
     * Create a morph out of string NBT.
     *
     * <pre>{@code
     *    var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *
     *    // Assuming c.getSubject() is a player
     *    c.getSubject().setMorph(morph);
     * }</pre>
     */
    public default AbstractMorph createMorph(String nbt)
    {
        return this.createMorph(this.createCompound(nbt));
    }

    /**
     * Create a morph out of NBT.
     *
     * <pre>{@code
     *    var tag = mappet.createCompound();
     *
     *    tag.setString("Name", "blockbuster.alex");
     *
     *    var morph = mappet.createMorph(tag);
     *
     *    // Assuming c.getSubject() is a player
     *    c.getSubject().setMorph(morph);
     * }</pre>
     */
    public AbstractMorph createMorph(INBTCompound compound);

    /**
     * Create a UI. You can send it to the player by using
     * {@link IScriptPlayer#openUI(IMappetUIBuilder)} method.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI().background();
     *        var label = ui.label("Hello, world!").background(0x88000000);
     *
     *        label.rxy(0.5, 0.5).wh(80, 20).anchor(0.5).labelAnchor(0.5);
     *
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     */
    public default IMappetUIBuilder createUI()
    {
        return this.createUI("", "");
    }

    /**
     * Create a UI with a script handler. You can send it to the
     * player by using {@link IScriptPlayer#openUI(IMappetUIBuilder)} method.
     *
     *
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").background();
     *        var label = ui.label("Hello, world!").background(0x88000000);
     *        var button = ui.button("Push me!").id("button");
     *
     *        label.rxy(0.5, 0.5).wh(80, 20).anchor(0.5).labelAnchor(0.5);
     *        label.rx(0.5).ry(0.5, 25).wh(80, 20).anchor(0.5);
     *
     *        c.getSubject().openUI(ui);
     *    }
     *
     *    function handler(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *
     *        if (uiContext.getLast() === "button")
     *        {
     *            // Button was pressed
     *        }
     *    }
     * }</pre>
     *
     * @param event Script event (whose script ID will be used for UI's user input handler).
     * @param function Given script's function that will be used as UI's user input handler.
     */
    public default IMappetUIBuilder createUI(IScriptEvent event, String function)
    {
        return this.createUI(event.getScript(), function);
    }

    /**
     * Create a UI with a script handler. You can send it to the
     * player by using {@link IScriptPlayer#openUI(IMappetUIBuilder)} method.
     *
     * <p>Script and function arguments allow to point to the function in some
     * script, which it will be responsible for handling the user input from
     * scripted UI.</p>
     *
     * <p>In the UI handler, you can access subject's UI context ({@link IMappetUIContext})
     * which has all the necessary methods to handle user's input.</p>
     *
     * <pre>{@code
     *    // ui.js
     *    function main(c)
     *    {
     *        var ui = mappet.createUI("handler", "main").background();
     *        var label = ui.label("Hello, world!").background(0x88000000);
     *        var button = ui.button("Push me!").id("button");
     *
     *        label.rxy(0.5, 0.5).wh(80, 20).anchor(0.5).labelAnchor(0.5);
     *        label.rx(0.5).ry(0.5, 25).wh(80, 20).anchor(0.5);
     *
     *        c.getSubject().openUI(ui);
     *    }
     *
     *    // handler.js
     *    function main(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *
     *        if (uiContext.getLast() === "button")
     *        {
     *            // Button was pressed
     *        }
     *    }
     * }</pre>
     *
     * @param script The script which will be used as UI's user input handler.
     * @param function Given script's function that will be used as UI's user input handler.
     */
    public IMappetUIBuilder createUI(String script, String function);

    /**
     * Get a global arbitrary object.
     *
     * <pre>{@code
     *    var number = mappet.get("number");
     *
     *    if (number === null || number === undefined)
     *    {
     *        number = 42;
     *        mappet.set("number", number);
     *    }
     * }</pre>
     */
    public Object get(String key);

    /**
     * Set a global arbitrary object during server's existence (other scripts
     * can access this data too).
     *
     * <pre>{@code
     *    var number = mappet.get("number");
     *
     *    if (number === null || number === undefined)
     *    {
     *        number = 42;
     *        mappet.set("number", number);
     *    }
     * }</pre>
     */
    public void set(String key, Object object);

    /**
     * Dump the simple representation of given non-JS object into the string (to see
     * what fields and methods are available for use).
     *
     * <pre>{@code
     *    var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *
     *    c.send(mappet.dump(morph));
     * }</pre>
     */
    public default String dump(Object object)
    {
        return this.dump(object, true);
    }

    /**
     * Dump given non-JS object into the string (to see what fields and methods are
     * available for use).
     *
     * <pre>{@code
     *    var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *
     *    c.send(mappet.dump(morph, true));
     * }</pre>
     *
     * @param simple Whether you want to see simple or full information about
     * the object.
     */
    public String dump(Object object, boolean simple);

    /**
     * Generate a random number between 0 and the given max value (but not
     * including the maximum value).
     *
     * <pre>{@code
     *    var randomNumber = mappet.random(10);
     *
     *    c.send(randomNumber);
     * }</pre>
     *
     * @param max Maximum value.
     */
    public double random(double max);

    /**
     * Generate a random number between the given min value and the given max value
     * (but not including the maximum value).
     *
     * <pre>{@code
     *    var randomNumber = mappet.random(5, 10);
     *
     *    c.send(randomNumber);
     * }</pre>
     *
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public double random(double min, double max);

    /**
     * Generate a random number between the given min value and the given max value
     * (but not including the maximum value) with given seed.
     *
     * <pre>{@code
     *    var randomNumber = mappet.random(5, 10, 4141241);
     *
     *    c.send(randomNumber);
     * }</pre>
     *
     * @param min Minimum value.
     * @param max Maximum value.
     */
    public double random(double min, double max, long seed);

    /**
     * Return Minecraft's formatting code.
     *
     * <p>Following colors are supported: black, dark_blue, dark_green, dark_aqua,
     * dark_red, dark_purple, gold, gray, dark_gray, blue, green, aqua, red,
     * light_purple, yellow, white</p>
     *
     * <p>Following styles are supported: obfuscated, bold, strikethrough, underline,
     * italic, reset.</p>
     *
     * <pre>{@code
     *    var style = mappet.style("dark_blue", "bold", "underline");
     *
     *    c.send(style + "This text is in blue!");
     * }</pre>
     *
     * @param codes An enumeration of formatting codes.
     */
    public String style(String... codes);

    /**
     * Return a mappet logger instance.
     */
    public IMappetLogger getLogger();

    /**
     * Return a mappet entity/player/npc by given minecraft entity.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var s = c.getSubject();
     *     var minecraftPlayer = s.minecraftPlayer;
     *     var mappetPlayer = mappet.getMappetEntity(minecraftPlayer);
     *     c.send(mappetPlayer.name);
     * }
     * }</pre>
     */
    public IScriptEntity getMappetEntity(Entity minecraftEntity);

    /* Vector math */

    /**
     * Create a ScriptVector.
     */
    public default ScriptVector vector(double x, double y, double z)
    {
        return new ScriptVector(x, y, z);
    }

    /**
     * Create an empty (0, 0) 2D vector.
     */
    public default Vector2d vector2()
    {
        return new Vector2d();
    }

    /**
     * Create a 2D vector.
     *
     * <pre>{@code
     *    var a = mappet.vector2(1, 0);
     *    var b = mappet.vector2(-1, 1);
     *
     *    a.normalize();
     *    b.normalize();
     *
     *    c.send("Dot product of a and b is: " + a.dot(b));
     * }</pre>
     */
    public default Vector2d vector2(double x, double y)
    {
        return new Vector2d(x, y);
    }

    /**
     * Copy a 2D vector.
     *
     * <pre>{@code
     *    var a = mappet.vector2(25, 17);
     *    var b = mappet.vector2(a);
     *
     *    b.x += 40;
     *    b.y -= 5;
     *
     *    var d = mappet.vector2(b);
     *
     *    d.sub(a);
     *
     *    c.send("Distance between a and b is: " + d.length());
     * }</pre>
     */
    public default Vector2d vector2(Vector2d v)
    {
        return new Vector2d(v);
    }

    /**
     * Create an empty (0, 0, 0) 3D vector.
     */
    public default Vector3d vector3()
    {
        return new Vector3d();
    }

    /**
     * Create a 3D vector.
     *
     * <pre>{@code
     *    var look = c.getSubject().getLook();
     *    var a = mappet.vector3(look.x, look.y, look.z);
     *    var b = mappet.vector3(0, 0, 1);
     *
     *    a.normalize();
     *    b.normalize();
     *
     *    c.send("Dot product of entity's look vector and positive Z is: " + a.dot(b));
     * }</pre>
     */
    public default Vector3d vector3(double x, double y, double z)
    {
        return new Vector3d(x, y, z);
    }

    /**
     * Copy a 3D vector.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *    var a = mappet.vector3(pos.x, pos.y, pos.z);
     *    var b = mappet.vector3(10, 4, 50);
     *
     *    var d = mappet.vector3(b);
     *
     *    d.sub(a);
     *
     *    c.send("Distance between you and point (10, 4, 50) is: " + d.length());
     * }</pre>
     */
    public default Vector3d vector3(Vector3d v)
    {
        return new Vector3d(v);
    }

    /**
     * Create a 4D vector.
     */
    public default Vector4d vector4()
    {
        return new Vector4d();
    }

    /**
     * Create a 4D vector.
     */
    public default Vector4d vector4(double x, double y, double z, double w)
    {
        return new Vector4d(x, y, z, w);
    }

    /**
     * Copy a 4D vector.
     */
    public default Vector4d vector4(Vector4d v)
    {
        return new Vector4d(v);
    }

    /**
     * Create an identity 3x3 matrix.
     *
     * <pre>{@code
     *    var v = mappet.vector3(0, 0, 1);
     *    var rotation = mappet.matrix3();
     *
     *    rotation.rotY(Math.PI / 2);
     *    rotation.transform(v);
     *
     *    c.send("Final point is: " + v);
     * }</pre>
     */
    public default Matrix3d matrix3()
    {
        Matrix3d m = new Matrix3d();

        m.setIdentity();

        return m;
    }

    /**
     * Copy a 3x3 matrix.
     */
    public default Matrix3d matrix3(Matrix3d m)
    {
        return new Matrix3d(m);
    }

    /**
     * Create an identity 4x4 matrix.
     *
     * <pre>{@code
     *    var v = mappet.vector4(0, 0, 1, 1);
     *    var rotation = mappet.matrix4();
     *
     *    rotation.rotY(Math.PI / 2);
     *
     *    var translation = mappet.matrix4();
     *
     *    translation.setTranslation(mappet.vector3(0, 4, 0));
     *    rotation.mul(translation);
     *    rotation.transform(v);
     *
     *    c.send("Final point is: " + v.x + ", " + v.y + ", " + v.z);
     * }</pre>
     */
    public default Matrix4d matrix4()
    {
        Matrix4d m = new Matrix4d();

        m.setIdentity();

        return m;
    }

    /**
     * Copy a 4x4 matrix.
     */
    public default Matrix4d matrix4(Matrix4d m)
    {
        return new Matrix4d(m);
    }

    /**
     * Create a bounding box.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var subject = c.getSubject();
     *     var subjectPosition = subject.getPosition();
     *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
     *     if (box.contains(subjectPosition)){
     *         c.send("the player in in the box")
     *     }
     * }
     * }</pre>
     */
    public default ScriptBox box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        return new ScriptBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Determines whether a point is located inside a bounding volume specified by two corners.
     * This method works with different vector types (2D, 3D, and 4D).
     *
     * <pre>{@code
     *   var pos = c.getSubject().getPosition();
     *   var point = mappet.vector3(pos.x, pos.y, pos.z);
     *   var bound1 = mappet.vector3(0, 0, 0);
     *   var bound2 = mappet.vector3(10, 10, 10);
     *   var isInside = mappet.isPointInBounds(point, bound1, bound2);
     *   c.send("Is the point inside the bounding volume? " + isInside);
     * }</pre>
     *
     * @param point The position of the point to check.
     * @param bound1 The position of one corner of the bounding volume.
     * @param bound2 The position of the opposite corner of the bounding volume.
     * @return true if the point is inside the bounding volume, false otherwise.
     * @throws IllegalArgumentException if the input vectors have different dimensions.
     */
    public boolean isPointInBounds(Object point, Object bound1, Object bound2);

    /**
     * Converts an object to an INBTCompound representation.
     *
     * @param object The object to convert to an INBTCompound.
     * @return The INBTCompound representation of the object or null if the object is not of the expected types.
     */
    public INBTCompound toNBT(Object object);

    /**
     * Formates strings (placeholders).
     *
     * <pre>{@code
     * // Example:
     * var name = "Steve";
     * var age = 18;
     * var message = mappet.format("Hello %s, you are %d years old!", name, age);
     * c.send(message);
     *
     * // You can also use the positional arguments:
     * var s = c.getSubject();
     * var pos = s.getPosition();
     * var message = mappet.format("Hello %1$s, you are in x:%2$.2f, y:%3$.2f, z:%4$.2f!", s.getName(), pos.x, pos.y, pos.z);
     * s.send(message);
     * }</pre>
     *
     * @param format string to format
     * @param args arguments to replace
     * @return formatted string
     */
    String format(String format, Object... args);
}