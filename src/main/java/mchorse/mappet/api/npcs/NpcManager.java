package mchorse.mappet.api.npcs;

import mchorse.mappet.api.utils.manager.BaseManager;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class NpcManager extends BaseManager<Npc>
{
    public NpcManager(File folder)
    {
        super(folder);
    }

    @Override
    protected Npc createData(String id, NBTTagCompound tag)
    {
        Npc npc = new Npc();

        if (tag != null)
        {
            npc.deserializeNBT(tag);
        }
        else
        {
            npc.states.put("default", new NpcState());
        }

        return npc;
    }
}