package mchorse.mappet.api.quests;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Quests implements INBTSerializable<NBTTagCompound>
{
    public Map<String, Quest> quests = new LinkedHashMap<String, Quest>();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, Quest> entry : this.quests.entrySet())
        {
            tag.setTag(entry.getKey(), entry.getValue().serializeNBT());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        for (String key : tag.getKeySet())
        {
            Quest quest = new Quest();

            quest.deserializeNBT(tag.getCompoundTag(key));
            this.quests.put(key, quest);
        }
    }

    public boolean add(String id, Quest quest, EntityPlayer player)
    {
        if (this.has(id))
        {
            return false;
        }

        this.quests.put(id, quest);
        quest.accept.trigger(player);

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

        return false;
    }

    public boolean has(String id)
    {
        return this.quests.containsKey(id);
    }

    public Quest getByName(String id)
    {
        return this.quests.get(id);
    }
}