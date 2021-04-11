package mchorse.mappet.api.quests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Quests implements INBTSerializable<NBTTagList>
{
    public List<Quest> quests = new ArrayList<Quest>();

    @Override
    public NBTTagList serializeNBT()
    {
        NBTTagList list = new NBTTagList();

        for (Quest quest : this.quests)
        {
            list.appendTag(quest.serializeNBT());
        }

        return list;
    }

    @Override
    public void deserializeNBT(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); i++)
        {
            Quest quest = new Quest();

            quest.deserializeNBT(list.getCompoundTagAt(i));
            this.quests.add(quest);
        }
    }

    public boolean add(Quest quest)
    {
        if (this.has(quest.getId()))
        {
            return false;
        }

        this.quests.add(quest);

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
        Iterator<Quest> it = this.quests.iterator();

        while (it.hasNext())
        {
            Quest quest = it.next();

            if (quest.getId().equals(id))
            {
                if (reward)
                {
                    quest.reward(player);
                }

                it.remove();

                return true;
            }
        }

        return false;
    }

    public boolean has(String id)
    {
        for (Quest quest : this.quests)
        {
            if (quest.getId().equals(id))
            {
                return true;
            }
        }

        return false;
    }

    public Quest getByName(String id)
    {
        for (Quest quest : this.quests)
        {
            if (quest.getId().equals(id))
            {
                return quest;
            }
        }

        return null;
    }
}