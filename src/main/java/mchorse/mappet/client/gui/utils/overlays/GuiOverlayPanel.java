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
    public GuiIconElement close;
    public GuiElement content;

    public GuiOverlayPanel(Minecraft mc, IKey title)
    {
        super(mc);

        this.title = Elements.label(title);
        this.close = new GuiIconElement(mc, Icons.CLOSE, (b) -> this.parent.removeFromParent());
        this.content = new GuiElement(mc);

        this.title.flex().relative(this).xy(10, 10).w(0.5F);
        this.close.flex().relative(this).x(1F, -10).y(10).wh(10, 8).anchorX(1F);
        this.content.flex().relative(this).xy(10, 28).w(1F, -20).h(1F, -28);

        this.add(this.title, this.close, this.content);
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
}