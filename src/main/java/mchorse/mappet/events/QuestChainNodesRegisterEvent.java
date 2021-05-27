package mchorse.mappet.events;

import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class QuestChainNodesRegisterEvent extends FactoryRegisterEvent<QuestNode>
{
    public QuestChainNodesRegisterEvent(MapFactory<QuestNode> factory)
    {
        super(factory);
    }
}