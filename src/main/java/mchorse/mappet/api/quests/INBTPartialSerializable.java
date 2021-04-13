package mchorse.mappet.api.quests;

import net.minecraft.nbt.NBTTagCompound;

public interface INBTPartialSerializable
{
    public NBTTagCompound partialSerializeNBT();

    public void partialDeserializeNBT(NBTTagCompound tag);
}