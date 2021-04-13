package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.utils.Trigger;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import net.minecraft.client.Minecraft;

public class GuiTriggerElement extends GuiElement
{
    public GuiTextElement soundEvent;
    public GuiTextElement triggerEvent;

    private Trigger trigger;

    public GuiTriggerElement(Minecraft mc, Trigger trigger)
    {
        super(mc);

        this.trigger = trigger;

        this.soundEvent = new GuiTextElement(mc, 1000, (text) -> this.trigger.soundEvent = text);
        this.soundEvent.flex().relative(this).y(1F, -20).w(0.5F, -3);
        this.soundEvent.setText(trigger.soundEvent);

        this.triggerEvent = new GuiTextElement(mc, 1000, (text) -> this.trigger.triggerEvent = text);
        this.triggerEvent.flex().relative(this).x(1F).y(1F, -20).w(0.5F, -2).anchorX(1F);
        this.triggerEvent.setText(trigger.triggerEvent);

        this.flex().h(32);

        this.add(this.soundEvent, this.triggerEvent);
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        this.font.drawStringWithShadow("Sound event", this.soundEvent.area.x, this.soundEvent.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow("Trigger event", this.triggerEvent.area.x, this.triggerEvent.area.y - 12, 0xffffff);
    }
}