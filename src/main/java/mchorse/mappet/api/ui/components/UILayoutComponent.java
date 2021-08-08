package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.api.ui.utils.LayoutType;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.utils.ScrollDirection;
import mchorse.mclib.client.gui.utils.resizers.layout.ColumnResizer;
import mchorse.mclib.client.gui.utils.resizers.layout.GridResizer;
import mchorse.mclib.client.gui.utils.resizers.layout.RowResizer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UILayoutComponent extends UIParentComponent
{
    public boolean scroll;
    public Integer scrollSize;
    public boolean horizontal;

    public LayoutType layoutType;
    public int margin;
    public int padding;

    public Integer width;
    public Integer items;

    public UILayoutComponent scroll()
    {
        return this.scroll(true);
    }

    public UILayoutComponent scroll(boolean scroll)
    {
        this.scroll = scroll;

        return this;
    }

    public UILayoutComponent scrollSize(int scrollSize)
    {
        this.change("ScrollSize");

        this.scrollSize = scrollSize;

        return this;
    }

    public UILayoutComponent horizontal()
    {
        return this.horizontal(true);
    }

    public UILayoutComponent horizontal(boolean horizontal)
    {
        this.horizontal = horizontal;

        return this;
    }

    public UILayoutComponent width(int width)
    {
        this.width = width;

        return this;
    }

    public UILayoutComponent items(int items)
    {
        this.items = items;

        return this;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiElement element;

        if (this.scroll)
        {
            GuiScrollElement scroll = new GuiScrollElement(mc, this.horizontal ? ScrollDirection.HORIZONTAL : ScrollDirection.VERTICAL);

            if (this.scrollSize != null)
            {
                scroll.scroll.scrollSize = this.scrollSize;
            }

            element = scroll;
        }
        else
        {
            element = new GuiElement(mc);
        }

        for (UIComponent component : this.getChildComponents())
        {
            GuiElement created = component.create(mc, context);

            if (this.layoutType == null)
            {
                created.flex().relative(element);
            }

            element.add(created);
        }

        if (this.layoutType != null)
        {
            this.applyLayout(element, this.layoutType);
        }

        return this.apply(element, context);
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    private void applyLayout(GuiElement element, LayoutType type)
    {
        if (type == LayoutType.COLUMN)
        {
            ColumnResizer column = element.flex().column(this.margin).stretch();

            if (this.scroll)
            {
                column.scroll();
            }

            if (!this.horizontal)
            {
                column.vertical();
            }

            if (this.width != null)
            {
                column.width(this.width);
            }

            column.padding(this.padding);
        }
        else if (type == LayoutType.ROW)
        {
            RowResizer row = element.flex().row(this.margin);

            if (this.width != null)
            {
                row.width(this.width);
            }

            row.padding(this.padding);
        }
        else if (type == LayoutType.GRID)
        {
            GridResizer grid = element.flex().grid(this.margin);

            if (this.width != null)
            {
                grid.width(this.width);
            }

            if (this.items != null)
            {
                grid.items(this.items);
            }

            grid.padding(this.padding);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        if (key.equals("ScrollSize") && element instanceof GuiScrollElement)
        {
            ((GuiScrollElement) element).scroll.scrollSize = this.scrollSize;
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setBoolean("Scroll", this.scroll);

        if (this.scrollSize != null)
        {
            tag.setInteger("ScrollSize", this.scrollSize);
        }

        tag.setBoolean("Horizontal", this.horizontal);

        if (this.layoutType != null)
        {
            tag.setInteger("LayoutType", this.layoutType.ordinal());
        }

        tag.setInteger("Margin", this.margin);
        tag.setInteger("Padding", this.padding);

        if (this.width != null)
        {
            tag.setInteger("Width", this.width);
        }

        if (this.items != null)
        {
            tag.setInteger("Items", this.items);
        }
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Scroll")) this.scroll = tag.getBoolean("Scroll");
        if (tag.hasKey("ScrollSize")) this.scrollSize = tag.getInteger("ScrollSize");
        if (tag.hasKey("Horizontal")) this.horizontal = tag.getBoolean("Horizontal");

        if (tag.hasKey("LayoutType"))
        {
            int layoutType = tag.getInteger("LayoutType");

            if (layoutType >= 0 && layoutType < LayoutType.values().length)
            {
                this.layoutType = LayoutType.values()[layoutType];
            }
        }

        if (tag.hasKey("Margin")) this.margin = tag.getInteger("Margin");
        if (tag.hasKey("Padding")) this.padding = tag.getInteger("Padding");
        if (tag.hasKey("Width")) this.width = tag.getInteger("Width");
        if (tag.hasKey("Items")) this.items = tag.getInteger("Items");
    }
}