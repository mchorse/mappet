package mchorse.mappet.api.huds;

import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class HUDScene extends AbstractData
{
    public List<HUDMorph> morphs = new ArrayList<HUDMorph>();
    public float fov = 70F;
    public boolean hide;

    @SideOnly(Side.CLIENT)
    public boolean update(boolean allowExpiring)
    {
        this.morphs.removeIf((morph) -> morph.update(allowExpiring));

        return allowExpiring && this.morphs.isEmpty();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList morphs = new NBTTagList();

        for (HUDMorph morph : this.morphs)
        {
            morphs.appendTag(morph.serializeNBT());
        }

        tag.setTag("Morphs", morphs);
        tag.setFloat("Fov", this.fov);
        tag.setBoolean("Hide", this.hide);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Morphs", Constants.NBT.TAG_LIST))
        {
            NBTTagList list = tag.getTagList("Morphs", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i++)
            {
                HUDMorph morph = new HUDMorph();

                morph.deserializeNBT(list.getCompoundTagAt(i));
                this.morphs.add(morph);
            }
        }

        if (tag.hasKey("Fov"))
        {
            this.fov = tag.getFloat("Fov");
        }

        if (tag.hasKey("Hide"))
        {
            this.hide = tag.getBoolean("Hide");
        }
    }
}