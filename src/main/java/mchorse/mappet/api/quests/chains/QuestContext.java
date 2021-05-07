package mchorse.mappet.api.quests.chains;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class QuestContext
{
    public EntityPlayer player;
    public String object;
    public List<QuestInfo> quests = new ArrayList<QuestInfo>();

    public QuestContext(EntityPlayer player, String object)
    {
        this.player = player;
        this.object = object;
    }
}