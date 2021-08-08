package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UIClickComponent;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.ui.components.UIGraphicsComponent;
import mchorse.mappet.api.ui.components.UIIconButtonComponent;
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
    public UIComponent getCurrent();

    public IMappetUIBuilder background();

    public default IMappetUIBuilder notClosable()
    {
        return this.closable(false);
    }

    public IMappetUIBuilder closable(boolean closable);

    public UIComponent create(String id);

    public UIGraphicsComponent graphics();

    public UIButtonComponent button(String label);

    public UIIconButtonComponent icon(String icon);

    public UILabelComponent label(String label);

    public UITextComponent text(String text);

    public default UITextboxComponent textbox()
    {
        return this.textbox("");
    }

    public default UITextboxComponent textbox(String text)
    {
        return this.textbox(text, 32);
    }

    public UITextboxComponent textbox(String text, int maxLength);

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

    public UIClickComponent click();

    public IMappetUIBuilder layout();

    public default IMappetUIBuilder column(int margin)
    {
        return this.column(margin, 0);
    }

    public IMappetUIBuilder column(int margin, int padding);

    public default IMappetUIBuilder row(int margin)
    {
        return this.row(margin, 0);
    }

    public IMappetUIBuilder row(int margin, int padding);

    public default IMappetUIBuilder grid(int margin)
    {
        return this.grid(margin, 0);
    }

    public IMappetUIBuilder grid(int margin, int padding);
}