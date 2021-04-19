package mchorse.mappet.utils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

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
}