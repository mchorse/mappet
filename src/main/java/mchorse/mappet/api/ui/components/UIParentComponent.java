package mchorse.mappet.api.ui.components;

import mchorse.mappet.CommonProxy;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public abstract class UIParentComponent extends UIComponent
{
    public List<UIComponent> children = new ArrayList<UIComponent>();

    @Override
    public List<UIComponent> getChildComponents()
    {
        return this.children;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        NBTTagList list = new NBTTagList();

        for (UIComponent component : this.children)
        {
            NBTTagCompound componentTag = component.serializeNBT();

            componentTag.setString("Type", CommonProxy.getUiComponents().getType(component));
            list.appendTag(componentTag);
        }

        tag.setTag("Components", list);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        NBTTagList list = tag.getTagList("Components", Constants.NBT.TAG_COMPOUND);

        for (int i = 0, c = list.tagCount(); i < c; i++)
        {
            NBTTagCompound componentTag = list.getCompoundTagAt(i);
            UIComponent component = CommonProxy.getUiComponents().create(componentTag.getString("Type"));

            if (component != null)
            {
                component.deserializeNBT(componentTag);
                this.children.add(component);
            }
        }
    }
}