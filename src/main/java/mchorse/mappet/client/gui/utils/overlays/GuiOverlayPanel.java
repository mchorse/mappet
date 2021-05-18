package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiOverlayPanel extends GuiElement
{
    public GuiLabel title;
    public GuiElement icons;
    public GuiIconElement close;
    public GuiElement content;

    public GuiOverlayPanel(Minecraft mc, IKey title)
    {
        super(mc);

        this.title = Elements.label(title);
        this.close = new GuiIconElement(mc, Icons.CLOSE, (b) -> this.close());
        this.content = new GuiElement(mc);
        this.icons = new GuiElement(mc);

        this.title.flex().relative(this).xy(10, 10).w(0.5F);
        this.close.flex().wh(16, 16);
        this.icons.flex().relative(this).x(1F, -7).y(6).anchorX(1F).row(0).reverse().resize().width(16).height(16);
        this.content.flex().relative(this).xy(10, 28).w(1F, -20).h(1F, -28);

        this.icons.add(this.close);

        this.add(this.title, this.icons, this.content);
    }

    public void close()
    {
        GuiElement parent = this.getParent();

        if (parent instanceof GuiOverlay)
        {
            ((GuiOverlay) parent).closeItself();
        }
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        return super.mouseClicked(context) || this.area.isInside(context);
    }

    @Override
    public void draw(GuiContext context)
    {
        int color = McLib.primaryColor.get();

        GuiDraw.drawDropShadow(this.area.x, this.area.y, this.area.ex(), this.area.ey(), 10, 0x44000000 + color, color);
        this.area.draw(0xff000000);

        super.draw(context);
    }

    public void onClose()
    {}
}