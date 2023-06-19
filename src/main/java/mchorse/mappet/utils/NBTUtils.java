package mchorse.mappet.utils;

import mchorse.mappet.common.ScriptedItemProps;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Set;

public class NBTUtils
{
    public static BlockPos blockPosFrom(NBTBase base)
    {
        if (base instanceof NBTTagList && ((NBTTagList) base).tagCount() >= 3)
        {
            NBTTagList list = (NBTTagList) base;
            NBTBase x = list.get(0);
            NBTBase y = list.get(1);
            NBTBase z = list.get(2);

            if (x instanceof NBTPrimitive && y instanceof NBTPrimitive && z instanceof NBTPrimitive)
            {
                return new BlockPos(((NBTPrimitive) x).getInt(), ((NBTPrimitive) y).getInt(), ((NBTPrimitive) z).getInt());
            }
        }

        return null;
    }

    public static NBTBase blockPosTo(BlockPos pos)
    {
        NBTTagList list = new NBTTagList();

        list.appendTag(new NBTTagInt(pos.getX()));
        list.appendTag(new NBTTagInt(pos.getY()));
        list.appendTag(new NBTTagInt(pos.getZ()));

        return list;
    }

    public static Vec3d vec3dFrom(NBTBase base)
    {
        if (base instanceof NBTTagList && ((NBTTagList) base).tagCount() >= 3)
        {
            NBTTagList list = (NBTTagList) base;
            NBTBase x = list.get(0);
            NBTBase y = list.get(1);
            NBTBase z = list.get(2);

            if (x instanceof NBTPrimitive && y instanceof NBTPrimitive && z instanceof NBTPrimitive)
            {
                return new Vec3d(((NBTPrimitive) x).getDouble(), ((NBTPrimitive) y).getDouble(), ((NBTPrimitive) z).getDouble());
            }
        }

        return null;
    }

    public static NBTBase vec3dTo(Vec3d vec)
    {
        NBTTagList list = new NBTTagList();

        list.appendTag(new NBTTagDouble(vec.x));
        list.appendTag(new NBTTagDouble(vec.y));
        list.appendTag(new NBTTagDouble(vec.z));

        return list;
    }

    public static String[] getStringArray(NBTTagList list)
    {
        String[] array = new String[list.tagCount()];

        for (int i = 0; i < list.tagCount(); i++)
        {
            array[i] = list.getStringTagAt(i);
        }

        return array;
    }

    public static void writeStringList(NBTTagList list, Set<String> set)
    {
        set.stream().forEach(string -> list.appendTag(new NBTTagString(string)));
    }

    public static boolean saveScriptedItemProps(ItemStack stack, NBTTagCompound tag)
    {
        if (stack.getItem().equals(Items.AIR))
        {
            return false;
        }

        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (stack.hasTagCompound())
        {
            stack.getTagCompound().setTag("ScriptedItem", tag);

            return true;
        }

        return false;
    }

    public static ScriptedItemProps getScriptedItemProps(ItemStack stack)
    {
        if (stack.getItem().equals(Items.AIR))
        {
            return null;
        }

        if (stack.hasTagCompound())
        {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag.hasKey("ScriptedItem"))
            {
                return new ScriptedItemProps(tag.getCompoundTag("ScriptedItem"));
            }
        }

        return new ScriptedItemProps();
    }

    public static void setScriptedItemProps(ItemStack itemStack, ScriptedItemProps props)
    {
        NBTTagCompound compound = itemStack.getTagCompound();

        // Ensure the ItemStack has a NBTTagCompound
        if (compound == null)
        {
            compound = new NBTTagCompound();
            itemStack.setTagCompound(compound);
        }

        // Write the ScriptedItemProps to the NBTTagCompound
        compound.setTag("ScriptedItem", props.toNBT());
    }
}