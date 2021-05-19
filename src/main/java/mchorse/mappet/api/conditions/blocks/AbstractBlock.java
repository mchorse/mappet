package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractBlock implements INBTSerializable<NBTTagCompound>
{
    public boolean not;
    public boolean or;

    public static AbstractBlock create(String type)
    {
        if (type.equals("world_time"))
        {
            return new WorldTimeBlock();
        }

        return null;
    }

    public abstract int getColor();

    public abstract String getType();

    public abstract boolean evaluate(DataContext context);

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("Not", this.not);
        tag.setBoolean("Or", this.or);

        this.serializeNBT(tag);

        return tag;
    }

    public abstract void serializeNBT(NBTTagCompound tag);

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.not = tag.getBoolean("Not");
        this.or = tag.getBoolean("Or");
    }
}