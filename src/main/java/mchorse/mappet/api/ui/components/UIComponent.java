package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.api.ui.utils.UIContextItem;
import mchorse.mappet.api.ui.utils.UIKeybind;
import mchorse.mappet.api.ui.utils.UIUnit;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.IconRegistry;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.Keybind;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Base UI component.
 *
 * <p>Every UI component in UI API is based off this base component, and therefore
 * they have all of the methods available for changing ID, margins, frame (x, y,
 * width, and height), tooltip, visibility, enabled, keybinds and update delay.</p>
 */
public abstract class UIComponent implements INBTSerializable<NBTTagCompound>
{
    public static final int DELAY = 200;

    public String id = "";
    public String tooltip = "";
    public boolean visible = true;
    public boolean enabled = true;

    public int tooltipDirection;
    public int marginTop;
    public int marginBottom;
    public int marginLeft;
    public int marginRight;

    public UIUnit x = new UIUnit();
    public UIUnit y = new UIUnit();
    public UIUnit w = new UIUnit();
    public UIUnit h = new UIUnit();

    public int updateDelay = this.getDefaultUpdateDelay();
    public List<UIKeybind> keybinds = new ArrayList<UIKeybind>();
    public List<UIContextItem> context = new ArrayList<UIContextItem>();

    protected Set<String> changedProperties = new HashSet<String>();

    /**
     * Set the ID of the component.
     *
     * <p>Without ID, the data that can be inputted by players won't be sent
     * into the script handler, so it is <b>required</b> to set component's
     * ID if you want to receive the data from the component.</p>
     *
     * <p><b>BEWARE</b>: multiple components must not share same ID, if they will
     * it will certainly cause bugs in the data that you'll be receiving from the
     * client and the way you retrieve components using {@link IMappetUIContext#get(String)}.</p>
     */
    public UIComponent id(String id)
    {
        this.id = id;

        return this;
    }

    /**
     * Set a tooltip that will be displayed at the bottom of component's frame.
     */
    public UIComponent tooltip(String tooltip)
    {
        return this.tooltip(tooltip, 0);
    }

    /**
     * Set a tooltip that will be displayed at specified side of component's frame.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("component").tooltip("Enter your full name", 1);
     * }</pre>
     *
     * @param direction <code>0</code> is bottom.
     * <code>1</code> is top.
     * <code>2</code> is right.
     * <code>3</code> is left.
     */
    public UIComponent tooltip(String tooltip, int direction)
    {
        this.change("Tooltip");

        this.tooltip = tooltip;
        this.tooltipDirection = direction;

        return this;
    }

    /**
     * Set component's visibility. Hiding components also disables any user input,
     * i.e. despite button being invisible, it can't be clicked.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("button").visible(false);
     * }</pre>
     */
    public UIComponent visible(boolean visible)
    {
        this.change("Visible");

        this.visible = visible;

        return this;
    }

    /**
     * Toggle component's user input. When the component is disabled, it can't
     * receive any user input: no inputting text into or focusing textbox and
     * textareas, no clicking on click area, icon button, or button, etc.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("button").enabled(false);
     * }</pre>
     */
    public UIComponent enabled(boolean enabled)
    {
        this.change("Enabled");

        this.enabled = enabled;

        return this;
    }

    /**
     * Set margin to all sides.
     *
     * <p><b>IMPORTANT</b>: margins affect positioning only within layout component.
     * They do absolutely nothing outside of column, row and grid layout components.</p>
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    uiContext.get("button").margin(10);
     * }</pre>
     */
    public UIComponent margin(int margin)
    {
        this.change("Margin");

        this.marginTop = margin;
        this.marginBottom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;

        return this;
    }

    /**
     * Set top margin. See {@link #margin(int)} method for more information about
     * restrictions.
     */
    public UIComponent marginTop(int margin)
    {
        this.change("Margin");

        this.marginTop = margin;

        return this;
    }

    /**
     * Set bottom margin. See {@link #margin(int)} method for more information
     * about restrictions.
     */
    public UIComponent marginBottom(int margin)
    {
        this.change("Margin");

        this.marginBottom = margin;

        return this;
    }

    /**
     * Set left margin. See {@link #margin(int)} method for more information about
     * restrictions.
     */
    public UIComponent marginLeft(int margin)
    {
        this.change("Margin");

        this.marginLeft = margin;

        return this;
    }

    /**
     * Set right margin. See {@link #margin(int)} method for more information about
     * restrictions.
     */
    public UIComponent marginRight(int margin)
    {
        this.change("Margin");

        this.marginRight = margin;

        return this;
    }

