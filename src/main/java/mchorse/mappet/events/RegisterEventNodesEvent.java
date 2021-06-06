package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterEventNodesEvent extends RegisterFactoryEvent<EventNode>
{
    public RegisterEventNodesEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}