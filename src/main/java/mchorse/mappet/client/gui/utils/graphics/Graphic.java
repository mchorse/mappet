package mchorse.mappet.client.gui.utils.graphics;

import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.ui.components.UIGraphicsComponent;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Graphic element.
 *
 * <p>This is a base interface for all graphic elements that are being constructed
 * by {@link UIGraphicsComponent}. Besides storing extra information per a type of
 * graphic, every graphic has position and relative measurements (just like
 * {@link UIComponent} has).</p>
 */
public abstract class Graphic implements INBTSerializable<NBTTagCompound>
{
    private static Area computed = new Area();

    public Area pixels = new Area();
    public float relativeX;
    public float relativeY;
    public float relativeW;
    public float relativeH;
    public int primary;

    @DiscardMethod
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
        else if (type.equals("text"))
        {
            graphic = new TextGraphic();
        }
        else if (type.equals("icon"))
        {
            graphic = new IconGraphic();
        }
        else if (type.equals("shadow"))
        {
            graphic = new ShadowGraphic();
        }

        if (graphic != null)
        {
            graphic.deserializeNBT(tag);
        }

        return graphic;
    }

    @DiscardMethod
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
        else if (graphic instanceof TextGraphic)
        {
            type = "text";
        }
        else if (graphic instanceof IconGraphic)
        {
            type = "icon";
        }
        else if (graphic instanceof ShadowGraphic)
        {
            type = "shadow";
        }

        tag.setString("Type", type);

        return tag;
    }

    /**
     * Set X in pixels relative to parent component.
     */
    public Graphic x(int value)
    {
        this.relativeX = 0F;
        this.pixels.x = value;

        return this;
    }

    /**
     * Set X relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully left, and <code>1</code> is fully right.
     */
    public Graphic rx(float value)
    {
        return this.rx(value, 0);
    }

    /**
     * Set X relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully left, and <code>1</code> is fully right.
     *
     * @param value Percentage how far into X.
     * @param offset Offset in pixels (can be negative).
     */
    public Graphic rx(float value, int offset)
    {
        this.relativeX = value;
        this.pixels.x = offset;

        return this;
    }

    /**
     * Set Y in pixels relative to parent component.
     */
    public Graphic y(int value)
    {
        this.relativeY = 0F;
        this.pixels.y = value;

        return this;
    }

    /**
     * Set Y relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully top, and <code>1</code> is fully bottom.
     */
    public Graphic ry(float value)
    {
        return this.ry(value, 0);
    }

    /**
     * Set Y relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is fully top, and <code>1</code> is fully bottom.
     *
     * @param value Percentage how far into Y.
     * @param offset Offset in pixels (can be negative).
     */
    public Graphic ry(float value, int offset)
    {
        this.relativeY = value;
        this.pixels.y = offset;

        return this;
    }

    /**
     * Set width in pixels.
     */
    public Graphic w(int value)
    {
        this.relativeW = 0F;
        this.pixels.w = value;

        return this;
    }

    /**
     * Set width relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's width, and <code>1</code> is <code>100%</code> of parent's
     * component width.
     */
    public Graphic rw(float value)
    {
        return this.rw(value, 0);
    }

    /**
     * Set width relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's width, and <code>1</code> is <code>100%</code> of parent's
     * component width.
     *
     * @param value Percentage of how wide relative to parent component.
     * @param offset Offset in pixels (can be negative).
     */
    public Graphic rw(float value, int offset)
    {
        this.relativeW = value;
        this.pixels.w = offset;

        return this;
    }

    /**
     * Set height in pixels.
     */
    public Graphic h(int value)
    {
        this.relativeH = 0F;
        this.pixels.h = value;

        return this;
    }

    /**
     * Set height relative in percents to parent component. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's height, and <code>1</code> is <code>100%</code> of parent's
     * component height.
     */
    public Graphic rh(float value)
    {
        return this.rh(value, 0);
    }

    /**
     * Set height relative in percents to parent component with offset. Passed value should be
     * <code>0..1</code>, where <code>0</code> is element will be <code>0%</code> of
     * parent component's height, and <code>1</code> is <code>100%</code> of parent's
     * component height.
     *
     * @param value Percentage of how tall relative to parent component.
     * @param offset Offset in pixels (can be negative).
     */
    public Graphic rh(float value, int offset)
    {
        this.relativeH = value;
        this.pixels.h = offset;

        return this;
    }

    /**
     * Set X and Y in pixels relative to parent component.
     */
    public Graphic xy(int x, int y)
    {
        return this.x(x).y(y);
    }

    /**
     * Set X and Y in pixels in percentage relative to parent component.
     */
    public Graphic rxy(float x, float y)
    {
        return this.rx(x).ry(y);
    }

    /**
     * Set width and height in pixels.
     */
    public Graphic wh(int w, int h)
    {
        return this.w(w).h(h);
    }

    /**
     * Set relative width and height in percentage relative to parent component.
     */
    public Graphic rwh(float w, float h)
    {
        return this.rw(w).rh(h);
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public final void draw(Area elementArea)
    {
        computed.x = elementArea.x + (int) (elementArea.w * this.relativeX) + this.pixels.x;
        computed.y = elementArea.y + (int) (elementArea.h * this.relativeY) + this.pixels.y;
        computed.w = (int) (elementArea.w * this.relativeW) + this.pixels.w;
        computed.h = (int) (elementArea.h * this.relativeH) + this.pixels.h;

        this.drawGraphic(computed);
    }

    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected abstract void drawGraphic(Area area);

    @Override
    @DiscardMethod
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setInteger("X", this.pixels.x);
        tag.setInteger("Y", this.pixels.y);
        tag.setInteger("W", this.pixels.w);
        tag.setInteger("H", this.pixels.h);
        tag.setFloat("RX", this.relativeX);
        tag.setFloat("RY", this.relativeY);
        tag.setFloat("RW", this.relativeW);
        tag.setFloat("RH", this.relativeH);
        tag.setInteger("Primary", this.primary);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.pixels.x = tag.getInteger("X");
        this.pixels.y = tag.getInteger("Y");
        this.pixels.w = tag.getInteger("W");
        this.pixels.h = tag.getInteger("H");
        this.relativeX = tag.getFloat("RX");
        this.relativeY = tag.getFloat("RY");
        this.relativeW = tag.getFloat("RW");
        this.relativeH = tag.getFloat("RH");
        this.primary = tag.getInteger("Primary");
    }
}