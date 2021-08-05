package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.ui.components.IUIComponent;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UIMorphComponent;
import mchorse.mappet.api.ui.components.UIStackComponent;
import mchorse.mappet.api.ui.components.UIStringListComponent;
import mchorse.mappet.api.ui.components.UITextComponent;
import mchorse.mappet.api.ui.components.UITextareaComponent;
import mchorse.mappet.api.ui.components.UITextboxComponent;
import mchorse.mappet.api.ui.components.UIToggleComponent;
import mchorse.mappet.api.ui.components.UITrackpadComponent;
import mchorse.metamorph.api.morphs.AbstractMorph;

import java.util.List;

/**
 * This is user interface builder interface. You can create GUIs with this thing.
 *
 * TODO: example
 */
public interface IMappetUIBuilder
{
    public IUIComponent getCurrent();

    public IMappetUIBuilder background();

    public IUIComponent create(String id);

    public UIButtonComponent button(String label);

    public UILabelComponent label(String label);

    public UITextComponent text(String text);

    public default UITextboxComponent textbox()
    {
        return this.textbox("");
    }

    public UITextboxComponent textbox(String text);

    public default UITextareaComponent textarea()
    {
        return this.textarea("");
    }

    public UITextareaComponent textarea(String text);

    public default UIToggleComponent toggle(String label)
    {
        return this.toggle(label, false);
    }

    public UIToggleComponent toggle(String label, boolean state);

    public default UITrackpadComponent trackpad()
    {
        return this.trackpad(0);
    }

    public UITrackpadComponent trackpad(int value);

    public default UIStringListComponent stringList(List<String> values)
    {
        return this.stringList(values, -1);
    }

    public UIStringListComponent stringList(List<String> values, int selected);

    public default UIStackComponent item()
    {
        return this.item(null);
    }

    public UIStackComponent item(IScriptItemStack stack);

    public default UIMorphComponent morph(AbstractMorph morph)
    {
        return this.morph(morph, false);
    }

    public UIMorphComponent morph(AbstractMorph morph, boolean editing);

    public IMappetUIBuilder layout();

    public IMappetUIBuilder column(int margin, int padding);

    public IMappetUIBuilder row(int margin, int padding);

    public IMappetUIBuilder grid(int margin, int padding);
}