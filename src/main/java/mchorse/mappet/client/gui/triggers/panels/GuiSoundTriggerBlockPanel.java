package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.triggers.blocks.SoundTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiResourceLocationOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiSoundOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiSoundTriggerBlockPanel extends GuiStringTriggerBlockPanel<SoundTriggerBlock>
{
    public GuiCirculateElement target;

    public GuiSoundTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, SoundTriggerBlock block)
    {
        super(mc, overlay, block);

        this.target = GuiMappetUtils.createTargetCirculate(mc, block.target, (target) -> this.block.target = target);

        for (Target target : Target.values())
        {
            if (!(target == Target.PLAYER || target == Target.GLOBAL))
            {
                this.target.disable(target.ordinal());
            }
        }

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.target")).marginTop(12), this.target);

        this.addDelay();
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.sounds.main");
    }

    @Override
    protected ContentType getType()
    {
        return null;
    }

    @Override
    protected void openOverlay()
    {
        GuiResourceLocationOverlayPanel overlay = new GuiSoundOverlayPanel(this.mc, this::setSound).set(this.block.string);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.9F);
    }

    private void setSound(ResourceLocation location)
    {
        this.block.string = location == null ? "" : location.toString();
    }
}