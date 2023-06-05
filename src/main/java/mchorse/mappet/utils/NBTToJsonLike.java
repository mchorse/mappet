package mchorse.mappet.utils;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * NBT to JSON-like utility
 *
 * This is successor of {@link NBTToJsonLike} which is better because
 * it also stores the data type of the
 */
public class NBTToJsonLike
{
    public static String toJson(NBTBase base)
    {
        return toJson(base, new StringBuilder(), "").toString();
    }

    /**
     * Write given NBT base element to JSON like format (which
     * preserves NBT data types)
     */
    public static StringBuilder toJson(NBTBase base, StringBuilder builder, String indent)
    {
        if (base instanceof NBTTagList)
        {
            NBTTagList list = (NBTTagList) base;

            builder.append("[\n");

            for (int i = 0; i < list.tagCount(); i++)
            {
                builder.append(indent);
                builder.append("    ");

                toJson(list.get(i), builder, indent + "    ");

                if (i < list.tagCount() - 1)
                {
                    builder.append(",");
                }

                builder.append("\n");
            }

            builder.append(indent);
            builder.append("]");
        }
        else if (base instanceof NBTTagCompound)
        {
            NBTTagCompound tag = (NBTTagCompound) base;

            builder.append("{\n");

            int i = 0;

            for (String key : tag.getKeySet())
            {
                builder.append(indent);
                builder.append("    \"");
                builder.append(key);
                builder.append("\": ");

                toJson(tag.getTag(key), builder, indent + "    ");

                if (i < tag.getSize() - 1)
                {
                    builder.append(",");
                }

                builder.append("\n");

                i++;
            }

            builder.append(indent);
            builder.append("}");
        }
        else
        {
            builder.append(base.toString());
        }

        return builder;
    }

    /**
     * From JSON (but it can only do a NBT tag compound)
     */
    public static NBTTagCompound fromJson(String json)
    {
        try
        {
            return JsonToNBT.getTagFromJson(json);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Write NBT tag compound into a file
     */
    public static void write(File file, NBTTagCompound tag) throws IOException
    {
        FileUtils.writeStringToFile(file, NBTToJsonLike.toJson(tag), Utils.getCharset());
    }

    /**
     * Read NBT tag compound out of file
     */
    public static NBTTagCompound read(File file) throws IOException
    {
        String json = FileUtils.readFileToString(file, Utils.getCharset());

        return NBTToJsonLike.fromJson(json);
    }
}