package mchorse.mappet.api.scripts.user;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.util.EnumParticleTypes;

/**
 * Scripting API factory that allows to initialize/create different stuff.
 */
public interface IScriptFactory
{
    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}
     */
    public IScriptBlockState createBlockState(String blockId, int meta);

    /**
     * Create an empty NBT compound
     */
    public default INBTCompound createCompound()
    {
        return this.createCompound(null);
    }

    /**
     * Parse an NBT compound data out of given string, if string NBT was
     * invalid then an empty compound will be returned
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
     *     double) so the converter will only convert numbers to either integer or
     *     double NBT tags, depending on how did you got the number, <code>42</code> being
     *     an integer, and <code>42.0</code> being a double.</li>
     *     <li>NBT lists support only storage of a <b>single type</b> at once, so if you
     *     provide an JS array like <code>[0, 1, 2, "test", {a:1,b:2}, 4, [0, 0, 0], 5.5]</code>
     *     then <b>only the the first element's</b> type will be taken in account, and the
     *     resulted NBT list will turn out like <code>[0.0d, 1.0d, 2.0d, 4.0d, 5.5d]</code>.
     *     <b>In case with numbers</b> if you had first integers, and somewhere in the
     *     middle in the list you got a double, then the integer type <b>will get converted
     *     to double</b>!</li>
     * </ul>
     */
    public INBTCompound createCompoundFromJS(Object jsObject);

    /**
     * Create an empty NBT list
     */
    public default INBTList createList()
    {
        return this.createList(null);
    }

    /**
     * Parse an NBT list data out of given string, if string NBT was
     * invalid then an empty list will be returned
     */
    public INBTList createList(String nbt);

    /**
     * Turn a JS object into an NBT compound.
     *
     * <p><b>Read carefully the description</b> of {@link #createCompoundFromJS(Object)}
     * for information about JS to NBT object conversion!</p>
     */
    public INBTList createListFromJS(Object jsObject);

    /**
     * Create an item stack out of string NBT
     */
    public default IScriptItemStack createItemStack(String nbt)
    {
        return this.createItemStack(this.createCompound(nbt));
    }

    /**
     * Create an item stack out of string NBT
     */
    public IScriptItemStack createItemStack(INBTCompound compound);

    /**
     * Get Minecraft particle type by its name
     */
    public EnumParticleTypes getParticleType(String type);

    /**
     * Create a morph out of string NBT
     */
    public default AbstractMorph createMorph(String nbt)
    {
        return this.createMorph(this.createCompound(nbt));
    }

    /**
     * Create a morph out of NBT
     */
    public AbstractMorph createMorph(INBTCompound compound);
}