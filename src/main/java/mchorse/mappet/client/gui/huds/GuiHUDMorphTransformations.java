package mchorse.mappet.client.gui.huds;

import mchorse.mappet.api.huds.HUDMorph;
import mchorse.mclib.client.gui.framework.elements.input.GuiTransformations;
import net.minecraft.client.Minecraft;

/**
 * HUD morph transformation editor
 */
public class GuiHUDMorphTransformations extends GuiTransformations
{
    public HUDMorph morph;

    public GuiHUDMorphTransformations(Minecraft mc)
    {
        super(mc);
    }

    public void setMorph(HUDMorph morph)
    {
        this.morph = morph;

        if (morph != null)
        {
            this.fillT(morph.translate.x, morph.translate.y, morph.translate.z);
            this.fillS(morph.scale.x, morph.scale.y, morph.scale.z);
            this.fillR(morph.rotate.x, morph.rotate.y, morph.rotate.z);
        }
    }

    public void setT(double x, double y, double z)
    {
        this.morph.translate.x = (float) x;
        this.morph.translate.y = (float) y;
        this.morph.translate.z = (float) z;
    }

    public void setS(double x, double y, double z)
    {
        this.morph.scale.x = (float) x;
        this.morph.scale.y = (float) y;
        this.morph.scale.z = (float) z;
    }

    public void setR(double x, double y, double z)
    {
        this.morph.rotate.x = (float) x;
        this.morph.rotate.y = (float) y;
        this.morph.rotate.z = (float) z;
    }
}