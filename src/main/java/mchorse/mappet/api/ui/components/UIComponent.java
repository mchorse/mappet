package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.UIKeybind;
import mchorse.mappet.api.ui.utils.UIUnit;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
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

public abstract class UIComponent implements INBTSerializable<NBTTagCompound>
{
    public static final int DELAY = 200;

    public String id = "";
    public String tooltip = "";
    public boolean visible = true;
    public boolean enabled = true;

    public int tooltipDirection;
    public int marginTop;
    public int marginBotom;
    public int marginLeft;
    public int marginRight;

    public UIUnit x = new UIUnit();
    public UIUnit y = new UIUnit();
    public UIUnit w = new UIUnit();
    public UIUnit h = new UIUnit();

    public int updateDelay = this.getDefaultUpdateDelay();
    public List<UIKeybind> keybinds = new ArrayList<UIKeybind>();

    protected Set<String> changedProperties = new HashSet<String>();

    public UIComponent id(String id)
    {
        this.id = id;

        return this;
    }

    public UIComponent tooltip(String tooltip)
    {
        return this.tooltip(tooltip, 0);
    }

    public UIComponent tooltip(String tooltip, int direction)
    {
        this.change("Tooltip");

        this.tooltip = tooltip;
        this.tooltipDirection = direction;

        return this;
    }

    public UIComponent visible(boolean visible)
    {
        this.change("Visible");

        this.visible = visible;

        return this;
    }

    public UIComponent enabled(boolean enabled)
    {
        this.change("Enabled");

        this.enabled = enabled;

        return this;
    }

    public UIComponent margin(int margin)
    {
        this.change("Margins");

        this.marginTop = margin;
        this.marginBotom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;

        return this;
    }

    public UIComponent marginTop(int margin)
    {
        this.change("Margins");

        this.marginTop = margin;

        return this;
    }

    public UIComponent marginBotom(int margin)
    {
        this.change("Margins");

        this.marginBotom = margin;

        return this;
    }

    public UIComponent marginLeft(int margin)
    {
        this.change("Margins");

        this.marginLeft = margin;

        return this;
    }

    public UIComponent marginRight(int margin)
    {
        this.change("Margins");

        this.marginRight = margin;

        return this;
    }

