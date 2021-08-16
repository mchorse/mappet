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
        this.pixels.set(x, y, w, h);
        this.primary = primary;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawGraphic(Area area)
    {
        Gui.drawRect(area.x, area.y, area.ex(), area.ey(), this.primary);
    }
}