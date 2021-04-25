package mchorse.mappet.api.factions;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FactionRelation implements INBTSerializable<NBTTagCompound>
{
    public List<Threshold> thresholds = new ArrayList<Threshold>();

    public FactionRelation()
    {
        this.thresholds.add(new Threshold(100, FactionAttitude.AGGRESSIVE, "Hostile", 0xee2222));
        this.thresholds.add(new Threshold(200, FactionAttitude.PASSIVE, "Neutral", 0xaaaaaa));
        this.thresholds.add(new Threshold(1000, FactionAttitude.FRIENDLY, "Friendly", 0x22ee22));
    }

    public FactionAttitude getAttitude(int score)
    {
        Threshold threshold = this.get(score);

        return threshold == null ? FactionAttitude.PASSIVE : threshold.attitude;
    }

    public Threshold get(int score)
    {
        for (Threshold threshold : this.thresholds)
        {
            if (score < threshold.score)
            {
                return threshold;
            }
        }

        return this.thresholds.isEmpty() ? null : this.thresholds.get(this.thresholds.size() - 1);
    }

    public void normalize()
    {
        this.thresholds.sort(Comparator.comparingInt(a -> a.score));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        this.normalize();

        for (Threshold threshold : this.thresholds)
        {
            list.appendTag(threshold.serializeNBT());
        }

        tag.setTag("Thresholds", list);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.thresholds.clear();

        if (tag.hasKey("Thresholds"))
        {
            NBTTagList list = tag.getTagList("Thresholds", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound compound = list.getCompoundTagAt(i);
                Threshold threshold = new Threshold();

                threshold.deserializeNBT(compound);
                this.thresholds.add(threshold);
            }
        }

        this.normalize();
    }

    /**
     * Threshold of a relation
     */
    public static class Threshold implements INBTSerializable<NBTTagCompound>
    {
        public int score;
        public FactionAttitude attitude = FactionAttitude.PASSIVE;

        public String title = "";
        public int color = 0xffffff;

        public Threshold()
        {}

        public Threshold(int score, FactionAttitude attitude, String title, int color)
        {
            this.score = score;
            this.attitude = attitude;

            this.title = title;
            this.color = color;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound tag = new NBTTagCompound();

            tag.setInteger("Score", this.score);
            tag.setString("Attitude", this.attitude.name());
            tag.setString("Title", this.title);
            tag.setInteger("Color", this.color);

            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound tag)
        {
            this.score = tag.getInteger("Score");
            this.attitude = FactionAttitude.get(tag.getString("Attitude"));
            this.title = tag.getString("Title");
            this.color = tag.getInteger("Color");
        }
    }
}