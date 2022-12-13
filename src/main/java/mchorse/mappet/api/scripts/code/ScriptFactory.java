package mchorse.mappet.api.scripts.code;

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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ScriptFactory implements IScriptFactory
{
    @Override
    public IScriptBlockState createBlockState(String blockId, int meta)
    {
        ResourceLocation location = new ResourceLocation(blockId);
        Block block = ForgeRegistries.BLOCKS.getValue(location);

        if (block != null)
        {
            IBlockState state = block.getStateFromMeta(meta);

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
            {}
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
            {}
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
            {}

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

    @Override
    public double random(double max)
    {
        return Math.random()*max;
    }

    @Override
    public double random(double min, double max)
    {
        return min + Math.random()*(max-min);
    }

    public enum colorCodes{
        black("0"),
        dark_blue("1"),
        dark_green("2"),
        dark_aqua("3"),
        dark_red("4"),
        dark_purple("5"),
        gold("6"),
        gray("7"),
        dark_gray("8"),
        blue("9"),
        green("a"),
        aqua("b"),
        red("c"),
        light_purple("d"),
        yellow("e"),
        white("f");

        private final String colorName;

        colorCodes(String colorName){
            this.colorName = "\u00A7"+colorName;
        }
    }
    @Override
    public String getColorCode(String color) {
        return colorCodes.valueOf(color).colorName;
    }

    public enum styleCodes{
        obfuscated("k"),
        bold("l"),
        strikethrough("m"),
        underline("n"),
        italic("o"),
        reset("r");
        private final String styleName;

        styleCodes(String styleName){
            this.styleName = "\u00A7"+ styleName;
        }
    }

    @Override
    public String getStyleCode(String... styles)
    {
        StringBuilder builder = new StringBuilder();
        for(String style : styles)
        {
            builder.append(styleCodes.valueOf(style).styleName);
        }
        return builder.toString();
    }

    @Override
    public String style(String color, String... styles){
        return getColorCode(color)+getStyleCode(styles);
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
}