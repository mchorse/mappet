package mchorse.mappet.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

import java.util.Map;

public class NBTToJson
{
    public static JsonElement toJson(NBTBase base)
    {
        if (base instanceof NBTTagByte)
        {
            byte value = ((NBTTagByte) base).getByte();

            return value < 2 ? new JsonPrimitive(value == 1) : new JsonPrimitive(value);
        }
        else if (base instanceof NBTTagShort)
        {
            return new JsonPrimitive(((NBTTagShort) base).getShort());
        }
        else if (base instanceof NBTTagInt)
        {
            return new JsonPrimitive(((NBTTagInt) base).getInt());
        }
        else if (base instanceof NBTTagLong)
        {
            return new JsonPrimitive(((NBTTagLong) base).getLong());
        }
        else if (base instanceof NBTTagFloat)
        {
            return new JsonPrimitive(((NBTTagFloat) base).getFloat());
        }
        else if (base instanceof NBTTagDouble)
        {
            return new JsonPrimitive(((NBTTagDouble) base).getDouble());
        }
        else if (base instanceof NBTTagString)
        {
            return new JsonPrimitive(((NBTTagString) base).getString());
        }
        else if (base instanceof NBTTagList)
        {
            JsonArray array = new JsonArray();
            NBTTagList list = (NBTTagList) base;

            for (int i = 0; i < list.tagCount(); i++)
            {
                array.add(toJson(list.get(i)));
            }

            return array;
        }
        else if (base instanceof NBTTagCompound)
        {
            JsonObject object = new JsonObject();
            NBTTagCompound tag = (NBTTagCompound) base;

            for (String key : tag.getKeySet())
            {
                object.add(key, toJson(tag.getTag(key)));
            }

            return object;
        }

        return JsonNull.INSTANCE;
    }

    public static NBTBase fromJson(JsonElement element)
    {
        if (element.isJsonPrimitive())
        {
            JsonPrimitive primitive = element.getAsJsonPrimitive();

            if (primitive.isString())
            {
                return new NBTTagString(primitive.getAsString());
            }
            else if (primitive.isNumber())
            {
                Number number = primitive.getAsNumber();

                if (number instanceof Byte)
                {
                    return new NBTTagByte(number.byteValue());
                }
                else if (number instanceof Short)
                {
                    return new NBTTagShort(number.shortValue());
                }
                else if (number instanceof Integer)
                {
                    return new NBTTagInt(number.intValue());
                }
                else if (number instanceof Long)
                {
                    return new NBTTagLong(number.longValue());
                }
                else if (number instanceof Float)
                {
                    return new NBTTagFloat(number.floatValue());
                }

                return new NBTTagDouble(number.doubleValue());
            }
            else if (primitive.isBoolean())
            {
                return new NBTTagByte(primitive.getAsBoolean() ? (byte) 1 : (byte) 0);
            }
        }
        else if (element.isJsonArray())
        {
            JsonArray array = element.getAsJsonArray();
            NBTTagList list = new NBTTagList();

            for (JsonElement child : array)
            {
                NBTBase base = fromJson(child);

                if (base != null)
                {
                    list.appendTag(base);
                }
            }

            return list;
        }
        else if (element.isJsonObject())
        {
            JsonObject object = element.getAsJsonObject();
            NBTTagCompound compound = new NBTTagCompound();

            for (Map.Entry<String, JsonElement> entry : object.entrySet())
            {
                NBTBase base = fromJson(entry.getValue());

                if (base != null)
                {
                    compound.setTag(entry.getKey(), base);
                }
            }

            return compound;
        }

        return null;
    }
}