    public UIComponent keybind(int keyCode, String action, String label)
    {
        return this.keybind(keyCode, action, label, false, false, false);
    }

    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl)
    {
        return this.keybind(keyCode, action, label, ctrl, false, false);
    }

    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl, boolean shift)
    {
        return this.keybind(keyCode, action, label, ctrl, shift, false);
    }

    public UIComponent keybind(int keyCode, String action, String label, boolean ctrl, boolean shift, boolean alt)
    {
        this.keybinds.add(new UIKeybind(keyCode, action, label, UIKeybind.createModifier(shift, ctrl, alt)));

        return this;
    }

    /* Position and size */

    public UIComponent x(int value)
    {
        return this.x(value, 0);
    }

    public UIComponent x(int value, int offset)
    {
        this.change("X");

        this.x.percentage = false;
        this.x.value = value;
        this.x.offset = offset;

        return this;
    }

    public UIComponent rx(float value)
    {
        return this.rx(value, 0);
    }

    public UIComponent rx(float value, int offset)
    {
        this.change("X");

        this.x.percentage = true;
        this.x.value = value;
        this.x.offset = offset;

        return this;
    }

    public UIComponent y(int value)
    {
        return this.y(value, 0);
    }

    public UIComponent y(int value, int offset)
    {
        this.change("Y");

        this.y.percentage = false;
        this.y.value = value;
        this.y.offset = offset;

        return this;
    }

    public UIComponent ry(float value)
    {
        return this.ry(value, 0);
    }

    public UIComponent ry(float value, int offset)
    {
        this.change("Y");

        this.y.percentage = true;
        this.y.value = value;
        this.y.offset = offset;

        return this;
    }

    public UIComponent w(int value)
    {
        return this.w(value, 0);
    }

    public UIComponent w(int value, int offset)
    {
        this.change("W");

        this.w.percentage = false;
        this.w.value = value;
        this.w.offset = offset;

        return this;
    }

    public UIComponent rw(float value)
    {
        return this.rw(value, 0);
    }

    public UIComponent rw(float value, int offset)
    {
        this.change("W");

        this.w.percentage = true;
        this.w.value = value;
        this.w.offset = offset;

        return this;
    }

    public UIComponent h(int value)
    {
        return this.h(value, 0);
    }

    public UIComponent h(int value, int offset)
    {
        this.change("H");

        this.h.percentage = false;
        this.h.value = value;
        this.h.offset = offset;

        return this;
    }

    public UIComponent rh(float value)
    {
        return this.rh(value, 0);
    }

    public UIComponent rh(float value, int offset)
    {
        this.change("H");

        this.h.percentage = true;
        this.h.value = value;
        this.h.offset = offset;

        return this;
    }

    public UIComponent xy(int x, int y)
    {
        return this.x(x).y(y);
    }

    public UIComponent rxy(float x, float y)
    {
        return this.rx(x).ry(y);
    }

    public UIComponent wh(int w, int h)
    {
        return this.w(w).h(h);
    }

    public UIComponent rwh(float w, float h)
    {
        return this.rw(w).rh(h);
    }

    public UIComponent anchor(float anchor)
    {
        return this.anchor(anchor, anchor);
    }

    public UIComponent anchor(float anchorX, float anchorY)
    {
        return this.anchorX(anchorX).anchorY(anchorY);
    }

    public UIComponent anchorX(float anchor)
    {
        this.change("X");

        this.x.anchor = anchor;

        return this;
    }

    public UIComponent anchorY(float anchor)
    {
        this.change("Y");

        this.y.anchor = anchor;

        return this;
    }

    /* Subclass utilities */

    protected int getDefaultUpdateDelay()
    {
        return 0;
    }

    public UIComponent updateDelay(int updateDelay)
    {
        this.change("UpdateDelay");

        this.updateDelay = updateDelay;

        return this;
    }

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
        element.marginBottom(this.marginBotom);
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

        for (UIKeybind keybind : this.keybinds)
        {
            Keybind key = element.keys().register(IKey.str(keybind.label), keybind.keyCode, () ->
            {
                context.sendKey(keybind.action);
            });

            List<Integer> held = new ArrayList<Integer>();

            if (keybind.isCtrl()) held.add(Keyboard.KEY_LCONTROL);
            if (keybind.isShift()) held.add(Keyboard.KEY_LSHIFT);
            if (keybind.isAlt()) held.add(Keyboard.KEY_LMENU);

            if (!held.isEmpty())
            {
                key.held(held.stream().mapToInt(i -> i).toArray());
            }
        }

        return element;
    }

    @SideOnly(Side.CLIENT)
    protected boolean isDataReserved()
    {
        return false;
    }

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

        element.tooltip(IKey.str(TextUtils.processColoredText(this.tooltip)), direction);
    }

    /* Changes API (to being able to update data from the server on the client) */

    protected void change(String... properties)
    {
        this.changedProperties.addAll(Arrays.asList(properties));
    }

    public void clearChanges()
    {
        this.changedProperties.clear();
    }

    public Set<String> getChanges()
    {
        return Collections.unmodifiableSet(this.changedProperties);
    }

    @SideOnly(Side.CLIENT)
    public void handleChanges(UIContext context, NBTTagCompound changes, GuiElement element)
    {
        this.deserializeNBT(changes);

        for (String key : changes.getKeySet())
        {
            this.applyProperty(context, key, element);
        }
    }

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
        else if (key.equals("Margins"))
        {
            element.marginTop(this.marginTop);
            element.marginBottom(this.marginBotom);
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
    }

    /* Main implementation */

    @SideOnly(Side.CLIENT)
    public abstract GuiElement create(Minecraft mc, UIContext context);

    public List<UIComponent> getChildComponents()
    {
        return Collections.emptyList();
    }

    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

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
        margins.appendTag(new NBTTagInt(this.marginBotom));
        margins.appendTag(new NBTTagInt(this.marginLeft));
        margins.appendTag(new NBTTagInt(this.marginRight));

        tag.setTag("Margin", margins);
        tag.setTag("X", this.x.serializeNBT());
        tag.setTag("Y", this.y.serializeNBT());
        tag.setTag("W", this.w.serializeNBT());
        tag.setTag("H", this.h.serializeNBT());
        tag.setInteger("UpdateDelay", this.updateDelay);

        if (!this.keybinds.isEmpty())
        {
            NBTTagList keybinds = new NBTTagList();

            for (UIKeybind keybind : this.keybinds)
            {
                keybinds.appendTag(keybind.serializeNBT());
            }

            tag.setTag("Keybinds", keybinds);
        }
    }

    @Override
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

        if (tag.hasKey("Margins"))
        {
            NBTTagList margins = tag.getTagList("Margins", Constants.NBT.TAG_INT);

            if (margins.tagCount() >= 4)
            {
                this.marginTop = margins.getIntAt(0);
                this.marginBotom = margins.getIntAt(1);
                this.marginLeft = margins.getIntAt(2);
                this.marginRight = margins.getIntAt(3);
            }
        }

        if (tag.hasKey("X")) this.x.deserializeNBT(tag.getCompoundTag("X"));
        if (tag.hasKey("Y")) this.y.deserializeNBT(tag.getCompoundTag("Y"));
        if (tag.hasKey("W")) this.w.deserializeNBT(tag.getCompoundTag("W"));
        if (tag.hasKey("H")) this.h.deserializeNBT(tag.getCompoundTag("H"));

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
    }
}