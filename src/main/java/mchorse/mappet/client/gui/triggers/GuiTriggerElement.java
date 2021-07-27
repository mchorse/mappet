package mchorse.mappet.client.gui.triggers;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTriggerElement extends GuiElement
{
    public GuiButtonElement open;

    private Trigger trigger;

    public GuiTriggerElement(Minecraft mc)
    {
        this(mc, null);
    }

    public GuiTriggerElement(Minecraft mc, Trigger trigger)
    {
        super(mc);

        this.open = new GuiButtonElement(mc, IKey.lang("mappet.gui.trigger.edit"), (b) -> this.openTriggerEditor());
        this.open.flex().relative(this).wh(1F, 1F);

        this.flex().h(20);

        this.add(this.open);

        this.set(trigger);
    }

    private void openTriggerEditor()
    {
        GuiTriggerOverlayPanel panel = new GuiTriggerOverlayPanel(this.mc, this.trigger);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.55F, 0.75F);
    }

    public Trigger get()
    {
        return this.trigger;
    }

    public void set(Trigger trigger)
    {
        this.trigger = trigger;
    }
}