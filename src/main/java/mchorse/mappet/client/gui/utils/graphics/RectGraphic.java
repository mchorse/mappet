package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RectGraphic extends Graphic
{
    public RectGraphic()
    {}

    public RectGraphic(int x, int y, int w, int h, int primary)
    {
        this.rect.set(x, y, w, h);
        this.primary = primary;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Area area)
    {
        int left = area.x + this.rect.x;
        int top = area.y + this.rect.y;

        Gui.drawRect(left, top, left + this.rect.w, top + this.rect.h, this.primary);
    }
}