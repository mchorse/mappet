package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.api.utils.nodes.NodeSystem;

public class QuestChain extends NodeSystem<QuestNode>
{
    public QuestChain(IFactory<QuestNode> factory)
    {
        super(factory);
    }
}