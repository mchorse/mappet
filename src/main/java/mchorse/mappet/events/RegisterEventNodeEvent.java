package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterEventNodeEvent extends RegisterFactoryEvent<EventNode>
{
    public RegisterEventNodeEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}