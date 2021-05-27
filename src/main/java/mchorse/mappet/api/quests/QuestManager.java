package mchorse.mappet.api.quests;

import mchorse.mappet.api.utils.manager.BaseManager;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class QuestManager extends BaseManager<Quest>
{
    public QuestManager(File folder)
    {
        super(folder);
    }

    @Override
    protected Quest createData(NBTTagCompound tag)
    {
        Quest quest = new Quest();

        if (tag != null)
        {
            quest.deserializeNBT(tag);
        }

        return quest;
    }
}