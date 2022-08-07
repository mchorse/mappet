package mchorse.mappet.api.scripts.code;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueObject;
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
    public INBTCompound createCompoundFromJS(Object jsObject) throws JavetException
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
    public INBTList createListFromJS(Object jsObject) throws JavetException
    {
        NBTBase base = this.convertToNBT(jsObject);

        return base instanceof NBTTagList ? new ScriptNBTList((NBTTagList) base) : null;
    }

    private NBTBase convertToNBT(Object object) throws JavetException
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
        else if (object instanceof V8ValueObject)
        {
            V8ValueObject mirror = (V8ValueObject) object;

            if (mirror instanceof V8ValueArray)
            {
                V8ValueArray array = (V8ValueArray) mirror;
                NBTTagList list = new NBTTagList();

                for (int i = 0, c = array.getLength(); i < c; i++)
                {
                    NBTBase base = this.convertToNBT(array.get(i));

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

                for (V8Value key : mirror.getPropertyNames().toArray())
                {
                    NBTBase base = this.convertToNBT(mirror.get(key));

                    if (base != null)
                    {
                        tag.setTag(key.toString(), base);
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
    public IMappetUIBuilder createUI(V8ValueFunction function) throws JavetException {
        function.setWeak();
        return new MappetUIBuilder(new UI(), function);
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
        if (object instanceof V8Value)
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