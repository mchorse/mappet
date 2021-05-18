package mchorse.mappet.api.npcs;

import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class Npc extends AbstractData
{
    public Map<String, NpcState> states = new HashMap<String, NpcState>();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagCompound states = new NBTTagCompound();

        for (Map.Entry<String, NpcState> entry : this.states.entrySet())
        {
            NBTTagCompound state = entry.getValue().serializeNBT();

            if (state.getSize() > 0)
            {
                states.setTag(entry.getKey(), state);
            }
        }

        if (states.getSize() > 0)
        {
            tag.setTag("States", states);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("States", Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound states = tag.getCompoundTag("States");

            for (String key : states.getKeySet())
            {
                NpcState state = new NpcState();

                state.deserializeNBT(states.getCompoundTag(key));
                this.states.put(key, state);
            }
        }
    }
}