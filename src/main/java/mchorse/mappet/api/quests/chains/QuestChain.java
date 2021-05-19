package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.factory.IFactory;

public class QuestChain extends NodeSystem<QuestNode>
{
    public QuestChain(IFactory<QuestNode> factory)
    {
        super(factory);
    }
}