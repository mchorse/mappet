package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GradientGraphic extends Graphic
{
    public int secondary;
    public boolean horizontal;

    public GradientGraphic()
    {}

    public GradientGraphic(int x, int y, int w, int h, int primary, int secondary, boolean horizontal)
    {
        this.rect.set(x, y, w, h);
        this.primary = primary;
        this.secondary = secondary;
        this.horizontal = horizontal;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Area area)
    {
        int left = area.x + this.rect.x;
        int top = area.y + this.rect.y;
        int right = left + this.rect.w;
        int bottom = top + this.rect.h;

        if (this.horizontal)
        {
            GuiDraw.drawHorizontalGradientRect(left, top, right, bottom, this.primary, this.secondary);
        }
        else
        {
            GuiDraw.drawVerticalGradientRect(left, top, right, bottom, this.primary, this.secondary);
        }
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Secondary", this.secondary);
        tag.setBoolean("Horizontal", this.horizontal);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.secondary = tag.getInteger("Secondary");
        this.horizontal = tag.getBoolean("Horizontal");
    }
}