    /**
     * Add a keybind with no modifiers. See {@link #keybind(int, String, String, boolean, boolean)} for proper example.
     */
    public UIComponent keybind(int keyCode, String action, String label)
    {
        return this.keybind(keyCode, action, label, false, false, false);
    }

    /**
     * Add a keybind with optional Control modifier.
     * See {@link #keybind(int, String, String, boolean, boolean)} for proper example.
     */
    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl)
    {
        return this.keybind(keyCode, action, label, ctrl, false, false);
    }

    /**
     * Add a keybind with optional Control and/or Shift modifier(s).
     * See {@link #keybind(int, String, String, boolean, boolean)} for proper example.
     */
    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl, boolean shift)
    {
        return this.keybind(keyCode, action, label, ctrl, shift, false);
    }

    /**
     * Add a keybind optionally with Control, Shift, and Alt key modifiers (i.e. while holding).
     *
     * <pre>{@code
     *    // For more reference, check this page to find the list of all key codes:
     *    // https://minecraft.fandom.com/wiki/Key_codes/Keyboard1
     *    //
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").background();
     *        var button = ui.icon("upload").id("icon");
     *
     *        // 203 = Arrow left
     *        ui.getCurrent().keybind(203, "left", "Change icon to left");
     *        // 205 = Arrow right
     *        ui.getCurrent().keybind(205, "right", "Change icon to right");
     *        button.rxy(0.5, 0.5).wh(20, 20).anchor(0.5);
     *        c.getSubject().openUI(ui);
     *    }
     *
     *    function handler(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *        var key = uiContext.getHotkey();
     *
     *        if (key === "left")
     *        {
     *            uiContext.get("icon").icon("leftload");
     *        }
     *        else if (key === "right")
     *        {
     *            uiContext.get("icon").icon("rightload");
     *        }
     *    }
     * }</pre>
     */
    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl, boolean shift, boolean alt)
    {
        this.change("Keybinds");

        this.keybinds.add(new UIKeybind(keyCode, action, label, UIKeybind.createModifier(shift, ctrl, alt)));

        return this;
    }

    /**
     * Add a context menu item.
     *
     * @param icon Icon ID (see {@link UIIconButtonComponent}).
     * @param action Action ID that will be used for handling with {@link IMappetUIContext#getContext()}.
     * @param label Label that will be displayed in the context menu item.
     */
    public UIComponent context(String icon, String action, String label)
    {
        return this.context(icon, action, label, 0);
    }

    /**
     * Add a context menu item.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").background();
     *        var label = ui.label("Hello!").id("label").tooltip("Right click me...");
     *
     *        label.rxy(0.5, 0.5).wh(160, 20).anchor(0.5).labelAnchor(0.5);
     *        label.context("bubble", "a", "How are you?");
     *        label.context("remove", "b", "...", 0xff0033);
     *
     *        c.getSubject().openUI(ui);
     *    }
     *
     *    function handler(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *        var data = uiContext.getData();
     *
     *        if (uiContext.getLast() === "textbox")
     *        {
     *            c.getSubject().send("Your name is: " + data.getString("textbox"));
     *        }
     *
     *        var item = uiContext.getContext();
     *
     *        if (item === "a")
     *        {
     *            uiContext.get("label").label("I'm fine, and you?");
     *        }
     *        else if (item === "b")
     *        {
     *            uiContext.get("label").label("");
     *        }
     *    }
     * }</pre>
     *
     * @param icon Icon ID (see {@link UIIconButtonComponent}).
     * @param action Action ID that will be used for handling with {@link IMappetUIContext#getContext()}.
     * @param label Label that will be displayed in the context menu item.
     * @param color Background color highlight (in RGB hex format).
     */
    public UIComponent context(String icon, String action, String label, int color)
    {
        this.change("Context");

        this.context.add(new UIContextItem(icon, action, label, color));

        return this;
    }

    /* Position and size */

    /**
     * Set X in pixels relative to parent component.
     */
    public UIComponent x(int value)
    {
        this.change("X");

        this.x.value = 0F;
        this.x.offset = value;

        return this;
    }

    /**
     * Set X relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully left, and <code>1</code> is fully right.
     */
    public UIComponent rx(float value)
    {
        return this.rx(value, 0);
    }

    /**
     * Set X relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully left, and <code>1</code> is fully right.
     *
     * @param value Percentage how far into X.
     * @param offset Offset in pixels (can be negative).
     */
    public UIComponent rx(float value, int offset)
    {
        this.change("X");

        this.x.value = value;
        this.x.offset = offset;

        return this;
    }

    /**
     * Set Y in pixels relative to parent component.
     */
    public UIComponent y(int value)
    {
        this.change("Y");

        this.y.value = 0F;
        this.y.offset = value;

        return this;
    }

    /**
     * Set Y relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully top, and <code>1</code> is fully bottom.
     */
    public UIComponent ry(float value)
    {
        return this.ry(value, 0);
    }

    /**
     * Set Y relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully top, and <code>1</code> is fully bottom.
     *
     * @param value Percentage how far into Y.
     * @param offset Offset in pixels (can be negative).
     */
    public UIComponent ry(float value, int offset)
    {
        this.change("Y");

        this.y.value = value;
        this.y.offset = offset;

        return this;
    }

    /**
     * Set width in pixels.
     */
    public UIComponent w(int value)
    {
        this.change("W");

        this.w.value = 0F;
        this.w.offset = value;

        return this;
    }

    /**
     * Set width relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's width, and <code>1</code> is <code>100%</code> of parent's
     * component width.
     */
    public UIComponent rw(float value)
    {
        return this.rw(value, 0);
    }

    /**
     * Set width relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's width, and <code>1</code> is <code>100%</code> of parent's
     * component width.
     *
     * @param value Percentage of how wide relative to parent component.
     * @param offset Offset in pixels (can be negative).
     */
    public UIComponent rw(float value, int offset)
    {
        this.change("W");

        this.w.value = value;
        this.w.offset = offset;

        return this;
    }

    /**
     * Set height in pixels.
     */
    public UIComponent h(int value)
    {
        this.change("H");

        this.h.value = 0F;
        this.h.offset = value;

        return this;
    }

    /**
     * Set height relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's height, and <code>1</code> is <code>100%</code> of parent's
     * component height.
     */
    public UIComponent rh(float value)
    {
        return this.rh(value, 0);
    }

    /**
     * Set height relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's height, and <code>1</code> is <code>100%</code> of parent's
     * component height.
     *
     * @param value Percentage of how tall relative to parent component.
     * @param offset Offset in pixels (can be negative).
     */
    public UIComponent rh(float value, int offset)
    {
        this.change("H");

        this.h.value = value;
        this.h.offset = offset;

        return this;
    }

    /**
     * Set X and Y in pixels relative to parent component.
     */
    public UIComponent xy(int x, int y)
    {
        return this.x(x).y(y);
    }

    /**
     * Set X and Y in pixels in percentage relative to parent component.
     */
    public UIComponent rxy(float x, float y)
    {
        return this.rx(x).ry(y);
    }

    /**
     * Set width and height in pixels.
     */
    public UIComponent wh(int w, int h)
    {
        return this.w(w).h(h);
    }

    /**
     * Set relative width and height in percentage relative to parent component.
     */
    public UIComponent rwh(float w, float h)
    {
        return this.rw(w).rh(h);
    }

    /**
     * Set horizontal and vertical alignment anchor.
     *
     * @param anchor Horizontal and vertical anchor.
     */
    public UIComponent anchor(float anchor)
    {
        return this.anchor(anchor, anchor);
    }

    /**
     * Set horizontal and vertical alignment anchor.
     *
     * @param anchorX Horizontal anchor.
     * @param anchorY Vertical anchor.
     */
    public UIComponent anchor(float anchorX, float anchorY)
    {
        return this.anchorX(anchorX).anchorY(anchorY);
    }

    /**
     * Set horizontal alignment anchor.
     */
    public UIComponent anchorX(float anchor)
    {
        this.change("X");

        this.x.anchor = anchor;

        return this;
    }

    /**
     * Set vertical alignment anchor.
     */
    public UIComponent anchorY(float anchor)
    {
        this.change("Y");

        this.y.anchor = anchor;

        return this;
    }

    /**
     * Set update delay in milliseconds (<code>1000</code> = <code>1</code> second).
     *
     * <p>Update delay allows to limit how frequently data gets sent from the client
     * to the hanlder script.</p>
     *
     * <pre>{@code
     *    // Assuming that ui is a IMappetUIBuilder
     *
     *    // Change text box's update delay to 1 second meaning
     *    // that a second after user didn't type anything into
     *    // the text box it will send all the data to the handler script
     *    ui.textbox().id("name").updateDelay(1000);
     * }</pre>
     */
    public UIComponent updateDelay(int updateDelay)
    {
        this.change("UpdateDelay");

        this.updateDelay = updateDelay;

        return this;
    }

    @DiscardMethod
    protected int getDefaultUpdateDelay()
    {
        return 0;
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected GuiElement apply(GuiElement element, UIContext context)
    {
        if (!this.tooltip.isEmpty())
        {
            applyTooltip(element);
        }

        element.setVisible(this.visible);
        element.setEnabled(this.enabled);

        element.marginTop(this.marginTop);
        element.marginBottom(this.marginBottom);
        element.marginLeft(this.marginLeft);
        element.marginRight(this.marginRight);

        this.x.apply(element.flex().x, context);
        this.y.apply(element.flex().y, context);
        this.w.apply(element.flex().w, context);
        this.h.apply(element.flex().h, context);

        if (!this.id.isEmpty())
        {
            context.registerElement(this.id, element, this.isDataReserved());
        }

        this.applyKeybinds(element, context);
        this.applyContext(element, context);

        return element;
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected GuiElement applyKeybinds(GuiElement element, UIContext context)
    {
        element.keys().keybinds.clear();

        for (UIKeybind keybind : this.keybinds)
        {
            Keybind key = element.keys().register(IKey.str(keybind.label), keybind.keyCode, () ->
            {
                context.sendKey(keybind.action);
            });

            List<Integer> held = new ArrayList<Integer>();

            if (keybind.isCtrl())
            {
                held.add(Keyboard.KEY_LCONTROL);
            }
            if (keybind.isShift())
            {
                held.add(Keyboard.KEY_LSHIFT);
            }
            if (keybind.isAlt())
            {
                held.add(Keyboard.KEY_LMENU);
            }

            if (!held.isEmpty())
            {
                key.held(held.stream().mapToInt(i -> i).toArray());
            }
        }

        return element;
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected boolean isDataReserved()
    {
        return false;
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    private void applyTooltip(GuiElement element)
    {
        Direction direction = Direction.BOTTOM;

        if (this.tooltipDirection == 1)
        {
            direction = Direction.TOP;
        }
        else if (this.tooltipDirection == 2)
        {
            direction = Direction.RIGHT;
        }
        else if (this.tooltipDirection == 3)
        {
            direction = Direction.LEFT;
        }

        if (this.tooltip.trim().isEmpty())
        {
            element.tooltip = null;
        }
        else
        {
            element.tooltip(IKey.str(TextUtils.processColoredText(this.tooltip)), direction);
        }
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    private void applyContext(GuiElement element, UIContext context)
    {
        if (this.context.isEmpty())
        {
            this.resetContext(element, context);
        }
        else
        {
            element.context(() ->
            {
                GuiSimpleContextMenu menu = new GuiSimpleContextMenu(Minecraft.getMinecraft());

                this.createContext(menu, element, context);

                return menu;
            });
        }
    }

    @SideOnly(Side.CLIENT)
    protected void resetContext(GuiElement element, UIContext context)
    {
        element.context(null);
    }

    @SideOnly(Side.CLIENT)
    protected void createContext(GuiSimpleContextMenu menu, GuiElement element, UIContext context)
    {
        for (UIContextItem item : this.context)
        {
            Runnable runnable = () -> context.sendContext(item.action);
            Icon icon = IconRegistry.icons.get(item.icon);

            if (icon == null)
            {
                icon = Icons.NONE;
            }

            if (item.color > 0)
            {
                menu.action(icon, IKey.str(item.label), runnable, item.color);
            }
            else
            {
                menu.action(icon, IKey.str(item.label), runnable);
            }
        }
    }

    /* Changes API (to being able to update data from the server on the client) */

    @DiscardMethod
    protected void change(String... properties)
    {
        this.changedProperties.addAll(Arrays.asList(properties));
    }

    @DiscardMethod
    public void clearChanges()
    {
        this.changedProperties.clear();
    }

    @DiscardMethod
    public Set<String> getChanges()
    {
        return Collections.unmodifiableSet(this.changedProperties);
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public void handleChanges(UIContext context, NBTTagCompound changes, GuiElement element)
    {
        this.deserializeNBT(changes);

        for (String key : changes.getKeySet())
        {
            this.applyProperty(context, key, element);
        }
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        if (key.equals("Tooltip"))
        {
            this.applyTooltip(element);
        }
        else if (key.equals("Visible"))
        {
            element.setVisible(this.visible);
        }
        else if (key.equals("Enabled"))
        {
            element.setEnabled(this.enabled);
        }
        else if (key.equals("Margin"))
        {
            element.marginTop(this.marginTop);
            element.marginBottom(this.marginBottom);
            element.marginLeft(this.marginLeft);
            element.marginRight(this.marginRight);
        }
        else if (key.equals("X"))
        {
            this.x.apply(element.flex().x, context);
        }
        else if (key.equals("Y"))
        {
            this.y.apply(element.flex().y, context);
        }
        else if (key.equals("W"))
        {
            this.w.apply(element.flex().w, context);
        }
        else if (key.equals("H"))
        {
            this.h.apply(element.flex().h, context);
        }
        else if (key.equals("Keybinds"))
        {
            this.applyKeybinds(element, context);
        }
        else if (key.equals("Context"))
        {
            this.applyContext(element, context);
        }
    }

    /* Main implementation */

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public abstract GuiElement create(Minecraft mc, UIContext context);

    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {}

    @DiscardMethod
    public List<UIComponent> getChildComponents()
    {
        return Collections.emptyList();
    }

    @DiscardMethod
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setString("Id", this.id);

        NBTTagCompound tooltip = new NBTTagCompound();

        tooltip.setString("Label", this.tooltip);
        tooltip.setInteger("Direction", this.tooltipDirection);

        tag.setTag("Tooltip", tooltip);

        tag.setBoolean("Visible", this.visible);
        tag.setBoolean("Enabled", this.enabled);

        NBTTagList margins = new NBTTagList();

        margins.appendTag(new NBTTagInt(this.marginTop));
        margins.appendTag(new NBTTagInt(this.marginBottom));
        margins.appendTag(new NBTTagInt(this.marginLeft));
        margins.appendTag(new NBTTagInt(this.marginRight));

        tag.setTag("Margin", margins);
        tag.setTag("X", this.x.serializeNBT());
        tag.setTag("Y", this.y.serializeNBT());
        tag.setTag("W", this.w.serializeNBT());
        tag.setTag("H", this.h.serializeNBT());
        tag.setInteger("UpdateDelay", this.updateDelay);

        NBTTagList keybinds = new NBTTagList();

        for (UIKeybind keybind : this.keybinds)
        {
            keybinds.appendTag(keybind.serializeNBT());
        }

        tag.setTag("Keybinds", keybinds);

        NBTTagList context = new NBTTagList();

        for (UIContextItem contextItem : this.context)
        {
            context.appendTag(contextItem.serializeNBT());
        }

        tag.setTag("Context", context);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Id"))
        {
            this.id = tag.getString("Id");
        }

        if (tag.hasKey("Tooltip", Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound tooltip = tag.getCompoundTag("Tooltip");

            this.tooltip = tooltip.getString("Label");
            this.tooltipDirection = tooltip.getInteger("Direction");
        }

        if (tag.hasKey("Visible"))
        {
            this.visible = tag.getBoolean("Visible");
        }

        if (tag.hasKey("Enabled"))
        {
            this.enabled = tag.getBoolean("Enabled");
        }

        if (tag.hasKey("Margin"))
        {
            NBTTagList margins = tag.getTagList("Margin", Constants.NBT.TAG_INT);

            if (margins.tagCount() >= 4)
            {
                this.marginTop = margins.getIntAt(0);
                this.marginBottom = margins.getIntAt(1);
                this.marginLeft = margins.getIntAt(2);
                this.marginRight = margins.getIntAt(3);
            }
        }

        if (tag.hasKey("X"))
        {
            this.x.deserializeNBT(tag.getCompoundTag("X"));
        }
        if (tag.hasKey("Y"))
        {
            this.y.deserializeNBT(tag.getCompoundTag("Y"));
        }
        if (tag.hasKey("W"))
        {
            this.w.deserializeNBT(tag.getCompoundTag("W"));
        }
        if (tag.hasKey("H"))
        {
            this.h.deserializeNBT(tag.getCompoundTag("H"));
        }

        if (tag.hasKey("UpdateDelay"))
        {
            this.updateDelay = tag.getInteger("UpdateDelay");
        }

        if (tag.hasKey("Keybinds"))
        {
            this.keybinds.clear();

            NBTTagList keybinds = tag.getTagList("Keybinds", Constants.NBT.TAG_COMPOUND);

            for (int i = 0, c = keybinds.tagCount(); i < c; i++)
            {
                UIKeybind keybind = new UIKeybind();

                keybind.deserializeNBT(keybinds.getCompoundTagAt(i));
                this.keybinds.add(keybind);
            }
        }

        if (tag.hasKey("Context"))
        {
            this.context.clear();

            NBTTagList context = tag.getTagList("Context", Constants.NBT.TAG_COMPOUND);

            for (int i = 0, c = context.tagCount(); i < c; i++)
            {
                UIContextItem contextItem = new UIContextItem();

                contextItem.deserializeNBT(context.getCompoundTagAt(i));
                this.context.add(contextItem);
            }
        }
    }
}