package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class DialogueNodesRegisterEvent extends FactoryRegisterEvent<EventNode>
{
    public DialogueNodesRegisterEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}