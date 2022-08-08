package mchorse.mappet.api.ui.utils;

import com.caoccao.javet.annotations.V8Property;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class UIContextItem implements INBTSerializable<NBTTagCompound>
{
    @V8Property(name = "_icon")
    public String icon = "";
    @V8Property(name = "_action")
    public String action = "";
    @V8Property(name = "_label")
    public String label = "";
    @V8Property(name = "_color")
    public int color;

    public UIContextItem()
    {}

    public UIContextItem(String icon, String action, String label, int color)
    {
        this.icon = icon;
        this.action = action;
        this.label = label;
        this.color = color;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Icon", this.icon);
        tag.setString("Action", this.action);
        tag.setString("Label", this.label);
        tag.setInteger("Color", this.color);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.icon = tag.getString("Icon");
        this.action = tag.getString("Action");
        this.label = tag.getString("Label");
        this.color = tag.getInteger("Color");
    }
}