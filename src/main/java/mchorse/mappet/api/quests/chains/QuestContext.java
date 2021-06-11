package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.DataContext;
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
    public int lastTimesCompleted;
    public boolean canceled;

    public DataContext data;

    public QuestContext(EntityPlayer player, String subject)
    {
        this.player = player;
        this.subject = subject;
    }
}