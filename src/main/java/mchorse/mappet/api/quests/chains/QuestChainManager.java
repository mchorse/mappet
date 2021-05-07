package mchorse.mappet.api.quests.chains;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class QuestChainManager extends BaseManager<QuestChain>
{
    public static final MapNodeFactory FACTORY = new MapNodeFactory()
        .register("quest", QuestNode.class);

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
        }

        return context;
    }

    private void evaluateRecursive(QuestNode node, QuestChain chain, QuestContext context)
    {
        ICharacter character = Character.get(context.player);
        QuestInfo info = null;

        if (character != null && !character.getStates().wasQuestCompleted(node.quest))
        {
            Quest quest = character.getQuests().getByName(node.quest);

            if (quest != null && quest.isComplete(context.player) && node.receiver.equals(context.object))
            {
                info = new QuestInfo(quest, QuestStatus.COMPLETED);
            }
            else if (quest != null)
            {
                info = new QuestInfo(quest, QuestStatus.UNAVAILABLE);
            }

            if (info == null)
            {
                quest = Mappet.quests.load(node.quest);

                if (quest != null)
                {
                    info = new QuestInfo(quest, QuestStatus.AVAILABLE);
                }
            }
        }

        if (info != null)
        {
            context.quests.add(info);

            return;
        }

        for (QuestNode child : chain.getChildren(node))
        {
            this.evaluateRecursive(child, chain, context);
        }
    }
}