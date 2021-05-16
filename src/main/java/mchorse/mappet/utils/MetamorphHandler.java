package mchorse.mappet.utils;

import mchorse.metamorph.api.events.RegisterBlacklistEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MetamorphHandler
{
    @SubscribeEvent
    public void onMorphBlacklist(RegisterBlacklistEvent event)
    {
        event.blacklist.add("mappet:npc");
    }
}