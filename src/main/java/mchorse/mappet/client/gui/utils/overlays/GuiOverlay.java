package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiOverlay extends GuiElement
{
    public static void addOverlay(GuiContext context, GuiOverlayPanel panel)
    {
        addOverlay(context, new GuiOverlay(context.mc, panel));
    }

    public static void addOverlay(GuiContext context, GuiOverlayPanel panel, float w, float h)
    {
        addOverlay(context, new GuiOverlay(context.mc, panel, w, h));
    }

    public static void addOverlay(GuiContext context, GuiOverlayPanel panel, int w, int h)
    {
        addOverlay(context, new GuiOverlay(context.mc, panel, w, h));
    }

    public static void addOverlay(GuiContext context, GuiOverlay overlay)
    {
        overlay.flex().relative(context.screen.root).wh(1F, 1F);
        context.screen.root.add(overlay);
        context.screen.root.resize();
    }

    public GuiOverlay(Minecraft mc, GuiOverlayPanel overlay)
    {
        super(mc);

        overlay.flex().relative(this).xy(0.5F, 0.5F).wh(0.5F, 0.5F).anchor(0.5F, 0.5F);
        this.markContainer().add(overlay);
    }

    public GuiOverlay(Minecraft mc, GuiOverlayPanel overlay, float w, float h)
    {
        this(mc, overlay);

        overlay.flex().wh(w, h);
    }

    public GuiOverlay(Minecraft mc, GuiOverlayPanel overlay, int w, int h)
    {
        this(mc, overlay);

        overlay.flex().wh(w, h);
    }

    /* Don't pass user input down the line... */

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        this.removeFromParent();
        GuiUtils.playClick();

        return true;
    }

    @Override
    public boolean mouseScrolled(GuiContext context)
    {
        super.mouseScrolled(context);

        return true;
    }

    @Override
    public boolean keyTyped(GuiContext context)
    {
        if (super.keyTyped(context))
        {
            return true;
        }

        if (context.keyCode == Keyboard.KEY_ESCAPE)
        {
            this.removeFromParent();
            GuiUtils.playClick();
        }

        return true;
    }

    @Override
    public void draw(GuiContext context)
    {
        this.area.draw(ColorUtils.HALF_BLACK);

        super.draw(context);
    }
}