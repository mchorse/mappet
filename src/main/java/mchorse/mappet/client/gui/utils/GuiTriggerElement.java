package mchorse.mappet.client.gui.utils;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiResourceLocationOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiSoundOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiTriggerElement extends GuiElement
{
    public GuiButtonElement soundEvent;
    public GuiButtonElement triggerEvent;
    public GuiTextElement command;
    public GuiButtonElement dialogue;

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

        this.soundEvent = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.sounds.main"), (b) -> this.openPickSoundOverlay());
        this.soundEvent.flex().relative(this).y(12).w(0.5F, -3);

        this.triggerEvent = new GuiButtonElement(mc, IKey.str("mappet.gui.overlays.event"), (text) -> this.openEventsOverlay());
        this.triggerEvent.flex().relative(this).x(1F).y(12).w(0.5F, -2).anchorX(1F);

        this.command = new GuiTextElement(mc, 10000, (text) -> this.trigger.command = text);
        this.command.flex().relative(this).y(49).w(1F);

        this.dialogue = new GuiButtonElement(mc, IKey.str("mappet.gui.overlays.dialogue"), (b) -> this.openDialoguesOverlay());
        this.dialogue.flex().relative(this).y(1F, -20).w(1F);
        
        this.editEvent = new GuiIconElement(mc, Icons.EDIT, (b) -> this.editEvent(this.trigger.triggerEvent));
        this.editDialogue = new GuiIconElement(mc, Icons.EDIT, (b) -> this.editDialogue(this.trigger.dialogue));

        this.editEvent.flex().relative(this.triggerEvent).x(1F, -18).y(2).wh(16, 16);
        this.editDialogue.flex().relative(this.dialogue).x(1F, -18).y(2).wh(16, 16);

        this.triggerEvent.add(this.editEvent);
        this.dialogue.add(this.editDialogue);

        this.flex().h(106);

        this.add(this.soundEvent, this.triggerEvent, this.command, this.dialogue);

        this.set(trigger);
    }

    public GuiTriggerElement expand()
    {
        this.soundEvent.flex().reset().relative(this).y(12).w(1F).h(20);
        this.triggerEvent.flex().reset().relative(this).y(49).w(1F).h(20);
        this.command.flex().reset().relative(this).y(86).w(1F).h(20);
        this.flex().h(143);

        return this;
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
        GuiResourceLocationOverlayPanel overlay = new GuiSoundOverlayPanel(this.mc, this::setSound).set(this.trigger.soundEvent);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.9F);
    }

    private void openEventsOverlay()
    {
        ClientProxy.requestNames(ContentType.EVENT, (names) ->
        {
            GuiStringOverlayPanel overlay = new GuiStringOverlayPanel(Minecraft.getMinecraft(), IKey.lang("mappet.gui.overlays.event"), names, (name) -> this.trigger.triggerEvent = name);

            overlay.set(this.trigger.triggerEvent);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void openDialoguesOverlay()
    {
        ClientProxy.requestNames(ContentType.DIALOGUE, (names) ->
        {
            GuiStringOverlayPanel overlay = new GuiStringOverlayPanel(Minecraft.getMinecraft(), IKey.lang("mappet.gui.overlays.dialogue"), names, (name) -> this.trigger.dialogue = name);

            overlay.set(this.trigger.dialogue);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void setSound(ResourceLocation location)
    {
        this.trigger.soundEvent = location == null ? "" : location.toString();
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
            this.command.setText(trigger.command);
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        this.font.drawStringWithShadow(I18n.format("mappet.gui.trigger.sound"), this.soundEvent.area.x, this.soundEvent.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow(I18n.format("mappet.gui.trigger.event"), this.triggerEvent.area.x, this.triggerEvent.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow(I18n.format("mappet.gui.trigger.command"), this.command.area.x, this.command.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow(I18n.format("mappet.gui.trigger.dialogue"), this.dialogue.area.x, this.dialogue.area.y - 12, 0xffffff);
    }
}