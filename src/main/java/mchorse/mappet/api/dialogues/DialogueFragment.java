package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class DialogueFragment implements INBTSerializable<NBTTagCompound>
{
    public String text = "";
    public int color = 0xffffff;

    public static String process(String text)
    {
        return TextUtils.processColoredText(text.replace("\\n", "\n"));
    }

    public String getProcessedText()
    {
        return process(this.text);
    }

    public DialogueFragment copy()
    {
        DialogueFragment fragment = new DialogueFragment();

        fragment.text = this.text;
        fragment.color = this.color;

        return fragment;
    }

    public DialogueFragment process(DataContext context)
    {
        this.text = context.process(this.text);

        return this;
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
