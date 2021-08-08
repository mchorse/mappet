package mchorse.mappet.api.ui.utils;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiClick extends GuiElement
{
    public UIComponent component;
    public UIContext context;

    public GuiClick(Minecraft mc, UIComponent component, UIContext context)
    {
        super(mc);

        this.component = component;
        this.context = context;
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        if (this.area.isInside(context) && !this.component.id.isEmpty())
        {
            NBTTagList list = new NBTTagList();

            list.appendTag(new NBTTagFloat(context.mouseX - this.area.x));
            list.appendTag(new NBTTagFloat(context.mouseY - this.area.y));
            list.appendTag(new NBTTagFloat((context.mouseX - this.area.x) / (float) this.area.w));
            list.appendTag(new NBTTagFloat((context.mouseY - this.area.y) / (float) this.area.h));
            list.appendTag(new NBTTagFloat(context.mouseButton));

            this.context.data.setTag(this.component.id, list);
            this.context.dirty(this.component.id, this.component.updateDelay);

            return true;
        }

        return false;
    }
}