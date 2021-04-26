package mchorse.mappet.api.npcs;

import mchorse.mappet.api.utils.BaseManager;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class NpcManager extends BaseManager<Npc>
{
    public NpcManager(File folder)
    {
        super(folder);
    }

    @Override
    public Npc create(String id, NBTTagCompound tag)
    {
        Npc npc = new Npc();

        if (tag != null)
        {
            npc.deserializeNBT(tag);
        }

        return npc;
    }
}