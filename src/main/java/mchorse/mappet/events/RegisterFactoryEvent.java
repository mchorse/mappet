package mchorse.mappet.events;

import mchorse.mappet.api.utils.factory.MapFactory;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class RegisterFactoryEvent <T> extends Event
{
    private MapFactory<T> factory;

    public RegisterFactoryEvent(MapFactory<T> factory)
    {
        this.factory = factory;
    }

    public MapFactory<T> getFactory()
    {
        return this.factory;
    }
}