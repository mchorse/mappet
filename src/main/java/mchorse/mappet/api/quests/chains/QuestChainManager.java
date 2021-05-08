package mchorse.mappet.api.quests.chains;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import sun.security.x509.AVA;

import java.io.File;

public class QuestChainManager extends BaseManager<QuestChain>
{
    public static final MapNodeFactory FACTORY = new MapNodeFactory().register("quest", QuestNode.class);

    public QuestChainManager(File folder)
    {
        super(folder);
    }

    @Override
    protected QuestChain createData(NBTTagCompound tag)
    {
        QuestChain chain = new QuestChain(FACTORY);

        if (tag != null)
        {
            chain.deserializeNBT(tag);
        }

        return chain;
    }

    public QuestContext evaluate(String id, EntityPlayer player, String subject)
    {
        QuestContext context = new QuestContext(player, subject);
        QuestChain chain = this.load(id);

        if (chain == null)
        {
            return context;
        }

        for (QuestNode node : chain.getRoots())
        {
            this.evaluateRecursive(node, chain, context);
            context.nesting = 0;
            context.completed = 0;
        }

        return context;
    }

    private void evaluateRecursive(QuestNode node, QuestChain chain, QuestContext context)
    {
        ICharacter character = Character.get(context.player);

        if (character == null)
        {
            return;
        }

        boolean wasCompleted = character.getStates().wasQuestCompleted(node.quest);
        QuestInfo info = null;

        Quest quest = character.getQuests().getByName(node.quest);

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
                    return;
                }
            }
            else if (context.subject.equals(node.giver))
            {
                info = new QuestInfo(quest, QuestStatus.UNAVAILABLE);
            }
        }

        if (info == null && !wasCompleted)
        {
            quest = Mappet.quests.load(node.quest);

            if (quest != null && context.subject.equals(node.giver) && context.nesting == context.completed)
            {
                info = new QuestInfo(quest, QuestStatus.AVAILABLE);
            }
        }

        if (info != null)
        {
            context.quests.add(info);

            return;
        }

        context.nesting += 1;
        context.completed += wasCompleted ? 1 : 0;

        for (QuestNode child : chain.getChildren(node))
        {
            this.evaluateRecursive(child, chain, context);
        }

        context.completed -= wasCompleted ? 1 : 0;
        context.nesting -= 1;
    }
}