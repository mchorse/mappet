package mchorse.mappet.api.dialogues;

import mchorse.mclib.utils.TextUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class DialogueFragment implements INBTSerializable<NBTTagCompound>
{
    public String text = "";
    public int color = 0xffffff;

    public String getProcessedText()
    {
        return TextUtils.processColoredText(this.text.replace("\\n", "\n"));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Text", this.text);
        tag.setInteger("Color", this.color);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Text"))
        {
            this.text = tag.getString("Text");
        }

        if (tag.hasKey("Color"))
        {
            this.color = tag.getInteger("Color");
        }
    }
}
