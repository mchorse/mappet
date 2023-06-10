package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
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

/**
 * Layout UI component.
 *
 * <p>This UI component does nothing, beside managing placement of other
 * components. Layout UI component have three different modes upon which
 * child components can be placed: column, row, and grid.</p>
 *
 * <p>Additionally, column mode supports scrolling when there are too many
 * components within its frame.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#layout()},
 * {@link IMappetUIBuilder#column(int)}, {@link IMappetUIBuilder#row(int)}, and
 * {@link IMappetUIBuilder#grid(int)} methods.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI().background();
 *        var column = ui.column(4, 10);
 *
 *        column.getCurrent().scroll().rxy(0.5, 0.5).w(240).rh(0.8).anchor(0.5);
 *
 *        var row = column.row(5);
 *        var name = row.column(4);
 *
 *        name.label("Name").h(8);
 *        name.textbox().id("name").h(20);
 *
 *        var lastname = row.column(4);
 *
 *        lastname.label("Last name").h(8);
 *        lastname.textbox().id("lastname").h(20);
 *
 *        column.toggle("I agree to ToS").id("toggle").h(14);
 *        column.text("The terms of service are following: you agree that your data will be used by an AI to generate funny cat and dog videos based entirely on your name and lastname.\n\nYou also agree to give us your time to view those videos, because we said so.").color(0xaaaaaa, false).marginTop(8);
 *        column.button("Oh... a button?").h(20).marginTop(12);
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
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

    /**
     * Enables scrolling. This option works only with {@link IMappetUIBuilder#column(int)}
     * layout component.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI().background();
     *        var column = ui.column(4, 10);
     *
     *        column.getCurrent().scroll().rxy(0.5, 0.5).wh(200, 200).anchor(0.5);
     *
     *        column.label("Name").h(8);
     *        column.textbox().id("name").h(20);
     *        column.label("Last name").h(8);
     *        column.textbox().id("lastname").h(20);
     *
     *        column.toggle("I agree to ToS").id("toggle").h(14);
     *        column.text("The terms of service are following: you agree that your data will be used by an AI to generate funny cat and dog videos based entirely on your name and lastname.\n\nYou also agree to give us your time to view those videos, because we said so.").color(0xaaaaaa, false).marginTop(8);
     *
     *        for (var i = 0; i < 10; i++)
     *        {
     *            column.button("Button " + (i + 1)).h(20);
     *        }
     *
     *        column.text("These 10 buttons above demonstrate the ability of this layout element to scroll down.").marginTop(12);
     *
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     */
    public UILayoutComponent scroll()
    {
        this.scroll = true;

        return this;
    }

    /**
     * Set manually scroll size of the layout element. This works only with
     * basic {@link IMappetUIBuilder#layout()} component.
     *
     * <p>If {@link UILayoutComponent#horizontal()} was enabled earlier,
     * then this value will change the max scrollable to width, rather than
     * height.</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var size = 400;
     *
     *        var ui = mappet.createUI().background();
     *
     *        // Demonstration of manual vertical scroll area
     *        var vertical = ui.layout();
     *
     *        vertical.getCurrent().scroll().scrollSize(size).rxy(0.25, 0.5).wh(150, 200).anchor(0.5);
     *        vertical.button("Top left").xy(10, 10).wh(100, 20);
     *        vertical.button("Middle").rx(0.5, -50).y(size / 2 - 10).wh(100, 20);
     *        vertical.button("Bottom right").rx(1, -110).y(size - 30).wh(100, 20);
     *
     *        ui.label("Vertical scroll").background(0x88000000).rx(0.25).ry(0.5, -120).wh(100, 20).anchorX(0.5).labelAnchor(0.5, 0);
     *
     *        // Demonstration of manual horizontal scroll area
     *        var horizontal = ui.layout();
     *
     *        horizontal.getCurrent().scroll().horizontal().scrollSize(size).rxy(0.75, 0.5).wh(150, 200).anchor(0.5);
     *        horizontal.button("Top left").xy(10, 10).wh(100, 20);
     *        horizontal.button("Middle").x(size / 2 - 50).ry(0.5, -10).wh(100, 20);
     *        horizontal.button("Bottom right").x(size - 110).ry(1, -30).wh(100, 20);
     *
     *        ui.label("Horizontal scroll").background(0x88000000).rx(0.75).ry(0.5, -120).wh(100, 20).anchorX(0.5).labelAnchor(0.5, 0);
     *
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     */
    public UILayoutComponent scrollSize(int scrollSize)
    {
        this.change("ScrollSize");

        this.scrollSize = scrollSize;

        return this;
    }

    /**
     * Enables horizontal mode. This usable when {@link UILayoutComponent#scroll()}
     * is enabled.
     */
    public UILayoutComponent horizontal()
    {
        this.horizontal = true;

        return this;
    }

    /**
     * Per component width (in pixels) that should be sustained within
     * {@link IMappetUIBuilder#grid(int)} layout type. This doesn't work with any other
     * component than grid.
     */
    public UILayoutComponent width(int width)
    {
        this.width = width;

        return this;
    }

    /**
     * How many components per row that should be placed within
     * {@link IMappetUIBuilder#grid(int)} layout type. This option doesn't work with
     * any other component than grid.
     */
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
            ColumnResizer column = element.flex().column(this.margin);

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
            else
            {
                column.stretch();
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

        if (tag.hasKey("Scroll"))
        {
            this.scroll = tag.getBoolean("Scroll");
        }
        if (tag.hasKey("ScrollSize"))
        {
            this.scrollSize = tag.getInteger("ScrollSize");
        }
        if (tag.hasKey("Horizontal"))
        {
            this.horizontal = tag.getBoolean("Horizontal");
        }

        if (tag.hasKey("LayoutType"))
        {
            int layoutType = tag.getInteger("LayoutType");

            if (layoutType >= 0 && layoutType < LayoutType.values().length)
            {
                this.layoutType = LayoutType.values()[layoutType];
            }
        }

        if (tag.hasKey("Margin"))
        {
            this.margin = tag.getInteger("Margin");
        }
        if (tag.hasKey("Padding"))
        {
            this.padding = tag.getInteger("Padding");
        }
        if (tag.hasKey("Width"))
        {
            this.width = tag.getInteger("Width");
        }
        if (tag.hasKey("Items"))
        {
            this.items = tag.getInteger("Items");
        }
    }
}