package mchorse.mappet.api.factions;

import mchorse.mappet.api.utils.BaseManager;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class FactionManager extends BaseManager<Faction>
{
    public FactionManager(File folder)
    {
        super(folder);
    }

    @Override
    public Faction create(NBTTagCompound tag)
    {
        Faction faction = new Faction();

        if (tag != null)
        {
            faction.deserializeNBT(tag);
        }

        return faction;
    }
}