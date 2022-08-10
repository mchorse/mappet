package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UIClickComponent;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.ui.components.UIGraphicsComponent;
import mchorse.mappet.api.ui.components.UIIconButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UILayoutComponent;
import mchorse.mappet.api.ui.components.UIMorphComponent;
import mchorse.mappet.api.ui.components.UIStackComponent;
import mchorse.mappet.api.ui.components.UIStringListComponent;
import mchorse.mappet.api.ui.components.UITextComponent;
import mchorse.mappet.api.ui.components.UITextareaComponent;
import mchorse.mappet.api.ui.components.UITextboxComponent;
import mchorse.mappet.api.ui.components.UIToggleComponent;
import mchorse.mappet.api.ui.components.UITrackpadComponent;
import mchorse.metamorph.api.morphs.AbstractMorph;

import java.util.ArrayList;
import java.util.List;

/**
 * This is user interface builder interface. You can create GUIs with this thing.
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI();
 *
 *        // Build a UI using ui...
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
public interface IMappetUIBuilder
{
    /**
     * Get current UI component on to which it adds children components.
     *
     * <p>It's useful only after using {@link IMappetUIBuilder#layout()},
     * {@link IMappetUIBuilder#column(int)}, {@link IMappetUIBuilder#row(int)}, and
     * {@link IMappetUIBuilder#grid(int)} to being able to position layout element.</p>
     */
    public UIComponent getCurrent();

    /**
     * Enable default background (subtle gradient of two half transparent dark colors).
     */
    public IMappetUIBuilder background();

    /**
     * Disable an ability for players to manually close opened screens built with an API
     * by pressing escape.
     *
     * <p><b>BEWARE</b>: players will get stuck if you won't provide a way to close your
     * custom UI manually. ProTip: use {@link IScriptPlayer#closeUI()} to close player's
     * screen.</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").notClosable().background();
     *        var close = ui.icon("close").id("exit");
     *
     *        ui.text("[oTo close this screen, gently click on the button in the top right corner...").rxy(0.5, 0.5).w(200).anchor(0.5);
     *
     *        close.rx(1, -25).y(5).wh(20, 20);
     *        c.getSubject().openUI(ui);
     *    }
     *
     *    function handler(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *
     *        if (uiContext.getLast() === "exit")
     *        {
     *            c.getSubject().closeUI();
     *        }
     *    }
     * }</pre>
     */
    @SuppressWarnings("unused")
    public default IMappetUIBuilder notClosable()
    {
        return this.closable(false);
    }

    /**
     * Toggle closability of this UI screen.
     *
     * @param closable Whether this UI screen can be closed or not (by default it is closable).
     */
    public IMappetUIBuilder closable(boolean closable);

    /**
     * Toggle pausing of this UI screen.
     *
     * @param paused Whether this UI screen is paused when opened.
     */
    public IMappetUIBuilder paused(boolean paused);

    /**
     * Create and insert a UI component based on its ID into UI being built by this builder.
     *
     * <p>This method is future proof for in case other modders will be adding their own
     * components, and the only way to create 3rd party UI components is using this method by
     * providing the ID of 3rd party UI component.</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI().background();
     *        var label = ui.create("label").label("Welcome, [l" + c.getSubject().getName() + "[r!");
     *
     *        label.rxy(0.5, 0.5).wh(100, 20).anchor(0.5);
     *        label.color(0x00ee22).background(0x88000000).labelAnchor(0.5);
     *
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     *
     * @param id ID of desired component to be created.
     */
    public UIComponent create(String id);

    /**
     * Create and insert a graphics UI component into UI being built by this builder.
     *
     * <p>Check {@link UIGraphicsComponent} for description and examples.</p>
     */
    public UIGraphicsComponent graphics();

    /**
     * Create and insert a button UI component into UI being built by this builder.
     *
     * <p>Check {@link UIButtonComponent} for description and examples.</p>
     */
    public UIButtonComponent button(String label);

    /**
     * Create and insert an icon button UI component into UI being built by this builder.
     *
     * <p>Check {@link UIIconButtonComponent} for description and examples.</p>
     */
    public UIIconButtonComponent icon(String icon);

    /**
     * Create and insert a label UI component into UI being built by this builder.
     *
     * <p>Check {@link UILabelComponent} for description and examples.</p>
     */
    public UILabelComponent label(String label);

    /**
     * Create and insert a text UI component into UI being built by this builder.
     *
     * <p>Check {@link UILabelComponent} for description and examples.</p>
     */
    public UITextComponent text(String text);

    /**
     * Create and insert a textbox UI component into UI being built by this builder.
     *
     * <p>Check {@link UITextboxComponent} for description and examples.</p>
     */
    public default UITextboxComponent textbox()
    {
        return this.textbox("");
    }

    /**
     * Create and insert a textbox UI component into UI, with default value filled,
     * being built by this builder.
     *
     * <p>Check {@link UITextboxComponent} for description and examples.</p>
     */
    public default UITextboxComponent textbox(String text)
    {
        return this.textbox(text, 32);
    }

    /**
     * Create and insert a textbox UI component into UI, with default value filled
     * and maximum length, being built by this builder.
     *
     * <p>Check {@link UITextboxComponent} for description and examples.</p>
     */
    public UITextboxComponent textbox(String text, int maxLength);

    /**
     * Create and insert a textarea UI component into UI being built by this builder.
     *
     * <p>Check {@link UITextareaComponent} for description and examples.</p>
     */
    public default UITextareaComponent textarea()
    {
        return this.textarea("");
    }

    /**
     * Create and insert a textarea UI component into UI, with default value filled,
     * being built by this builder.
     *
     * <p>Check {@link UITextareaComponent} for description and examples.</p>
     */
    public UITextareaComponent textarea(String text);

    /**
     * Create and insert a toggle UI component into UI being built by this builder.
     *
     * <p>Check {@link UIToggleComponent} for description and examples.</p>
     */
    public default UIToggleComponent toggle(String label)
    {
        return this.toggle(label, false);
    }

    /**
     * Create and insert a toggle UI component into UI, with default toggled state,
     * being built by this builder.
     *
     * <p>Check {@link UIToggleComponent} for description and examples.</p>
     */
    public UIToggleComponent toggle(String label, boolean state);

    /**
     * Create and insert a trackpad UI component into UI being built by this builder.
     *
     * <p>Check {@link UITrackpadComponent} for description and examples.</p>
     */
    public default UITrackpadComponent trackpad()
    {
        return this.trackpad(0);
    }

    /**
     * Create and insert a trackpad UI component into UI, with default filled value,
     * being built by this builder.
     *
     * <p>Check {@link UITrackpadComponent} for description and examples.</p>
     */
    public UITrackpadComponent trackpad(double value);

    /**
     * Create and insert a string list UI component into UI, with list of possible
     * values in the list, being built by this builder.
     *
     * <p>Check {@link UIStringListComponent} for description and examples.</p>
     */
    public default UIStringListComponent stringList(ArrayList<String> values)
    {
        return this.stringList(values, -1);
    }

    /**
     * Create and insert a string list UI component into UI, with list of possible
     * values in the list and selected index by default, being built by this builder.
     *
     * <p>Check {@link UIStringListComponent} for description and examples.</p>
     */
    public UIStringListComponent stringList(ArrayList<String> values, int selected);

    /**
     * Create and insert an item stack UI component into UI being built by this builder.
     *
     * <p>Check {@link UIStackComponent} for description and examples.</p>
     */
    public default UIStackComponent item()
    {
        return this.item(null);
    }

    /**
     * Create and insert an item stack UI component into UI, with default item stack
     * picked, being built by this builder.
     *
     * <p>Check {@link UIStackComponent} for description and examples.</p>
     */
    public UIStackComponent item(IScriptItemStack stack);

    /**
     * Create and insert a morph UI component into UI being built by this builder.
     *
     * <p>Check {@link UIMorphComponent} for description and examples.</p>
     */
    public default UIMorphComponent morph(AbstractMorph morph)
    {
        return this.morph(morph, false);
    }

    /**
     * Create and insert a morph UI component into UI, with a flag whether the player
     * can pick or edit the morph, being built by this builder.
     *
     * <p>Check {@link UIMorphComponent} for description and examples.</p>
     */
    public UIMorphComponent morph(AbstractMorph morph, boolean editing);

    /**
     * Create and insert a click area UI component into UI being built by this builder.
     *
     * <p>Check {@link UIClickComponent} for description and examples.</p>
     */
    public UIClickComponent click();

    /**
     * Create and insert a layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public IMappetUIBuilder layout();

    /**
     * Create and insert a column layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public default IMappetUIBuilder column(int margin)
    {
        return this.column(margin, 0);
    }

    /**
     * Create and insert a column layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public IMappetUIBuilder column(int margin, int padding);

    /**
     * Create and insert a row layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public default IMappetUIBuilder row(int margin)
    {
        return this.row(margin, 0);
    }

    /**
     * Create and insert a row layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public IMappetUIBuilder row(int margin, int padding);

    /**
     * Create and insert a grid layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public default IMappetUIBuilder grid(int margin)
    {
        return this.grid(margin, 0);
    }

    /**
     * Create and insert a grid layout UI component into UI being built by this builder.
     *
     * <p>Check {@link UILayoutComponent} for description and examples.</p>
     */
    public IMappetUIBuilder grid(int margin, int padding);
}