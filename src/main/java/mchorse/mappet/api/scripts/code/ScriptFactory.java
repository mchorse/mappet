package mchorse.mappet.api.scripts.code;

import com.google.common.collect.ImmutableList;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.code.mappet.MappetUIBuilder;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTList;
import mchorse.mappet.api.scripts.user.IScriptFactory;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import mchorse.mappet.api.ui.UI;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.vecmath.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScriptFactory implements IScriptFactory
{
    private static final Map<String, String> formattingCodes = new HashMap<String, String>();

    private Random random = new Random();

    static
    {
        formattingCodes.put("black", "0");
        formattingCodes.put("dark_blue", "1");
        formattingCodes.put("dark_green", "2");
        formattingCodes.put("dark_aqua", "3");
        formattingCodes.put("dark_red", "4");
        formattingCodes.put("dark_purple", "5");
        formattingCodes.put("gold", "6");
        formattingCodes.put("gray", "7");
        formattingCodes.put("dark_gray", "8");
        formattingCodes.put("blue", "9");
        formattingCodes.put("green", "a");
        formattingCodes.put("aqua", "b");
        formattingCodes.put("red", "c");
        formattingCodes.put("light_purple", "d");
        formattingCodes.put("yellow", "e");
        formattingCodes.put("white", "f");
        formattingCodes.put("obfuscated", "k");
        formattingCodes.put("bold", "l");
        formattingCodes.put("strikethrough", "m");
        formattingCodes.put("underline", "n");
        formattingCodes.put("italic", "o");
        formattingCodes.put("reset", "r");
    }

    @Override
    public IScriptBlockState createBlockState(String blockId, int meta)
    {
        ResourceLocation location = new ResourceLocation(blockId);
        Block block = ForgeRegistries.BLOCKS.getValue(location);

        if (block != null)
        {
            Block value = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
            ImmutableList<IBlockState> validStates = value.getBlockState().getValidStates();
            IBlockState state = meta >= 0 && meta < validStates.size() ? validStates.get(meta) : value.getDefaultState();

            return ScriptBlockState.create(state);
        }

        return ScriptBlockState.create(null);
    }

    @Override
    public INBTCompound createCompound(String nbt)
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (nbt != null)
        {
            try
            {
                tag = JsonToNBT.getTagFromJson(nbt);
            }
            catch (Exception e)
            {
            }
        }

        return new ScriptNBTCompound(tag);
    }

    @Override
    public INBTCompound createCompoundFromJS(Object jsObject)
    {
        NBTBase base = this.convertToNBT(jsObject);

        return base instanceof NBTTagCompound ? new ScriptNBTCompound((NBTTagCompound) base) : null;
    }

    @Override
    public INBTList createList(String nbt)
    {
        NBTTagList list = new NBTTagList();

        if (nbt != null)
        {
            try
            {
                list = (NBTTagList) JsonToNBT.getTagFromJson("{List:" + nbt + "}").getTag("List");
            }
            catch (Exception e)
            {
            }
        }

        return new ScriptNBTList(list);
    }

    @Override
    public INBTList createListFromJS(Object jsObject)
    {
        NBTBase base = this.convertToNBT(jsObject);

        return base instanceof NBTTagList ? new ScriptNBTList((NBTTagList) base) : null;
    }

    private NBTBase convertToNBT(Object object)
    {
        if (object instanceof String)
        {
            return new NBTTagString((String) object);
        }
        else if (object instanceof Double)
        {
            return new NBTTagDouble((Double) object);
        }
        else if (object instanceof Integer)
        {
            return new NBTTagInt((Integer) object);
        }
        else if (object instanceof ScriptObjectMirror)
        {
            ScriptObjectMirror mirror = (ScriptObjectMirror) object;

            if (mirror.isArray())
            {
                NBTTagList list = new NBTTagList();

                for (int i = 0, c = mirror.size(); i < c; i++)
                {
                    NBTBase base = this.convertToNBT(mirror.getSlot(i));

                    if (base != null)
                    {
                        list.appendTag(base);
                    }
                }

                return list;
            }
            else
            {
                NBTTagCompound tag = new NBTTagCompound();

                for (String key : mirror.keySet())
                {
                    NBTBase base = this.convertToNBT(mirror.get(key));

                    if (base != null)
                    {
                        tag.setTag(key, base);
                    }
                }

                return tag;
            }
        }

        return null;
    }

    @Override
    public IScriptItemStack createItem(INBTCompound compound)
    {
        if (compound != null)
        {
            return ScriptItemStack.create(new ItemStack(compound.getNBTTagCompound()));
        }

        return ScriptItemStack.EMPTY;
    }

    @Override
    public IScriptItemStack createItem(String itemId, int count, int meta)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));

        return ScriptItemStack.create(new ItemStack(item, count, meta));
    }

    @Override
    public IScriptItemStack createBlockItem(String blockId, int count, int meta)
    {
        Block item = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));

        return ScriptItemStack.create(new ItemStack(item, count, meta));
    }

    @Override
    public EnumParticleTypes getParticleType(String type)
    {
        return EnumParticleTypes.getByName(type);
    }

    @Override
    public Potion getPotion(String type)
    {
        return Potion.getPotionFromResourceLocation(type);
    }

    @Override
    public AbstractMorph createMorph(INBTCompound compound)
    {
        if (compound == null)
        {
            return null;
        }

        return MorphManager.INSTANCE.morphFromNBT(compound.getNBTTagCompound());
    }

    @Override
    public IMappetUIBuilder createUI(String script, String function)
    {
        script = script == null ? "" : script;
        function = function == null ? "" : function;

        return new MappetUIBuilder(new UI(), script, function);
    }

    @Override
    public Object get(String key)
    {
        return Mappet.scripts.objects.get(key);
    }

    @Override
    public void set(String key, Object object)
    {
        Mappet.scripts.objects.put(key, object);
    }

    @Override
    public String dump(Object object, boolean simple)
    {
        if (object instanceof ScriptObjectMirror)
        {
            return object.toString();
        }

        Class<?> clazz = object.getClass();
        StringBuilder output = new StringBuilder(simple ? clazz.getSimpleName() : clazz.getTypeName());

        output.append(" {\n");

        for (Field field : clazz.getDeclaredFields())
        {
            if (Modifier.isStatic(field.getModifiers()))
            {
                continue;
            }

            output.append("    ");

            if (!simple)
            {
                output.append(this.getModifier(field.getModifiers()));
            }

            output.append(field.getName());

            if (!simple)
            {
                output.append(" (");
                output.append(simple ? field.getType().getSimpleName() : field.getType().getTypeName());
                output.append(")");
            }

            String value = "";

            try
            {
                field.setAccessible(true);
                Object o = field.get(object);

                value = o == null ? "null" : o.toString();
            }
            catch (Exception e)
            {
            }

            output.append(": ").append(value).append("\n");
        }

        output.append("\n");

        for (Method method : clazz.getDeclaredMethods())
        {
            if (Modifier.isStatic(method.getModifiers()))
            {
                continue;
            }

            output.append("    ");

            if (!simple)
            {
                output.append(this.getModifier(method.getModifiers()));
            }

            output.append(simple ? method.getReturnType().getSimpleName() : method.getReturnType().getTypeName());
            output.append(" ");
            output.append(method.getName()).append("(");

            int size = method.getParameterCount();

            for (int i = 0; i < size; i++)
            {
                Class<?> arg = method.getParameterTypes()[i];

                output.append(simple ? arg.getSimpleName() : arg.getTypeName());

                if (i < size - 1)
                {
                    output.append(", ");
                }
            }

            output.append(")").append("\n");
        }

        output.append("}");

        return output.toString();
    }

    private String getModifier(int m)
    {
        String modifier = Modifier.isFinal(m) ? "final " : "";

        if (Modifier.isPublic(m))
        {
            modifier += "public ";
        }
        else if (Modifier.isProtected(m))
        {
            modifier += "protected ";
        }
        else if (Modifier.isPrivate(m))
        {
            modifier += "private ";
        }

        return modifier;
    }

    @Override
    public double random(double max)
    {
        return Math.random() * max;
    }

    @Override
    public double random(double min, double max)
    {
        return min + Math.random() * (max - min);
    }

    @Override
    public double random(double min, double max, long seed)
    {
        this.random.setSeed(seed);

        return min + this.random.nextDouble() * (max - min);
    }

    @Override
    public String style(String... styles)
    {
        StringBuilder builder = new StringBuilder();

        for (String style : styles)
        {
            String code = formattingCodes.get(style);

            if (code != null)
            {
                builder.append('\u00A7');
                builder.append(code);
            }
        }

        return builder.toString();
    }

    @Override
    public boolean isPointInBounds(Object point, Object bound1, Object bound2)
    {
        if (point instanceof Vector2d)
        {
            return isPointInBounds2D((Vector2d) point, (Vector2d) bound1, (Vector2d) bound2);
        }
        else if (point instanceof Vector3d)
        {
            return isPointInBounds3D((Vector3d) point, (Vector3d) bound1, (Vector3d) bound2);
        }
        else if (point instanceof Vector4d)
        {
            return isPointInBounds4D((Vector4d) point, (Vector4d) bound1, (Vector4d) bound2);
        }
        else
        {
            throw new IllegalArgumentException("Invalid vector type: " + point.getClass().getName());
        }
    }

    private boolean isPointInBounds2D(Vector2d point, Vector2d bound1, Vector2d bound2) {
        return point.x >= Math.min(bound1.x, bound2.x) && point.x <= Math.max(bound1.x, bound2.x) &&
                point.y >= Math.min(bound1.y, bound2.y) && point.y <= Math.max(bound1.y, bound2.y);
    }

    private boolean isPointInBounds3D(Vector3d point, Vector3d bound1, Vector3d bound2) {
        return point.x >= Math.min(bound1.x, bound2.x) && point.x <= Math.max(bound1.x, bound2.x) &&
                point.y >= Math.min(bound1.y, bound2.y) && point.y <= Math.max(bound1.y, bound2.y) &&
                point.z >= Math.min(bound1.z, bound2.z) && point.z <= Math.max(bound1.z, bound2.z);
    }

    private boolean isPointInBounds4D(Vector4d point, Vector4d bound1, Vector4d bound2) {
        return point.x >= Math.min(bound1.x, bound2.x) && point.x <= Math.max(bound1.x, bound2.x) &&
                point.y >= Math.min(bound1.y, bound2.y) && point.y <= Math.max(bound1.y, bound2.y) &&
                point.z >= Math.min(bound1.z, bound2.z) && point.z <= Math.max(bound1.z, bound2.z) &&
                point.w >= Math.min(bound1.w, bound2.w) && point.w <= Math.max(bound1.w, bound2.w);
    }

    @Override
    public INBTCompound toNBT(Object object)
    {
        if (object instanceof INBTCompound)
        {
            return (INBTCompound) object;
        }

        if (object instanceof NBTBase)
        {
            return new ScriptNBTCompound((NBTTagCompound) object);
        }

        if (object instanceof AbstractMorph)
        {
            return new ScriptNBTCompound(((AbstractMorph) object).toNBT());
        }

        return null;
    }
}