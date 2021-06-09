package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterDialogueNodeEvent extends RegisterFactoryEvent<EventBaseNode>
{
    public RegisterDialogueNodeEvent(MapFactory<EventBaseNode> factory)
    {
        super(factory);
    }
}