package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.utils.Area;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.IconRegistry;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IconGraphic extends Graphic
{
    public String id;
    public float anchorX;
    public float anchorY;

    @SideOnly(Side.CLIENT)
    private Icon icon;

    public IconGraphic()
    {}

    public IconGraphic(String id, int x, int y, int primary, float anchorX, float anchorY)
    {
        this.rect.set(x, y, 0, 0);
        this.primary = primary;
        this.id = id;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Area area)
    {
        ColorUtils.bindColor(this.primary);

        if (this.icon == null)
        {
            this.icon = IconRegistry.icons.get(this.id);
            this.icon = this.icon == null ? Icons.NONE : this.icon;
        }

        int left = area.x + this.rect.x;
        int top = area.y + this.rect.y;

        this.icon.render(left, top, this.anchorX, this.anchorY);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Icon", this.id);
        tag.setFloat("AnchorX", this.anchorX);
        tag.setFloat("AnchorY", this.anchorY);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.id = tag.getString("Icon");
        this.anchorX = tag.getFloat("AnchorX");
        this.anchorY = tag.getFloat("AnchorY");
    }
}