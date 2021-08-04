package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UITextComponent;
import mchorse.mappet.api.ui.components.UITextboxComponent;
import mchorse.mappet.api.ui.components.UIToggleComponent;
import mchorse.mappet.api.ui.components.UITrackpadComponent;

public class MappetUIBuilder implements IMappetUIBuilder
{
    private UI ui = new UI();
    private String script;
    private String function;

    public UI getUI()
    {
        return this.ui;
    }

    public String getScript()
    {
        return this.script;
    }

    public String getFunction()
    {
        return this.function;
    }

    @Override
    public void setHandler(String script, String function)
    {
        this.script = script;
        this.function = function;
    }

    @Override
    public IMappetUIBuilder background()
    {
        this.ui.background = true;

        return this;
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

    @Override
    public UITextComponent text(String text)
    {
        UITextComponent element = new UITextComponent();

        this.ui.root.getChildComponents().add(element);
        element.label(text);

        return element;
    }

    @Override
    public UITextboxComponent textbox(String text)
    {
        UITextboxComponent element = new UITextboxComponent();

        this.ui.root.getChildComponents().add(element);
        element.label(text);

        return element;
    }

    @Override
    public UIToggleComponent toggle(String label, boolean state)
    {
        UIToggleComponent element = new UIToggleComponent();

        this.ui.root.getChildComponents().add(element);
        element.state(state).label(label);

        return element;
    }

    @Override
    public UITrackpadComponent trackpad(int value)
    {
        UITrackpadComponent element = new UITrackpadComponent();

        this.ui.root.getChildComponents().add(element);
        element.value(value);

        return element;
    }
}