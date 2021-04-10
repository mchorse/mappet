package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.nodes.Node;

public abstract class EventNode extends Node
{
    public abstract boolean execute(EventContext context);
}