package mchorse.mappet.events;

import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterQuestChainNodesEvent extends RegisterFactoryEvent<QuestNode>
{
    public RegisterQuestChainNodesEvent(MapFactory<QuestNode> factory)
    {
        super(factory);
    }
}