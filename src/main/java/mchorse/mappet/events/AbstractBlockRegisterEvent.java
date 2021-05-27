package mchorse.mappet.events;

import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mappet.api.utils.factory.MapFactory;

public class AbstractBlockRegisterEvent extends FactoryRegisterEvent<AbstractBlock>
{
    public AbstractBlockRegisterEvent(MapFactory<AbstractBlock> factory)
    {
        super(factory);
    }
}