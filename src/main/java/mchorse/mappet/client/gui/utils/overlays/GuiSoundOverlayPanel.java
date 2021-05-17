package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Consumer;

public class GuiSoundOverlayPanel extends GuiResourceLocationOverlayPanel
{
    public GuiSoundOverlayPanel(Minecraft mc, Consumer<ResourceLocation> callback)
    {
        super(mc, IKey.lang("mappet.gui.overlays.sounds.main"), ForgeRegistries.SOUND_EVENTS.getKeys(), callback);

        GuiIconElement edit = new GuiIconElement(mc, Icons.SOUND, (b) -> this.playSound());

        edit.flex().relative(this.close).x(-3).w(16).h(1F).anchorX(1F);
        this.close.add(edit);
    }

    private void playSound()
    {
        if (this.rls.list.getIndex() <= 0)
        {
            return;
        }

        SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(this.rls.list.getCurrentFirst()));

        if (event != null)
        {
            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(event, 1.0F));
        }
    }
}