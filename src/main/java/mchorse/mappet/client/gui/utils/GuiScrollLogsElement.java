package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

public class GuiScrollLogsElement extends GuiScrollElement
{
    public boolean background = false;

    public GuiScrollLogsElement(Minecraft mc)
    {
        super(mc);
    }

    public GuiScrollLogsElement background()
    {
        this.background = true;
        return this;
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.background)
        {
            this.area.draw(0x78000000);
        }

        super.draw(context);
    }
}
