package mchorse.mappet.events;

import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterQuestChainNodeEvent extends RegisterFactoryEvent<QuestNode>
{
    public RegisterQuestChainNodeEvent(MapFactory<QuestNode> factory)
    {
        super(factory);
    }
}