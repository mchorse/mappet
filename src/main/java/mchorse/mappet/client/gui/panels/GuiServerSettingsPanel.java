package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.states.States;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.events.GuiTriggerHotkeysOverlayPanel;
import mchorse.mappet.client.gui.states.GuiStatesEditor;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiStringOverlayPanel;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketRequestServerSettings;
import mchorse.mappet.network.common.content.PacketRequestStates;
import mchorse.mappet.network.common.content.PacketServerSettings;
import mchorse.mappet.network.common.content.PacketStates;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class GuiServerSettingsPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiElement states;
    public GuiStatesEditor statesEditor;
    public GuiLabel statesTitle;
    public GuiIconElement statesSwitch;
    public GuiIconElement statesAdd;

    public GuiLabelListElement<String> triggers;
    public GuiTriggerElement trigger;
    public GuiIconElement hotkeys;
    public GuiScrollElement editor;

    private ServerSettings settings;
    private String lastTarget;
    private String lastTrigger = "player_chat";

    public GuiServerSettingsPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.states = new GuiElement(mc);
        this.states.flex().relative(this).wh(0.5F, 1F);

        this.statesEditor = new GuiStatesEditor(mc);
        this.statesEditor.flex().relative(this.states).y(25).w(1F).h(1F, -25);
        this.statesTitle = Elements.label(IKey.str("")).anchor(0, 0.5F).background();
        this.statesTitle.flex().relative(this.states).xy(10, 10).wh(120, 20);
        this.statesSwitch = new GuiIconElement(mc, Icons.SEARCH, this::openSearch);
        this.statesSwitch.flex().relative(this.states).x(1F, -50).y(10);
        this.statesAdd = new GuiIconElement(mc, Icons.ADD, this::addState);
        this.statesAdd.flex().relative(this.states).x(1F, -30).y(10);

        this.triggers = new GuiLabelListElement<String>(mc, (l) -> this.fillTrigger(l.get(0), false));
        this.triggers.background().flex().relative(this).x(0.5F, 10).y(35).w(0.5F, -20).h(96);
        this.trigger = new GuiTriggerElement(mc);
        this.trigger.flex().relative(this).x(1F, -10).y(1F, -10).wh(120, 20).anchor(1F, 1F);
        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this).x(0.5F).y(131).w(0.5F).h(1F, -161).column(5).scroll().stretch().padding(10);

        this.hotkeys = new GuiIconElement(mc, Icons.DOWNLOAD, (b) -> this.openHotkeysEditor());
        this.hotkeys.tooltip(IKey.lang("mappet.gui.settings.hotkeys"), Direction.LEFT);
        this.hotkeys.flex().relative(this).x(1F, -16).y(20).wh(20, 20).anchor(0.5F, 0.5F);

        GuiLabel triggers = Elements.label(IKey.lang("mappet.gui.settings.title")).anchor(0, 0.5F).background();

        triggers.flex().relative(this).x(0.5F, 10).y(10).wh(120, 20);

        this.states.add(this.statesTitle, this.statesSwitch, this.statesAdd, this.statesEditor);
        this.add(this.states, this.hotkeys, this.triggers, this.editor, this.trigger, triggers);
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

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.lastTarget), 0.4F, 0.6F);
    }

    private void addState(GuiIconElement element)
    {
        this.statesEditor.addNew();
    }

    private void openHotkeysEditor()
    {
        GuiTriggerHotkeysOverlayPanel overlay = new GuiTriggerHotkeysOverlayPanel(this.mc, this.settings.hotkeys);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
    }

    public void fill(NBTTagCompound tag)
    {
        this.settings = new ServerSettings(null);
        this.settings.deserializeNBT(tag);

        this.triggers.clear();

        for (String key : this.settings.registered.keySet())
        {
            this.triggers.add(IKey.lang("mappet.gui.settings.triggers." + key), key);
        }

        this.triggers.sort();
        this.triggers.setCurrentValue(this.lastTrigger);

        this.fillTrigger(this.triggers.getCurrentFirst(), true);

        this.resize();
    }

    private void fillTrigger(Label<String> trigger, boolean select)
    {
        this.editor.removeAll();
        this.editor.add(new GuiText(this.mc).text(IKey.lang("mappet.gui.settings.triggers.descriptions." + trigger.value)));
        this.editor.add(Elements.label(IKey.lang("mappet.gui.settings.variables")).background().marginTop(16).marginBottom(8));
        this.editor.add(new GuiText(this.mc).text(IKey.lang("mappet.gui.settings.triggers.variables." + trigger.value)));

        this.trigger.set(this.settings.registered.get(trigger.value));

        if (select)
        {
            this.triggers.setCurrentScroll(trigger);
        }

        this.lastTrigger = trigger.value;

        this.resize();
    }

    public void fillStates(String target, NBTTagCompound data)
    {
        States states = new States();

        this.statesTitle.label = target.equals("~") ? IKey.lang("mappet.gui.states.server") : IKey.format("mappet.gui.states.player", target);
        states.deserializeNBT(data);
        this.statesEditor.set(states);
        this.lastTarget = target;
    }

    public void save()
    {
        if (this.settings != null)
        {
            Dispatcher.sendToServer(new PacketServerSettings(this.settings.serializeNBT()));
        }

        if (this.statesEditor.get() != null)
        {
            Dispatcher.sendToServer(new PacketStates(this.lastTarget, this.statesEditor.get().serializeNBT()));
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