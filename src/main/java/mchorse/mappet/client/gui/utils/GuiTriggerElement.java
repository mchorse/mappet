package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiResourceLocationOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiSoundOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTriggerElement extends GuiElement
{
    public GuiButtonElement soundEvent;
    public GuiTextElement triggerEvent;
    public GuiTextElement command;
    public GuiTextElement dialogue;

    public GuiIconElement editEvent;
    public GuiIconElement editDialogue;

    private Trigger trigger;

    public GuiTriggerElement(Minecraft mc)
    {
        this(mc, null);
    }

    public GuiTriggerElement(Minecraft mc, Trigger trigger)
    {
        super(mc);

        this.soundEvent = new GuiButtonElement(mc, IKey.str("Pick sound event..."), (b) -> this.openPickSoundOverlay());
        this.soundEvent.flex().relative(this).y(12).w(0.5F, -3);

        this.triggerEvent = new GuiTextElement(mc, 1000, (text) -> this.trigger.triggerEvent = text);
        this.triggerEvent.flex().relative(this).x(1F).y(12).w(0.5F, -2).anchorX(1F);

        this.command = new GuiTextElement(mc, 10000, (text) -> this.trigger.command = text);
        this.command.flex().relative(this).y(49).w(1F);

        this.dialogue = new GuiTextElement(mc, 10000, (text) -> this.trigger.dialogue = text);
        this.dialogue.flex().relative(this).y(1F, -20).w(1F);
        
        this.editEvent = new GuiIconElement(mc, Icons.EDIT, (b) -> this.editEvent(this.triggerEvent.field.getText()));
        this.editDialogue = new GuiIconElement(mc, Icons.EDIT, (b) -> this.editDialogue(this.dialogue.field.getText()));

        this.editEvent.flex().relative(this.triggerEvent).x(1F, -18).y(2).wh(16, 16);
        this.editDialogue.flex().relative(this.dialogue).x(1F, -18).y(2).wh(16, 16);

        this.triggerEvent.add(this.editEvent);
        this.dialogue.add(this.editDialogue);

        this.flex().h(106);

        this.add(this.soundEvent, this.triggerEvent, this.command, this.dialogue);

        this.set(trigger);
    }

    private void editEvent(String text)
    {
        GuiMappetDashboard dashboard = GuiMappetDashboard.get(this.mc);

        this.openPanel(text, dashboard, dashboard.event);
    }

    private void editDialogue(String text)
    {
        GuiMappetDashboard dashboard = GuiMappetDashboard.get(this.mc);

        this.openPanel(text, dashboard, dashboard.dialogue);
    }

    private void openPanel(String text, GuiMappetDashboard dashboard, GuiMappetDashboardPanel panel)
    {
        if (panel.names.list.getList().isEmpty())
        {
            panel.requestDataNames();
        }

        if (panel.names.list.getList().contains(text))
        {
            this.openDashboard(dashboard);

            dashboard.panels.setPanel(panel);
            panel.pickData(text);
            panel.names.list.setCurrentScroll(text);
        }
    }

    private void openDashboard(GuiMappetDashboard dashboard)
    {
        if (!(this.mc.currentScreen instanceof GuiMappetDashboard))
        {
            this.mc.displayGuiScreen(dashboard);
        }
    }

    private void openPickSoundOverlay()
    {
        GuiResourceLocationOverlayPanel overlay = new GuiSoundOverlayPanel(this.mc, (rl) -> this.trigger.soundEvent = rl.toString()).set(this.trigger.soundEvent);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.9F);
    }

    public Trigger get()
    {
        return this.trigger;
    }

    public void set(Trigger trigger)
    {
        this.trigger = trigger;

        if (trigger != null)
        {
            this.triggerEvent.setText(trigger.triggerEvent);
            this.command.setText(trigger.command);
            this.dialogue.setText(trigger.dialogue);
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        this.font.drawStringWithShadow("Sound event", this.soundEvent.area.x, this.soundEvent.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow("Trigger event", this.triggerEvent.area.x, this.triggerEvent.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow("Command", this.command.area.x, this.command.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow("Dialogue", this.dialogue.area.x, this.dialogue.area.y - 12, 0xffffff);
    }
}