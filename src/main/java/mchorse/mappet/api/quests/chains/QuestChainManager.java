package mchorse.mappet.api.quests.chains;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class QuestChainManager extends BaseManager<QuestChain>
{
    public QuestChainManager(File folder)
    {
        super(folder);
    }

    @Override
    protected QuestChain createData(String id, NBTTagCompound tag)
    {
        QuestChain chain = new QuestChain(CommonProxy.getChains());

        if (tag != null)
        {
            chain.deserializeNBT(tag);
        }

        return chain;
    }

    public QuestContext evaluate(String id, EntityPlayer player, String subject)
    {
        ICharacter character = Character.get(player);
        QuestContext context = new QuestContext(player, subject);
        QuestChain chain = this.load(id);

        if (character == null || chain == null)
        {
            return context;
        }

        context.data = new DataContext(player);

        for (QuestNode node : chain.getRoots())
        {
            int size = context.quests.size();

            this.evaluateRecursive(context, character, chain, node);

            /* Special case for quest retake */
            if (node.allowRetake && !context.canceled && context.nesting > 0)
            {
                if (context.nesting == context.completed && size == context.quests.size() && node.condition.check(context.data))
                {
                    Quest quest = Mappet.quests.load(node.quest);

                    if (quest != null)
                    {
                        QuestInfo info = this.giveNewQuest(context, character, node, quest);

                        if (info != null)
                        {
                            context.quests.add(info);
                        }
                    }
                }
            }

            context.nesting = 0;
            context.completed = 0;
            context.lastTimesCompleted = 0;
        }

        return context;
    }

    private void evaluateRecursive(QuestContext context, ICharacter character, QuestChain chain, QuestNode node)
    {
        int timesCompleted = character.getStates().getQuestCompletedTimes(node.quest);
        boolean wasCompleted = timesCompleted > 0;
        Quest quest = character.getQuests().getByName(node.quest);
        QuestInfo info = null;

        /* Check present quest */
        if (quest != null)
        {
            if (quest.isComplete(context.player))
            {
                if (context.subject.equals(node.receiver))
                {
                    info = new QuestInfo(quest, QuestStatus.COMPLETED);
                }
                else
                {
                    context.canceled = true;

                    return;
                }
            }
            else if (context.subject.equals(node.giver))
            {
                info = new QuestInfo(quest, QuestStatus.UNAVAILABLE);
            }
        }

        /* Check for possible quests */
        if (info == null)
        {
            quest = Mappet.quests.load(node.quest);

            if (quest != null && this.canTakeQuest(context, node, timesCompleted))
            {
                info = this.giveNewQuest(context, character, node, quest);
            }
        }

        if (info != null)
        {
            context.quests.add(info);
            context.canceled = true;

            return;
        }

        int nesting = context.nesting;
        int completed = context.completed;

        context.canceled = false;

        for (QuestNode child : chain.getChildren(node))
        {
            context.nesting = nesting + 1;
            context.completed = completed + (wasCompleted ? 1 : 0);
            context.lastTimesCompleted = timesCompleted;
            this.evaluateRecursive(context, character, chain, child);
        }
    }

    private boolean canTakeQuest(QuestContext context, QuestNode node, int timesCompleted)
    {
        if (!context.subject.equals(node.giver))
        {
            return false;
        }

        if (!node.condition.check(context.data))
        {
            return false;
        }

        if (node.allowRetake)
        {
            if (context.nesting == 0)
            {
                return timesCompleted == 0;
            }
            else
            {
                return timesCompleted < context.lastTimesCompleted;
            }
        }

        return timesCompleted == 0 && context.nesting == context.completed;
    }

    private QuestInfo giveNewQuest(QuestContext context, ICharacter character, QuestNode node, Quest quest)
    {
        if (node.autoAccept)
        {
            character.getQuests().add(quest, context.player);

            if (context.subject.equals(node.giver))
            {
                return new QuestInfo(quest, QuestStatus.UNAVAILABLE);
            }
        }
        else
        {
            return new QuestInfo(quest, QuestStatus.AVAILABLE);
        }

        context.canceled = true;

        return null;
    }
}