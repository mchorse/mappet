package mchorse.mappet.api.ui.components;

import com.caoccao.javet.annotations.V8Property;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * String list UI component.
 *
 * <p>This component allows users to pick a string out of a list of strings
 * that you provided.</p>
 *
 * <p>The value that gets written to UI context's data (if ID is present) is
 * the selected string that picked in the list.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#stringList(ArrayList)} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        let s = c.getSubject();
 *        let ui = mappet.createUI(handler).background();
 *        let strings = ui.stringList(["Apple", "Orange", "Pineapple", "Avocado"]).id("strings").tooltip("Pick a fruit...");
 *        let label = ui.label("...").id("fruit").visible(false);
 *
 *        strings.background(0x88000000).rxy(0.5, 0.5).wh(100, 240).anchor(0.5);
 *        label.rx(0.5).ry(0.5, -160).anchor(0.5, 0.5);
 *        label.background(0x88000000).labelAnchor(0.5, 0.5);
 *        s.openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var uiContext = c.getSubject().getUIContext();
 *        var data = uiContext.getData();
 *
 *        if (uiContext.getLast() === "strings")
 *        {
 *            uiContext.get("fruit").label(data.getString("strings")).visible(true);
 *        }
 *    }
 * }</pre>
 */
public class UIStringListComponent extends UIComponent
{
    @V8Property(name = "_values")
    public List<String> values = new ArrayList<String>();
    @V8Property(name = "_selected")
    public Integer selected;
    @V8Property(name = "_background")
    public Integer background;

    /**
     * Replace values within this string list.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Replace values in strings
     *    uiContext.get("strings").values("Tomato", "Cucumber", "Pepper", "Cabbage");
     * }</pre>
     */
    public UIStringListComponent values(String... values)
    {
        this.change("Values");

        this.values.clear();
        this.values.addAll(Arrays.asList(values));

        return this;
    }

    /**
     * Replace values within this string list.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    var vegetables = ["Tomato", "Cucumber", "Pepper", "Cabbage"];
     *
     *    // Replace values in strings
     *    uiContext.get("strings").values(vegetables);
     * }</pre>
     */
    public UIStringListComponent values(ArrayList<String> values)
    {
        this.change("Values");

        this.values.clear();
        this.values.addAll(values);

        return this;
    }

    /**
     * Replace values within this string list.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *    var vegetables = ["Tomato", "Cucumber", "Pepper", "Cabbage"];
     *
     *    // Replace values in strings
     *    uiContext.get("strings").setValues(vegetables);
     * }</pre>
     */
    public UIStringListComponent setValues(ArrayList<String> values)
    {
        return this.values(values);
    }

    /**
     * Returns values of this string list.
     *
     * <pre>{@code
     *    var values = uiContext.get("strings").getValues();
     *    
     *    for (var i in values)
     *    {
     *        c.send(values[i]);
     *    }
     * }</pre>
     */
    public List<String> getValues()
    {
        return this.values;
    }

    /**
     * Set the currently selected element.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set first string in the list to be selected
     *    uiContext.get("strings").selected(0);
     * }</pre>
     */
    public UIStringListComponent selected(int selected)
    {
        this.change("Selected");

        this.selected = selected;

        return this;
    }

    /**
     * Set component's solid color background.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set half transparent black background
     *    uiContext.get("strings").background();
     * }</pre>
     */
    public UIStringListComponent background()
    {
        return this.background(ColorUtils.HALF_BLACK);
    }

    /**
     * Set component's solid color background.
     *
     * <pre>{@code
     *    // Assuming that uiContext is a IMappetUIContext
     *
     *    // Set half transparent toxic green background
     *    uiContext.get("strings").background(0x8800ff00);
     * }</pre>
     */
    public UIStringListComponent background(int background)
    {
        this.change("Background");

        this.background = background;

        return this;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiStringListElement list = (GuiStringListElement) element;

        if (key.equals("Values"))
        {
            list.clear();
            list.add(this.values);
        }
        else if (key.equals("Selected") && this.selected != null)
        {
            list.setIndex(this.selected);
        }
        else if (key.equals("Background") && this.background != null)
        {
            list.background(this.background);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiStringListElement element = new GuiStringListElement(mc, null);

        element.callback = (v) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setString(this.id, v.get(0));
                context.data.setInteger(this.id + ".index", element.getIndex());
                context.dirty(this.id, this.updateDelay);
            }
        };

        element.add(this.values);

        if (this.selected != null)
        {
            element.setIndex(this.selected);
        }

        if (this.background != null)
        {
            element.background(this.background);
        }

        return this.apply(element, context);
    }

    @Override
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            String value = "";
            int index = 0;

            if (this.selected != null && this.selected >= 0 && this.selected < this.values.size())
            {
                value = this.values.get(this.selected);
                index = this.selected;
            }

            tag.setInteger(this.id + ".index", index);
            tag.setString(this.id, value);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        NBTTagList list = new NBTTagList();

        for (String value : this.values)
        {
            list.appendTag(new NBTTagString(value));
        }

        if (list.tagCount() > 0)
        {
            tag.setTag("Values", list);
        }

        if (this.selected != null)
        {
            tag.setInteger("Selected", this.selected);
        }

        if (this.background != null)
        {
            tag.setInteger("Background", this.background);
        }
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Values"))
        {
            NBTTagList list = tag.getTagList("Values", Constants.NBT.TAG_STRING);

            this.values.clear();

            for (int i = 0, c = list.tagCount(); i < c; i++)
            {
                this.values.add(list.getStringTagAt(i));
            }
        }

        if (tag.hasKey("Selected"))
        {
            this.selected = tag.getInteger("Selected");
        }

        if (tag.hasKey("Background"))
        {
            this.background = tag.getInteger("Background");
        }
    }
}
