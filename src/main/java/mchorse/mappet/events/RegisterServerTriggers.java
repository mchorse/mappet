package mchorse.mappet.events;

import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.utils.Trigger;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RegisterServerTriggers extends Event
{
    private final ServerSettings settings;

    public RegisterServerTriggers(ServerSettings settings)
    {
        this.settings = settings;
    }

    public void register(String key, Trigger trigger)
    {
        this.settings.register(key, trigger);
    }
}