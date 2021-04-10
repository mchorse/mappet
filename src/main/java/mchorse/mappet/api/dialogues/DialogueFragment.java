package mchorse.mappet.api.dialogues;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class DialogueFragment implements INBTSerializable<NBTTagCompound>
{
    public String text = "";

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Text", this.text);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Text"))
        {
            this.text = tag.getString("Text");
        }
    }
}
