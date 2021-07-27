package mchorse.mappet.api.hud;

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

    @SideOnly(Side.CLIENT)
    public boolean update()
    {
        this.morphs.removeIf(HUDMorph::update);

        return this.morphs.isEmpty();
    }

    public void fill(List<HUDMorph> perspective, List<HUDMorph> ortho)
    {
        for (HUDMorph morph : this.morphs)
        {
            (morph.ortho ? ortho : perspective).add(morph);
        }
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
    }
}