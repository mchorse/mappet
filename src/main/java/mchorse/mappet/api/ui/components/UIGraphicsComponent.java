package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.client.gui.utils.GuiGraphics;
import mchorse.mappet.client.gui.utils.graphics.GradientGraphic;
import mchorse.mappet.client.gui.utils.graphics.Graphic;
import mchorse.mappet.client.gui.utils.graphics.IconGraphic;
import mchorse.mappet.client.gui.utils.graphics.ImageGraphic;
import mchorse.mappet.client.gui.utils.graphics.RectGraphic;
import mchorse.mappet.client.gui.utils.graphics.ShadowGraphic;
import mchorse.mappet.client.gui.utils.graphics.TextGraphic;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.utils.resources.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Graphics UI component.
 *
 * <p>This component allows drawing solid colored rectangles, gradient rectangles,
 * images loaded through Minecraft's texture manager, text and McLib icons. Think
 * of it as a very primitive canvas implementation.</p>
 *
 * <p>This component can be created using {@link IMappetUIBuilder#graphics()} method.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *
 *        // Background rendering
 *        var back = ui.graphics().rx(0.5, -150).ry(1, -250).wh(300, 250);
 *        var icons = Java.type("mchorse.mclib.client.gui.utils.IconRegistry").icons.keySet();
 *
 *        back.shadow(80, 80, 300 - 160, 250 - 160, 0x88ff1493, 0x00ff1493, 80);
 *        back.shadow(80, 80, 300 - 160, 250 - 160, 0x880088ff, 0x000088ff, 40);
 *
 *        for each (var icon in icons)
 *        {
 *            var x = Math.random() * 280 + 10;
 *            var y = Math.random() * 230 + 10;
 *
 *            back.icon(icon, x - 1, y - 1, 0xff000000 + Math.random() * 0xffffff);
 *        }
 *
 *        // Draw my favorite "me"
 *        var m = mappet.createMorph("{Skin:\"blockbuster:textures/entity/mchorse/skin.png\",CustomPose:{Size:[0.6f,1.8f,0.6f],Poses:{right_arm:{P:[-6.0f,-2.0f,0.0f],R:[-90.0f,-31.0f,0.0f]},left_leg:{P:[2.0f,-12.0f,0.0f]},right_armwear:{P:[0.0f,-4.0f,0.0f]},outer:{P:[0.0f,4.0f,0.0f]},left_legwear:{P:[0.0f,-6.0f,0.0f]},body:{P:[0.0f,8.0f,0.0f],R:[0.0f,-29.0f,0.0f]},bodywear:{P:[0.0f,-6.0f,0.0f]},head:{P:[0.0f,8.0f,0.0f],R:[7.0f,-51.0f,0.0f]},left_arm:{P:[6.0f,-2.0f,0.0f]},right_leg:{P:[-2.0f,-12.0f,0.0f]},right_legwear:{P:[0.0f,-6.0f,0.0f]},anchor:{P:[0.0f,16.0f,0.0f]},left_armwear:{P:[0.0f,-4.0f,0.0f]}}},BodyParts:[{Limb:\"head\",Morph:{Settings:{Hands:1b},Name:\"blockbuster.mchorse/head\"}}],Settings:{Hands:1b},Name:\"blockbuster.fred_3d\"}");
 *        var morph = ui.morph(m).id("icon");
 *
 *        morph.rx(0.5, -150).ry(1, -250).wh(300, 250).position(-0.017, 1.367, 0).rotation(0, 0).distance(2.2).fov(40).enabled(false);
 *
 *        // Draw foreground
 *        var graphics = ui.graphics().rx(0.5, -150).ry(1, -250).wh(300, 250);
 *
 *        // Draw small rectangles
 *        for (var i = 0; i < 100; i++)
 *        {
 *            var x = Math.random() * 280 + 10;
 *            var y = Math.random() * 230 + 10;
 *
 *            graphics.rect(x - 1, y - 1, 2, 2, 0x88000000 + Math.random() * 0xffffff);
 *        }
 *
 *        graphics.gradient(0, 210, 300, 40, 0x00ff0000, 0xffff0000);
 *        // This is Thanos' infinity gauntlet. just sayin in case it gets removed xd
 *        graphics.image("https://i.pinimg.com/originals/6d/5a/99/6d5a99fa0f01dff1dd5de5e3b8244f8d.png", 0, 75, 55, 100);
 *        graphics.text("McThanos", 120, 230, 0xffffff);
 *
 *        ui.label("Graphic Design is my passion").color(0x00ff00).background(0x88000000).rxy(0.5, 0.25).wh(100, 20).anchor(0.5).labelAnchor(0.5);
 *
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */
public class UIGraphicsComponent extends UIComponent
{
    public List<Graphic> graphics = new ArrayList<Graphic>();

    public UIGraphicsComponent removeAll()
    {
        this.change("Graphics");
        this.graphics.clear();

        return this;
    }

    /**
     * Draw a solid colored rectangle.
     *
     * @param color ARGB color that fills the rectangle.
     */
    public Graphic rect(int color)
    {
        return this.rect(0, 0, 0, 0, color);
    }

    /**
     * Draw a solid colored rectangle relative to graphics component's frame.
     *
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @param color ARGB color that fills the rectangle.
     */
    public Graphic rect(int x, int y, int w, int h, int color)
    {
        return this.addGraphic(new RectGraphic(x, y, w, h, color));
    }

    /**
     * Draw a vertical gradient rectangle.
     *
     * @param primary ARGB color that fills top part of the gradient.
     * @param secondary ARGB color that fills bottom part of the gradient.
     */
    public Graphic gradient(int primary, int secondary)
    {
        return this.gradient(primary, secondary, false);
    }

    /**
     * Draw a vertical/horizontal gradient rectangle.
     *
     * @param primary ARGB color that fills top part of the gradient.
     * @param secondary ARGB color that fills bottom part of the gradient.
     * @param horizontal Whether gradient is horizontal (<code>true</code>) or vertical (<code>false</code>).
     */
    public Graphic gradient(int primary, int secondary, boolean horizontal)
    {
        return this.gradient(0, 0, 0, 0, primary, secondary, horizontal);
    }

    /**
     * Draw a vertical gradient rectangle relative to graphics component's frame.
     *
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @param primary ARGB color that fills top part of the gradient.
     * @param secondary ARGB color that fills bottom part of the gradient.
     */
    public Graphic gradient(int x, int y, int w, int h, int primary, int secondary)
    {
        return this.gradient(x, y, w, h, primary, secondary, false);
    }

    /**
     * Draw a gradient rectangle relative to graphics component's frame.
     *
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @param primary ARGB color that fills top or left part of the gradient.
     * @param secondary ARGB color that fills bottom or right part of the gradient.
     * @param horizontal Whether gradient is horizontal (<code>true</code>) or vertical (<code>false</code>).
     */
    public Graphic gradient(int x, int y, int w, int h, int primary, int secondary, boolean horizontal)
    {
        return this.addGraphic(new GradientGraphic(x, y, w, h, primary, secondary, horizontal));
    }

    /**
     * Draw an image.
     */
    public Graphic image(String image, int textureWidth, int textureHeight)
    {
        return this.image(image, textureWidth, textureHeight, 0xffffffff);
    }

    /**
     * Draw an image.
     */
    public Graphic image(String image, int textureWidth, int textureHeight, int primary)
    {
        return this.image(image, 0, 0, 0, 0, textureWidth, textureHeight, primary);
    }

    /**
     * Draw an image relative to graphics component's frame.
     *
     * <p>Image argument is a so called "resource location." For example, if you want
     * to draw pig's skin on the screen you can input "minecraft:textures/entity/pig/pig.png"
     * and it will draw it on the screen.</p>
     *
     * <p>If you have Blockbuster enabled, it can also display images from internet
     * by inputting image's URL. Although, sometimes it won't work due to incorrect headers
     * that doesn't identify a web-browser.</p>
     */
    public Graphic image(String image, int x, int y, int w, int h)
    {
        return this.image(image, x, y, w, h, w, h, 0xffffffff);
    }

    /**
     * Draw an image relative to graphics component's frame with known texture size.
     */
    public Graphic image(String image, int x, int y, int w, int h, int textureWidth, int textureHeight)
    {
        return this.image(image, x, y, w, h, textureWidth, textureHeight, 0xffffffff);
    }

    /**
     * Draw an image relative to graphics component's frame with known texture size and color.
     */
    public Graphic image(String image, int x, int y, int w, int h, int textureWidth, int textureHeight, int primary)
    {
        return this.addGraphic(new ImageGraphic(RLUtils.create(image), x, y, w, h, textureWidth, textureHeight, primary));
    }

    /**
     * Draw a text label relative to graphics component's frame.
     *
     * @param color ARGB text's font color.
     */
    public Graphic text(String text, int x, int y, int color)
    {
        return this.text(text, x, y, color, 0, 0);
    }

    /**
     * Draw a text label with an anchor relative to graphics component's frame.
     *
     * @param color ARGB text's font color.
     * @param anchorX Horizontal anchor (<code>0..1</code>).
     * @param anchorY Vertical anchor (<code>0..1</code>).
     */
    public Graphic text(String text, int x, int y, int color, float anchorX, float anchorY)
    {
        return this.text(text, x, y, 0, 0, color, anchorX, anchorY);
    }

    /**
     * Draw a text label with an anchor relative to graphics component's frame.
     *
     * @param color ARGB text's font color.
     * @param anchorX Horizontal anchor (<code>0..1</code>).
     * @param anchorY Vertical anchor (<code>0..1</code>).
     */
    public Graphic text(String text, int x, int y, int w, int h, int color, float anchorX, float anchorY)
    {
        return this.addGraphic(new TextGraphic(text, x, y, w, h, color, anchorX, anchorY));
    }

    /**
     * Draw a McLib icon relative to graphics component's frame.
     *
     * <p>Most of the icons are <code>16x16</code> so keep that in mind.</p>
     *
     * @param color ARGB color that used to render an icon.
     */
    public Graphic icon(String icon, int x, int y, int color)
    {
        return this.icon(icon, x, y, color, 0, 0);
    }

    /**
     * Draw a McLib icon with an anchor relative to graphics component's frame.
     *
     * @param color ARGB color that used to render an icon.
     * @param anchorX Horizontal anchor (<code>0..1</code>).
     * @param anchorY Vertical anchor (<code>0..1</code>).
     */
    public Graphic icon(String icon, int x, int y, int color, float anchorX, float anchorY)
    {
        return this.addGraphic(new IconGraphic(icon, x, y, color, anchorX, anchorY));
    }

    /**
     * Draw a drop shadow.
     *
     * @param primary ARGB color that fills inside.
     * @param secondary ARGB color that fills outside.
     * @param offset Fading shadow's distance from the given box using <code>x</code>,
     * <code>y</code>, <code>w</code>, and <code>h</code> arguments.
     */
    public Graphic shadow(int primary, int secondary, int offset)
    {
        return this.shadow(0, 0, 0, 0, primary, secondary, offset);
    }

    /**
     * Draw a drop shadow.
     *
     * @param w Width of the rectangle.
     * @param h Height of the rectangle.
     * @param primary ARGB color that fills inside.
     * @param secondary ARGB color that fills outside.
     * @param offset Fading shadow's distance from the given box using <code>x</code>,
     * <code>y</code>, <code>w</code>, and <code>h</code> arguments.
     */
    public Graphic shadow(int x, int y, int w, int h, int primary, int secondary, int offset)
    {
        return this.addGraphic(new ShadowGraphic(x, y, w, h, primary, secondary, offset));
    }

    @DiscardMethod
    private <T extends Graphic> T addGraphic(T graphic)
    {
        this.change("Graphics");
        this.graphics.add(graphic);

        return graphic;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiGraphics element = new GuiGraphics(mc);

        element.graphics.addAll(this.graphics);

        return this.apply(element, context);
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        if (key.equals("Graphics"))
        {
            GuiGraphics graphics = (GuiGraphics) element;

            graphics.graphics.clear();
            graphics.graphics.addAll(this.graphics);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        NBTTagList list = new NBTTagList();

        for (Graphic graphic : this.graphics)
        {
            list.appendTag(Graphic.toNBT(graphic));
        }

        tag.setTag("Graphics", list);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Graphics"))
        {
            NBTTagList list = tag.getTagList("Graphics", Constants.NBT.TAG_COMPOUND);

            this.graphics.clear();

            for (int i = 0, c = list.tagCount(); i < c; i++)
            {
                Graphic graphic = Graphic.fromNBT(list.getCompoundTagAt(i));

                if (graphic != null)
                {
                    this.graphics.add(graphic);
                }
            }
        }
    }
}