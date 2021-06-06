package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterDialogueNodesEvent extends RegisterFactoryEvent<EventNode>
{
    public RegisterDialogueNodesEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}