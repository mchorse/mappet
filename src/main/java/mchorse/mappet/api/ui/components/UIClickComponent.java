package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.GuiClick;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIClickComponent extends UIComponent
{
    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        return this.apply(new GuiClick(mc, this, context), context);
    }

    @Override
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));
            list.appendTag(new NBTTagFloat(0));

            tag.setTag(this.id, list);
        }
    }
}