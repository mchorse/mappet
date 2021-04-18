package mchorse.mappet.api.npcs;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class Npc implements INBTSerializable<NBTTagCompound>
{
    public Map<String, NpcState> states = new HashMap<String, NpcState>();
    public boolean unique;

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

        if (this.unique)
        {
            tag.setBoolean("Unique", this.unique);
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

        if (tag.hasKey("Unique"))
        {
            this.unique = tag.getBoolean("Unique");
        }
    }
}
