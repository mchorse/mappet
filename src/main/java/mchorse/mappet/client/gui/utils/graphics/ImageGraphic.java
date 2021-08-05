package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.resources.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ImageGraphic extends Graphic
{
    public ResourceLocation picture;
    public int width;
    public int height;

    public ImageGraphic()
    {}

    public ImageGraphic(ResourceLocation picture, int x, int y, int w, int h, int width, int height, int primary)
    {
        this.picture = picture;
        this.rect.set(x, y, w, h);
        this.primary = primary;
        this.width = width;
        this.height = height;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void draw(Area area)
    {
        if (this.picture != null)
        {
            ColorUtils.bindColor(this.primary);

            int left = area.x + this.rect.x;
            int top = area.y + this.rect.y;

            Minecraft.getMinecraft().renderEngine.bindTexture(this.picture);

            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();

            GuiDraw.drawBillboard(left, top, 0, 0, this.rect.w, this.rect.h, this.width, this.height);
        }
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.picture != null)
        {
            tag.setString("Image", this.picture.toString());
        }

        tag.setInteger("Width", this.width);
        tag.setInteger("Height", this.height);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.picture = RLUtils.create(tag.getString("Image"));
        this.width = tag.getInteger("Width");
        this.height = tag.getInteger("Height");
    }
}