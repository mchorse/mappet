package mchorse.mappet.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import net.minecraftforge.fml.common.eventhandler.Event;

public class StateChangedEvent extends Event
{
    public final States states;
    public final String key;
    public final Double previous;
    public final Double current;

    public StateChangedEvent(States states, String key, Double previous, Double current)
    {
        this.states = states;
        this.key = key;
        this.previous = previous;
        this.current = current;
    }

    public boolean isGlobal()
    {
        return this.states == Mappet.states;
    }
}