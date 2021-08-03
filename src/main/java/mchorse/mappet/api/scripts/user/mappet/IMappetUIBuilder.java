package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UITextComponent;

/**
 * This is user interface builder interface. You can create GUIs with this thing.
 *
 * TODO: example
 */
public interface IMappetUIBuilder
{
    public default void setHandler(IScriptEvent event, String function)
    {
        this.setHandler(event.getScript(), function);
    }

    public void setHandler(String script, String function);

    public IMappetUIBuilder background();

    public UIButtonComponent button(String label);

    public UILabelComponent label(String label);

    public UITextComponent text(String text);
}