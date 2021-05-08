package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiModelRenderer;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.metamorph.api.Morph;
import mchorse.metamorph.api.MorphUtils;
import net.minecraft.client.Minecraft;

public class GuiMorphRenderer extends GuiModelRenderer
{
    public Morph morph = new Morph();

    public GuiMorphRenderer(Minecraft mc)
    {
        super(mc);
    }

    @Override
    protected void update()
    {
        super.update();

        if (!this.morph.isEmpty())
        {
            this.morph.get().update(this.entity);
        }
    }

    @Override
    protected void drawUserModel(GuiContext context)
    {
        GuiModelRenderer.disableRenderingFlag();

        MorphUtils.render(this.morph.get(), this.entity, 0.0D, 0.0D, 0.0D, this.yaw, context.partialTicks);
    }

    /**
     * Don't draw the grid
     */
    @Override
    protected void drawGround()
    {}
}