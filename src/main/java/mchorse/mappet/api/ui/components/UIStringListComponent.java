package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
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

public class UIStringListComponent extends UIComponent
{
    public List<String> values = new ArrayList<String>();
    public Integer selected;

    public UIStringListComponent values(String... values)
    {
        this.change("Values");

        this.values.clear();
        this.values.addAll(Arrays.asList(values));

        return this;
    }

    public UIStringListComponent values(List<String> values)
    {
        this.change("Values");

        this.values.clear();
        this.values.addAll(values);

        return this;
    }

    public UIStringListComponent selected(int selected)
    {
        this.change("Selected");

        this.selected = selected;

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
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiStringListElement element = new GuiStringListElement(mc, (v) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setString(this.id, v.get(0));
                context.dirty(this.id, this.updateDelay);
            }
        });

        element.add(this.values);

        if (this.selected != null)
        {
            element.setIndex(this.selected);
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

            if (this.selected != null && this.selected >= 0 && this.selected < this.values.size())
            {
                value = this.values.get(this.selected);
            }

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
    }
}