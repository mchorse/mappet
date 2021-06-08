package mchorse.mappet.events;

import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterConditionBlockEvent extends RegisterFactoryEvent<AbstractConditionBlock>
{
    public RegisterConditionBlockEvent(MapFactory<AbstractConditionBlock> factory)
    {
        super(factory);
    }
}