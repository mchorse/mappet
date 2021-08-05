package mchorse.mappet.events;

import mchorse.mappet.api.ui.components.IUIComponent;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterUIComponentEvent extends RegisterFactoryEvent<IUIComponent>
{
    public RegisterUIComponentEvent(MapFactory<IUIComponent> factory)
    {
        super(factory);
    }
}