package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.client.gui.utils.GuiGraphics;
import mchorse.mappet.client.gui.utils.graphics.GradientGraphic;
import mchorse.mappet.client.gui.utils.graphics.Graphic;
import mchorse.mappet.client.gui.utils.graphics.ImageGraphic;
import mchorse.mappet.client.gui.utils.graphics.RectGraphic;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class UIGraphicsComponent extends UIComponent
{
    public List<Graphic> graphics = new ArrayList<Graphic>();

    public UIGraphicsComponent removeAll()
    {
        this.change("Graphics");
        this.graphics.clear();

        return this;
    }

    public UIGraphicsComponent rect(int x, int y, int w, int h, int color)
    {
        return this.addGraphic(new RectGraphic(x, y, w, h, color));
    }

    public UIGraphicsComponent gradient(int x, int y, int w, int h, int primary, int secondary)
    {
        return this.gradient(x, y, w, h, primary, secondary, false);
    }

    public UIGraphicsComponent gradient(int x, int y, int w, int h, int primary, int secondary, boolean horizontal)
    {
        return this.addGraphic(new GradientGraphic(x, y, w, h, primary, secondary, horizontal));
    }

    public UIGraphicsComponent image(String image, int x, int y, int w, int h)
    {
        return this.image(image, x, y, w, h, w, h, 0xffffffff);
    }

    public UIGraphicsComponent image(String image, int x, int y, int w, int h, int textureWidth, int textureHeight)
    {
        return this.image(image, x, y, w, h, textureWidth, textureHeight, 0xffffffff);
    }

    public UIGraphicsComponent image(String image, int x, int y, int w, int h, int textureWidth, int textureHeight, int primary)
    {
        return this.addGraphic(new ImageGraphic(new ResourceLocation(image), x, y, w, h, textureWidth, textureHeight, primary));
    }

    private UIGraphicsComponent addGraphic(Graphic graphic)
    {
        this.change("Graphics");
        this.graphics.add(graphic);

        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiGraphics element = new GuiGraphics(mc);

        element.graphics.addAll(this.graphics);

        return this.apply(element, context);
    }

    @Override
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