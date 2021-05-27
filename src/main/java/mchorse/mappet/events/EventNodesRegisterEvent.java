package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class EventNodesRegisterEvent extends FactoryRegisterEvent<EventNode>
{
    public EventNodesRegisterEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}