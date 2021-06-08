package mchorse.mappet.events;

import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterTriggerBlockEvent extends RegisterFactoryEvent<AbstractTriggerBlock>
{
    public RegisterTriggerBlockEvent(MapFactory<AbstractTriggerBlock> factory)
    {
        super(factory);
    }
}