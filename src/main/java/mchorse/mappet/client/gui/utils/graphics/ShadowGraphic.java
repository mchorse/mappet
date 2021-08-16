package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.nbt.NBTTagCompound;

public class ShadowGraphic extends Graphic
{
    public int secondary;
    public int offset;

    public ShadowGraphic()
    {}

    public ShadowGraphic(int x, int y, int w, int h, int primary, int secondary, int offset)
    {
        this.pixels.set(x, y, w, h);
        this.primary = primary;
        this.secondary = secondary;
        this.offset = offset;
    }

    @Override
    protected void drawGraphic(Area area)
    {
        GuiDraw.drawDropShadow(area.x, area.y, area.ex(), area.ey(), this.offset, this.primary, this.secondary);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Secondary", this.secondary);
        tag.setInteger("Offset", this.offset);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.secondary = tag.getInteger("Secondary");
        this.offset = tag.getInteger("Offset");
    }
}