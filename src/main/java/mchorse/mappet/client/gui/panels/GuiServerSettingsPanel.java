package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.events.GuiEventHotkeysOverlayPanel;
import mchorse.mappet.client.gui.states.GuiStatesEditor;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketRequestServerSettings;
import mchorse.mappet.network.common.content.PacketRequestStates;
import mchorse.mappet.network.common.content.PacketServerSettings;
import mchorse.mappet.network.common.content.PacketStates;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.ScrollDirection;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiServerSettingsPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiElement states;
    public GuiStatesEditor statesEditor;
    public GuiLabel statesTitle;
    public GuiIconElement statesSwitch;
    public GuiIconElement statesAdd;

    public GuiScrollElement editor;
    public GuiButtonElement hotkeys;

    private ServerSettings settings;
    private String target;

    public GuiServerSettingsPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.states = new GuiElement(mc);
        this.states.flex().relative(this).wh(0.5F, 1F);

        this.statesEditor = new GuiStatesEditor(mc);
        this.statesEditor.flex().relative(this.states).y(20).w(1F).h(1F, -20);
        this.statesTitle = Elements.label(IKey.str("")).anchor(0, 0.5F);
        this.statesTitle.flex().relative(this.states).xy(10, 10).wh(120, 20);
        this.statesSwitch = new GuiIconElement(mc, Icons.SEARCH, this::openSearch);
        this.statesSwitch.flex().relative(this.states).x(1F, -50).y(10);
        this.statesAdd = new GuiIconElement(mc, Icons.ADD, this::addState);
        this.statesAdd.flex().relative(this.states).x(1F, -30).y(10);

        this.editor = new GuiScrollElement(mc, ScrollDirection.HORIZONTAL);
        this.editor.flex().relative(this).x(0.5F).wh(0.5F, 1F).column(5).scroll().width(180).padding(15);

        this.hotkeys = new GuiButtonElement(mc, IKey.lang("mappet.gui.settings.hotkeys"), (b) -> this.openHotkeysEditor());

        this.states.add(this.statesTitle, this.statesSwitch, this.statesAdd, this.statesEditor);
        this.add(this.states, this.editor);
    }

    private void openSearch(GuiIconElement element)
    {
        List<String> targets = new ArrayList<String>();

        targets.add("~");

        for (EntityPlayer player : this.mc.world.playerEntities)
        {
            targets.add(player.getGameProfile().getName());
        }

        GuiStringOverlayPanel overlay = new GuiStringOverlayPanel(this.mc, IKey.lang("mappet.gui.states.pick"), false, targets, (target) ->
        {
            if (target.isEmpty())
            {
                return;
            }

            this.save();

            Dispatcher.sendToServer(new PacketRequestStates(target));
        });

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.target), 0.4F, 0.6F);
    }

    private void addState(GuiIconElement element)
    {
        this.statesEditor.addNew();
    }

    private void openHotkeysEditor()
    {
        GuiEventHotkeysOverlayPanel overlay = new GuiEventHotkeysOverlayPanel(this.mc, this.settings.hotkeys);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
    }

    public void fill(NBTTagCompound tag)
    {
        this.settings = new ServerSettings(null);
        this.settings.deserializeNBT(tag);

        this.editor.removeAll();
        this.editor.add(this.hotkeys);

        /* TODO: add variable tooltips */
        for (Map.Entry<String, Trigger> entry : this.settings.registered.entrySet())
        {
            GuiTriggerElement trigger = new GuiTriggerElement(this.mc, entry.getValue());
            GuiLabel label = Elements.label(IKey.lang("mappet.gui.settings.triggers." + entry.getKey())).background();
            GuiElement element = Elements.column(this.mc, 5, label.marginBottom(4), trigger);

            this.editor.add(element.marginTop(12));
        }

        this.resize();
    }

    public void fillStates(String target, NBTTagCompound data)
    {
        States states = new States();

        this.statesTitle.label = target.equals("~") ? IKey.lang("mappet.gui.states.server") : IKey.format("mappet.gui.states.player", target);
        states.deserializeNBT(data);
        this.statesEditor.set(states);
        this.target = target;
    }

    public void save()
    {
        if (this.settings != null)
        {
            Dispatcher.sendToServer(new PacketServerSettings(this.settings.serializeNBT()));
        }

        if (this.statesEditor.get() != null)
        {
            Dispatcher.sendToServer(new PacketStates(this.target, this.statesEditor.get().serializeNBT()));
        }
    }

    @Override
    public void appear()
    {
        super.appear();

        Dispatcher.sendToServer(new PacketRequestServerSettings());
        Dispatcher.sendToServer(new PacketRequestStates("~"));
    }

    @Override
    public void disappear()
    {
        super.disappear();

        this.save();
    }

    @Override
    public void close()
    {
        super.close();

        this.save();
        this.statesEditor.set(null);
    }
}