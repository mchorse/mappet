package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterEventNodeEvent extends RegisterFactoryEvent<EventBaseNode>
{
    public RegisterEventNodeEvent(MapFactory<EventBaseNode> factory)
    {
        super(factory);
    }
}