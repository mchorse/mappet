package mchorse.mappet.api.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.quests.PacketQuest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Quests implements INBTSerializable<NBTTagCompound>
{
    public Map<String, Quest> quests = new LinkedHashMap<String, Quest>();
    public boolean iterating;
    public List<Quest> toAdd = new ArrayList<Quest>(2);

    public void initiate(EntityPlayer player)
    {
        for (Quest quest : this.quests.values())
        {
            quest.initiate(player);
        }
    }

    public boolean add(Quest quest, EntityPlayer player)
    {
        if (this.has(quest.getId()))
        {
            return false;
        }

        if (this.iterating)
        {
            this.toAdd.add(quest);

            return true;
        }

        this.quests.put(quest.getId(), quest);
        quest.initiate(player);
        quest.accept.trigger(player);

        if (player instanceof EntityPlayerMP)
        {
            Dispatcher.sendTo(new PacketQuest(quest.getId(), quest), (EntityPlayerMP) player);
        }

        return true;
    }

    public boolean complete(String id, EntityPlayer player)
    {
        return this.remove(id, player, true);
    }

    public boolean decline(String id, EntityPlayer player)
    {
        return this.remove(id, player, false);
    }

    public boolean remove(String id, EntityPlayer player, boolean reward)
    {
        Quest quest = this.quests.remove(id);

        if (quest == null)
        {
            return false;
        }

        if (reward)
        {
            quest.reward(player);
        }
        else
        {
            quest.decline.trigger(player);
        }

        if (player instanceof EntityPlayerMP)
        {
            Dispatcher.sendTo(new PacketQuest(id, null), (EntityPlayerMP) player);
        }

        return true;
    }

    public boolean has(String id)
    {
        return this.quests.containsKey(id);
    }

    public Quest getByName(String id)
    {
        return this.quests.get(id);
    }

    public void copy(Quests quests)
    {
        this.quests.clear();

        for (Map.Entry<String, Quest> entry : quests.quests.entrySet())
        {
            Quest quest = Mappet.quests.load(entry.getKey());

            quest.partialDeserializeNBT(entry.getValue().partialSerializeNBT());
            this.quests.put(entry.getKey(), quest);
        }
    }

    public void flush(EntityPlayer player)
    {
        if (this.iterating)
        {
            this.iterating = false;

            for (Quest quest : this.toAdd)
            {
                this.add(quest, player);
            }

            this.toAdd.clear();
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, Quest> entry : this.quests.entrySet())
        {
            tag.setTag(entry.getKey(), entry.getValue().partialSerializeNBT());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        for (String key : tag.getKeySet())
        {
            Quest quest = Mappet.quests.load(key);

            if (quest != null)
            {
                quest.partialDeserializeNBT(tag.getCompoundTag(key));
                this.quests.put(key, quest);
            }
        }
    }
}