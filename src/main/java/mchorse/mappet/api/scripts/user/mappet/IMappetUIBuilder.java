package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;

/**
 * This is user interface builder interface. You can create GUIs with this thing.
 *
 * TODO: example
 */
public interface IMappetUIBuilder
{
    public UI getUI();

    public UIButtonComponent button(String label);

    public UILabelComponent label(String label);
}