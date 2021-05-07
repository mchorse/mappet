package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.nodes.factory.INodeFactory;

public class QuestChain extends NodeSystem<QuestNode>
{
    public QuestChain(INodeFactory<QuestNode> factory)
    {
        super(factory);
    }
}