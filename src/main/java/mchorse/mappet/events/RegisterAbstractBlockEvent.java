package mchorse.mappet.events;

import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterAbstractBlockEvent extends RegisterFactoryEvent<AbstractBlock>
{
    public RegisterAbstractBlockEvent(MapFactory<AbstractBlock> factory)
    {
        super(factory);
    }
}