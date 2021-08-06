package mchorse.mappet.events;

import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.utils.factory.MapFactory;

public class RegisterUIComponentEvent extends RegisterFactoryEvent<UIComponent>
{
    public RegisterUIComponentEvent(MapFactory<UIComponent> factory)
    {
        super(factory);
    }
}