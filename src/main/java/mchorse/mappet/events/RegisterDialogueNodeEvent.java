package mchorse.mappet.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterDialogueNodeEvent extends RegisterFactoryEvent<EventNode>
{
    public RegisterDialogueNodeEvent(MapFactory<EventNode> factory)
    {
        super(factory);
    }
}