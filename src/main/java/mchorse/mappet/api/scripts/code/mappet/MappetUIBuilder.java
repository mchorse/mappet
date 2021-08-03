package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;

public class MappetUIBuilder implements IMappetUIBuilder
{
    private UI ui = new UI();

    @Override
    public UI getUI()
    {
        return this.ui;
    }

    @Override
    public UIButtonComponent button(String label)
    {
        UIButtonComponent element = new UIButtonComponent();

        this.ui.root.getChildComponents().add(element);
        element.label(label);

        return element;
    }

    @Override
    public UILabelComponent label(String label)
    {
        UILabelComponent element = new UILabelComponent();

        this.ui.root.getChildComponents().add(element);
        element.label(label);

        return element;
    }
}