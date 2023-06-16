package mchorse.mappet.utils;

import mchorse.mappet.api.conditions.Checker;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class ConditionModel implements INBTSerializable<NBTTagCompound>
{
    public AbstractMorph morph;
    public Checker checker;

    public ConditionModel()
    {
        this.morph = this.getDefaultMorph();
        this.checker = new Checker();
    }

    public AbstractMorph getDefaultMorph()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Name", "blockbuster.fred");

        return MorphManager.INSTANCE.morphFromNBT(tag);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("checker", this.checker.serializeNBT());
        NBTTagCompound morph = new NBTTagCompound();
        this.morph.toNBT(morph);
        tag.setTag("morph", morph);


        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        this.checker.deserializeNBT(nbt.getCompoundTag("checker"));
        this.morph = MorphManager.INSTANCE.morphFromNBT(nbt.getCompoundTag("morph"));
    }

    @Override
    public String toString()
    {
        return "ConditionModel[morph_name:"+morph.name+",condition:"+checker.toString()+"]";
    }
}
