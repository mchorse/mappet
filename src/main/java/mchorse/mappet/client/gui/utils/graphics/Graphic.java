package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Graphic implements INBTSerializable<NBTTagCompound>
{
    public Area rect = new Area();
    public int primary;

    public static Graphic fromNBT(NBTTagCompound tag)
    {
        String type = tag.getString("Type");
        Graphic graphic = null;

        if (type.equals("rect"))
        {
            graphic = new RectGraphic();
        }
        else if (type.equals("gradient"))
        {
            graphic = new GradientGraphic();
        }
        else if (type.equals("image"))
        {
            graphic = new ImageGraphic();
        }

        if (graphic != null)
        {
            graphic.deserializeNBT(tag);
        }

        return graphic;
    }

    public static NBTTagCompound toNBT(Graphic graphic)
    {
        NBTTagCompound tag = graphic.serializeNBT();
        String type = "rect";

        if (graphic instanceof GradientGraphic)
        {
            type = "gradient";
        }
        else if (graphic instanceof ImageGraphic)
        {
            type = "image";
        }

        tag.setString("Type", type);

        return tag;
    }

    @SideOnly(Side.CLIENT)
    public abstract void draw(Area area);

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setInteger("X", this.rect.x);
        tag.setInteger("Y", this.rect.y);
        tag.setInteger("W", this.rect.w);
        tag.setInteger("H", this.rect.h);
        tag.setInteger("Primary", this.primary);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.rect.x = tag.getInteger("X");
        this.rect.y = tag.getInteger("Y");
        this.rect.w = tag.getInteger("W");
        this.rect.h = tag.getInteger("H");
        this.primary = tag.getInteger("Primary");
    }
}