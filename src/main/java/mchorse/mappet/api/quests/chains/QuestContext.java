package mchorse.mappet.api.quests.chains;

import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class QuestContext
{
    public EntityPlayer player;
    public String subject;
    public List<QuestInfo> quests = new ArrayList<QuestInfo>();

    public int nesting;
    public int completed;

    public QuestContext(EntityPlayer player, String subject)
    {
        this.player = player;
        this.subject = subject;
    }
}