package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TextGraphic extends Graphic
{
    public String text;
    public float anchorX;
    public float anchorY;

    public TextGraphic()
    {}

    public TextGraphic(String text, int x, int y, int primary, float anchorX, float anchorY)
    {
        this.rect.set(x, y, 0, 0);
        this.primary = primary;
        this.text = text;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Area area)
    {
        Minecraft mc = Minecraft.getMinecraft();

        int w = mc.fontRenderer.getStringWidth(this.text);
        int left = area.x + this.rect.x - (int) (w * this.anchorX);
        int top = area.y + this.rect.y - (int) (mc.fontRenderer.FONT_HEIGHT * this.anchorY);

        mc.fontRenderer.drawStringWithShadow(this.text, left, top, this.primary);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Text", this.text);
        tag.setFloat("AnchorX", this.anchorX);
        tag.setFloat("AnchorY", this.anchorY);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.text = tag.getString("Text");
        this.anchorX = tag.getFloat("AnchorX");
        this.anchorY = tag.getFloat("AnchorY");
    }
}