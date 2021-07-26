package mchorse.mappet.api.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBlock implements INBTSerializable<NBTTagCompound>
{
    @SideOnly(Side.CLIENT)
    public abstract String stringify();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

    protected abstract void serializeNBT(NBTTagCompound tag);
}