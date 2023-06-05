package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.data.ScriptBox;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.logs.IMappetLogger;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import mchorse.metamorph.api.morphs.AbstractMorph;
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
 * fun main(c: IScriptEvent) {
 *     // Create a diamond hoe using Mappet's factory
 *     val item : IScriptItemStack = mappet.createItem("minecraft:diamond_hoe")
 *
 *     val subject : IScriptEntity = c.getSubject()
 *     subject.setMainItem(item)
 * }
 * }</pre>
 */
public interface IScriptFactory
{
    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val fence : IScriptBlockState = mappet.createBlockState("minecraft:fence", 0)
     *
     *     c.send("Block id: ${fence.getBlockId()}, meta: ${fence.getMeta()}")
     * }
     * }</pre>
     */
    public IScriptBlockState createBlockState(String blockId, int meta);

    /**
     * Create an empty NBT compound.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val tag : INBTCompound = mappet.createCompound()
     *
     *     tag.setString("id", "minecraft:diamond_hoe")
     *     tag.setByte("Count", 1)
     *
     *     val item : IScriptItemStack = mappet.createItem(tag)
     *
     *     c.send(item.serialize().toString())
     * }
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
     * fun main(c: IScriptEvent) {
     *     val tag : INBTCompound = mappet.createCompound("{id:\"minecraft:diamond_hoe\",Count:1b}")
     *     c.send(tag.getString("id"))
     * }
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
     *    //not related to kts
     * }</pre>
     */
    public INBTCompound createCompoundFromJS(Object jsObject);

    /**
     * Create an empty NBT list.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val list = mappet.createList()
     *
     *     list.addInt(1)
     *     list.addInt(2)
     *     list.addInt(3)
     *     list.addInt(4)
     *     list.addInt(5)
     *     list.addInt(6)
     *
     *     // [1,2,3,4,5,6]
     *     c.send(list.stringify().toString())
     * }
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
     * fun main(c: IScriptEvent) {
     *     val list = mappet.createList("[1, 2, 3, 4, 5, 6]")
     *
     *     // [1,2,3,4,5,6]
     *     c.send(list.stringify().toString())
     * }
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
     *    //not related to kts
     * }</pre>
     */
    public INBTList createListFromJS(Object jsObject);

    /**
     * Create an item stack out of string NBT.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createItemNBT("{id:\"minecraft:enchanted_book\",Count:1b,tag:{StoredEnchantments:[{lvl:4s,id:4s}]},Damage:0s}")
     *
     *     // It will output "minecraft:enchanted_book"
     *     c.send(item.getItem().getId())
     * }
     * }</pre>
     *
     * @return an item stack from the string NBT data, or an empty item stack
     *         if the data doesn't have a valid reference to an existing item
     */
    public default IScriptItemStack createItemNBT(String nbt)
    {
        return this.createItem(this.createCompound(nbt));
    }

    /**
     * Create an item stack out of string NBT.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val itemCompound : INBTCompound = mappet.createCompound("{id:\"minecraft:diamond\",Count:1b,Damage:0s}")
     *     val itemStack : IScriptItemStack = mappet.createItem(itemCompound)
     *
     *     c.getSubject().giveItem(itemStack)
     * }
     * }</pre>
     *
     * @return an item stack from the NBT data, or an empty item stack if the
     *         data doesn't have a valid reference to an existing item
     */
    public IScriptItemStack createItem(INBTCompound compound);

    /**
     * Create an item stack with item ID.
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createItem("minecraft:diamond")
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     *         stack if the block doesn't exist
     */
    public default IScriptItemStack createItem(String itemId)
    {
        return this.createItem(itemId, 1);
    }

    /**
     * Create an item stack with item ID, count
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createItem("minecraft:diamond", 64)
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     *         stack if the block doesn't exist
     */
    public default IScriptItemStack createItem(String itemId, int count)
    {
        return this.createItem(itemId, count, 0);
    }

    /**
     * Create an item stack with item ID, count and meta
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createItem("minecraft:dirt", 64, 1)
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     *         stack if the block doesn't exist
     */
    public IScriptItemStack createItem(String itemId, int count, int meta);

    /**
     * Create an item stack with block ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createBlockItem("minecraft:dirt")
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     *          stack if the block doesn't exist
     */
    public default IScriptItemStack createBlockItem(String blockId)
    {
        return this.createItem(blockId, 1);
    }

    /**
     * Create an item stack with block ID and count.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createBlockItem("minecraft:dirt", 64)
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with an item specified by ID, or an empty item
     *         stack if the block doesn't exist
     */
    public default IScriptItemStack createBlockItem(String blockId, int count)
    {
        return this.createItem(blockId, count, 0);
    }

    /**
     * Create an item stack with block ID, count and meta.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val item : IScriptItemStack = mappet.createBlockItem("minecraft:dirt", 64, 1)
     *
     *     c.getSubject().giveItem(item)
     * }
     * }</pre>
     *
     * @return an item stack with block specified by ID, or an empty item
     *         stack if the block doesn't exist
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
     * fun main(c: IScriptEvent) {
     *     val explode : EnumParticleTypes = mappet.getParticleType("explode")
     *     val pos : ScriptVector = c.getSubject().getPosition()
     *
     *     c.getWorld().spawnParticles(explode, true, pos.x, pos.y + 1, pos.z, 50, 0.5, 0.5, 0.5, 0.1)
     * }
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
     * fun main(c: IScriptEvent) {
     *     val slowness : Potion = mappet.getPotion("slowness")
     *     c.getSubject().applyPotion(slowness, 200, 1, false)
     * }
     * }</pre>
     */
    public Potion getPotion(String type);

    /**
     * Create a morph out of string NBT.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val morph : AbstractMorph = mappet.createMorph("{Name:\"blockbuster.alex\"}")
     *
     *     // Assuming c.getSubject() is a player or an NPC
     *     c.getSubject().setMorph(morph)
     * }
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
     * fun main(c: IScriptEvent) {
     *     val tag : INBTCompound = mappet.createCompound()
     *
     *     tag.setString("Name", "blockbuster.alex")
     *
     *     val morph : AbstractMorph = mappet.createMorph(tag)
     *
     *     // Assuming c.getSubject() is a player
     *     c.getSubject().setMorph(morph)
     * }
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
     * fun main(c: IScriptEvent) {
     *     var number = mappet.get("number")
     *
     *     if (number == null)
     *     {
     *         number = 42
     *         mappet.set("number", number)
     *     }
     * }
     * }</pre>
     */
    public Object get(String key);

    /**
     * Set a global arbitrary object during server's existence (other scripts
     * can access this data too).
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     var number = mappet.get("number")
     *
     *     if (number == null)
     *     {
     *         number = 42
     *         mappet.set("number", number)
     *     }
     * }
     * }</pre>
     */
    public void set(String key, Object object);

    /**
     * Dump the simple representation of given non-JS object into the string (to see
     * what fields and methods are available for use).
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     var morph : AbstractMorph = mappet.createMorph("{Name:\"blockbuster.alex\"}")
     *     c.send(mappet.dump(morph))
     * }
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
     * fun main(c: IScriptEvent) {
     *    var morph : AbstractMorph= mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *
     *    c.send(mappet.dump(morph, true));
     * }
     * }</pre>
     *
     * @param simple Whether you want to see simple or full information about
     *               the object.
     */
    public String dump(Object object, boolean simple);

    /**
     * Generate a random number between 0 and the given max value (but not
     * including the maximum value).
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val randomNumber : Double = mappet.random(10.0)
     *     c.send("${randomNumber}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val randomNumber : Double = mappet.random(5.0, 10.0)
     *     c.send("${randomNumber}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val randomNumber = mappet.random(5.0, 10.0, 4141241)
     *     c.send(randomNumber.toString()) //8.016058986062115
     * }
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
     * fun main(c: IScriptEvent) {
     *     val style = mappet.style("dark_blue", "bold", "underline")
     *     c.send(style + "This text is in blue!")
     * }
     * }</pre>
     *
     * @param codes An enumeration of formatting codes.
     */
    public String style(String... codes);

    /**
     * Return a mappet logger instance.
     */
    public IMappetLogger getLogger();

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
     * fun main(c: IScriptEvent) {
     *     val a = mappet.vector2(1.0, 0.0)
     *     val b = mappet.vector2(-1.0, 1.0)
     *
     *     a.normalize()
     *     b.normalize()
     *
     *     c.send("Dot product of a and b is: ${a.dot(b)}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val a = mappet.vector2(25.0, 17.0)
     *     val b = mappet.vector2(a)
     *
     *     b.x += 40
     *     b.y -= 5
     *
     *     val d = mappet.vector2(b)
     *     d.sub(a)
     *
     *     c.send("Distance between a and b is: ${d.length()}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val pos = c.getSubject().getPosition()
     *     val a = mappet.vector3(pos.x, pos.y, pos.z)
     *     val b = mappet.vector3(10.0, 4.0, 50.0)
     *
     *     val d = mappet.vector3(b)
     *     d.sub(a)
     *
     *     c.send("Distance between you and point (10, 4, 50) is: ${d.length()}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val pos = c.getSubject().getPosition()
     *     val a = mappet.vector3(pos.x, pos.y, pos.z)
     *     val b = mappet.vector3(10.0, 4.0, 50.0)
     *     val d = mappet.vector3(b)
     *     d.sub(a)
     *     c.send("Distance between you and point (10, 4, 50) is: ${d.length()}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val v = mappet.vector3(0.0, 0.0, 1.0)
     *     val rotation = mappet.matrix3()
     *
     *     rotation.rotY(Math.PI / 2)
     *     rotation.transform(v)
     *
     *     c.send("Final point is: ${v}")
     * }
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
     * fun main(c: IScriptEvent) {
     *     val v = mappet.vector4(0.0, 0.0, 1.0, 1.0)
     *     val rotation = mappet.matrix4()
     *
     *     rotation.rotY(Math.PI / 2)
     *
     *     val translation = mappet.matrix4()
     *     translation.setTranslation(mappet.vector3(0.0, 4.0, 0.0))
     *     rotation.mul(translation)
     *     rotation.transform(v)
     *
     *     c.send("Final point is: " + v.x + ", " + v.y + ", " + v.z)
     * }
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
     * // import does not work for some reason
     * // but this method can be replaced using ScriptBox
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
     *
     * @return The INBTCompound representation of the object or null if the object is not of the expected types.
     */
    public INBTCompound toNBT(Object object);

}