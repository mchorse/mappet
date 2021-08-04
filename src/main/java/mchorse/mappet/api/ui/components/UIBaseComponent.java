package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.UIUnit;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.List;

public abstract class UIBaseComponent implements IUIComponent
{
    public static final int DELAY = 200;

    public String id = "";
    public String tooltip = "";
    public int marginTop;
    public int marginBotom;
    public int marginLeft;
    public int marginRight;

    public UIUnit x = new UIUnit();
    public UIUnit y = new UIUnit();
    public UIUnit w = new UIUnit();
    public UIUnit h = new UIUnit();

    public int updateDelay = this.getDefaultUpdateDelay();

    public UIBaseComponent id(String id)
    {
        this.id = id;

        return this;
    }

    public UIBaseComponent tooltip(String tooltip)
    {
        this.tooltip = tooltip;

        return this;
    }

    public UIBaseComponent margin(int margin)
    {
        this.marginTop = margin;
        this.marginBotom = margin;
        this.marginLeft = margin;
        this.marginRight = margin;

        return this;
    }

    public UIBaseComponent marginTop(int margin)
    {
        this.marginTop = margin;

        return this;
    }

    public UIBaseComponent marginBotom(int margin)
    {
        this.marginBotom = margin;

        return this;
    }

    public UIBaseComponent marginLeft(int margin)
    {
        this.marginLeft = margin;

        return this;
    }

    public UIBaseComponent marginRight(int margin)
    {
        this.marginRight = margin;

        return this;
    }

    /* Position and size */

    public UIBaseComponent x(int value)
    {
        return this.x(value, 0);
    }

    public UIBaseComponent x(int value, int offset)
    {
        this.x.percentage = false;
        this.x.value = value;
        this.x.offset = offset;

        return this;
    }

    public UIBaseComponent rx(float value)
    {
        return this.rx(value, 0);
    }

    public UIBaseComponent rx(float value, int offset)
    {
        this.x.percentage = true;
        this.x.value = value;
        this.x.offset = offset;

        return this;
    }

    public UIBaseComponent y(int value)
    {
        return this.y(value, 0);
    }

    public UIBaseComponent y(int value, int offset)
    {
        this.y.percentage = false;
        this.y.value = value;
        this.y.offset = offset;

        return this;
    }

    public UIBaseComponent ry(float value)
    {
        return this.ry(value, 0);
    }

    public UIBaseComponent ry(float value, int offset)
    {
        this.y.percentage = true;
        this.y.value = value;
        this.y.offset = offset;

        return this;
    }

    public UIBaseComponent w(int value)
    {
        return this.w(value, 0);
    }

    public UIBaseComponent w(int value, int offset)
    {
        this.w.percentage = false;
        this.w.value = value;
        this.w.offset = offset;

        return this;
    }

    public UIBaseComponent rw(float value)
    {
        return this.rw(value, 0);
    }

    public UIBaseComponent rw(float value, int offset)
    {
        this.w.percentage = true;
        this.w.value = value;
        this.w.offset = offset;

        return this;
    }

    public UIBaseComponent h(int value)
    {
        return this.h(value, 0);
    }

    public UIBaseComponent h(int value, int offset)
    {
        this.h.percentage = false;
        this.h.value = value;
        this.h.offset = offset;

        return this;
    }

    public UIBaseComponent rh(float value)
    {
        return this.rh(value, 0);
    }

    public UIBaseComponent rh(float value, int offset)
    {
        this.h.percentage = true;
        this.h.value = value;
        this.h.offset = offset;

        return this;
    }

    public UIBaseComponent xy(int x, int y)
    {
        return this.x(x).y(y);
    }

    public UIBaseComponent rxy(float x, float y)
    {
        return this.rx(x).ry(y);
    }

    public UIBaseComponent wh(int w, int h)
    {
        return this.w(w).h(h);
    }

    public UIBaseComponent rwh(float w, float h)
    {
        return this.rw(w).rh(h);
    }

    public UIBaseComponent anchor(float anchor)
    {
        return this.anchorX(anchor).anchorY(anchor);
    }

    public UIBaseComponent anchorX(float anchor)
    {
        this.x.anchor = anchor;

        return this;
    }

    public UIBaseComponent anchorY(float anchor)
    {
        this.y.anchor = anchor;

        return this;
    }

    /* Subclass utilities */

    protected int getDefaultUpdateDelay()
    {
        return 0;
    }

    public UIBaseComponent updateDelay(int updateDelay)
    {
        this.updateDelay = updateDelay;

        return this;
    }

    protected GuiElement apply(GuiElement element, UIContext context)
    {
        if (!this.tooltip.isEmpty())
        {
            element.tooltip(IKey.str(this.tooltip));
        }

        element.marginTop(this.marginTop);
        element.marginBottom(this.marginBotom);
        element.marginLeft(this.marginLeft);
        element.marginRight(this.marginRight);

        this.x.apply(element.flex().x, context);
        this.y.apply(element.flex().y, context);
        this.w.apply(element.flex().w, context);
        this.h.apply(element.flex().h, context);

        return element;
    }

    /* IUIComponent implementation */

    @Override
    public List<IUIComponent> getChildComponents()
    {
        return Collections.emptyList();
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        NBTTagList margins = new NBTTagList();

        margins.appendTag(new NBTTagInt(this.marginTop));
        margins.appendTag(new NBTTagInt(this.marginBotom));
        margins.appendTag(new NBTTagInt(this.marginLeft));
        margins.appendTag(new NBTTagInt(this.marginRight));

        tag.setString("Id", this.id);
        tag.setString("Tooltip", this.tooltip);
        tag.setTag("Margin", margins);
        tag.setTag("X", this.x.serializeNBT());
        tag.setTag("Y", this.y.serializeNBT());
        tag.setTag("W", this.w.serializeNBT());
        tag.setTag("H", this.h.serializeNBT());
        tag.setInteger("UpdateDelay", this.updateDelay);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        NBTTagList margins = tag.getTagList("Margins", Constants.NBT.TAG_INT);

        if (margins.tagCount() >= 4)
        {
            this.marginTop = margins.getIntAt(0);
            this.marginBotom = margins.getIntAt(1);
            this.marginLeft = margins.getIntAt(2);
            this.marginRight = margins.getIntAt(3);
        }

        this.id = tag.getString("Id");
        this.tooltip = tag.getString("Tooltip");
        this.x.deserializeNBT(tag.getCompoundTag("X"));
        this.y.deserializeNBT(tag.getCompoundTag("Y"));
        this.w.deserializeNBT(tag.getCompoundTag("W"));
        this.h.deserializeNBT(tag.getCompoundTag("H"));

        if (tag.hasKey("UpdateDelay"))
        {
            this.updateDelay = tag.getInteger("UpdateDelay");
        }
    }
}