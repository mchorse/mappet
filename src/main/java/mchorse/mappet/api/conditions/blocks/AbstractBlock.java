package mchorse.mappet.api.conditions.blocks;

import com.google.common.collect.ImmutableSet;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Set;

public abstract class AbstractBlock implements INBTSerializable<NBTTagCompound>
{
    public static final Set<String> BLOCKS = ImmutableSet.of("state", "world_time");

    public boolean not;
    public boolean or;

    public static AbstractBlock create(String type)
    {
        if (type.equals("state"))
        {
            return new StateBlock();
        }
        else if (type.equals("world_time"))
        {
            return new WorldTimeBlock();
        }

        return null;
    }

    public abstract int getColor();

    public abstract String getType();

    public abstract boolean evaluate(DataContext context);

    public abstract String stringify();

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