package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.components.IUIComponent;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UILayoutComponent;
import mchorse.mappet.api.ui.components.UIStackComponent;
import mchorse.mappet.api.ui.components.UIStringListComponent;
import mchorse.mappet.api.ui.components.UITextComponent;
import mchorse.mappet.api.ui.components.UITextboxComponent;
import mchorse.mappet.api.ui.components.UIToggleComponent;
import mchorse.mappet.api.ui.components.UITrackpadComponent;

import java.util.List;

public class MappetUIBuilder implements IMappetUIBuilder
{
    private UI ui;
    private IUIComponent current;
    private String script;
    private String function;

    public MappetUIBuilder(UI ui, String script, String function)
    {
        this.ui = ui;
        this.current = ui.root;
        this.script = script;
        this.function = function;
    }

    public MappetUIBuilder(IUIComponent component)
    {
        this.current = component;
    }

    @Override
    public IUIComponent getCurrent()
    {
        return this.current;
    }

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
    public IMappetUIBuilder background()
    {
        if (this.ui != null)
        {
            this.ui.background = true;
        }

        return this;
    }

    @Override
    public UIButtonComponent button(String label)
    {
        UIButtonComponent element = new UIButtonComponent();

        this.current.getChildComponents().add(element);
        element.label(label);

        return element;
    }

    @Override
    public UILabelComponent label(String label)
    {
        UILabelComponent element = new UILabelComponent();

        this.current.getChildComponents().add(element);
        element.label(label);

        return element;
    }

    @Override
    public UITextComponent text(String text)
    {
        UITextComponent element = new UITextComponent();

        this.current.getChildComponents().add(element);
        element.label(text);

        return element;
    }

    @Override
    public UITextboxComponent textbox(String text)
    {
        UITextboxComponent element = new UITextboxComponent();

        this.current.getChildComponents().add(element);
        element.label(text);

        return element;
    }

    @Override
    public UIToggleComponent toggle(String label, boolean state)
    {
        UIToggleComponent element = new UIToggleComponent();

        this.current.getChildComponents().add(element);
        element.state(state).label(label);

        return element;
    }

    @Override
    public UITrackpadComponent trackpad(int value)
    {
        UITrackpadComponent element = new UITrackpadComponent();

        this.current.getChildComponents().add(element);
        element.value(value);

        return element;
    }

    @Override
    public UIStringListComponent stringList(List<String> values, int selected)
    {
        UIStringListComponent element = new UIStringListComponent();

        this.current.getChildComponents().add(element);
        element.values(values);

        if (selected >= 0)
        {
            element.selected(selected);
        }

        return element;
    }

    @Override
    public UIStackComponent item(IScriptItemStack stack)
    {
        UIStackComponent element = new UIStackComponent();

        this.current.getChildComponents().add(element);

        if (stack != null && !stack.isEmpty())
        {
            element.stack(stack.getMinecraftItemStack());
        }

        return element;
    }

    @Override
    public IMappetUIBuilder layout()
    {
        return new MappetUIBuilder(this.layout(0, 0));
    }

    public UILayoutComponent layout(int margin, int padding)
    {
        UILayoutComponent layout = new UILayoutComponent();

        layout.margin = margin;
        layout.padding = padding;
        this.current.getChildComponents().add(layout);

        return layout;
    }

    @Override
    public IMappetUIBuilder column(int margin, int padding)
    {
        UILayoutComponent layout = this.layout(margin, padding);

        layout.layoutType = UILayoutComponent.LayoutType.COLUMN;

        return new MappetUIBuilder(layout);
    }

    @Override
    public IMappetUIBuilder row(int margin, int padding)
    {
        UILayoutComponent layout = this.layout(margin, padding);

        layout.layoutType = UILayoutComponent.LayoutType.ROW;

        return new MappetUIBuilder(layout);
    }

    @Override
    public IMappetUIBuilder grid(int margin, int padding)
    {
        UILayoutComponent layout = this.layout(margin, padding);

        layout.layoutType = UILayoutComponent.LayoutType.GRID;

        return new MappetUIBuilder(layout);
    }
}