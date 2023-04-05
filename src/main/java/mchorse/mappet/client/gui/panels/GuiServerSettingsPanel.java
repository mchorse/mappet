package mchorse.mappet.client.gui.panels;

import mchorse.mappet.EventHandler;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.triggers.Trigger;
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
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.list.GuiLabelListElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    public GuiIconElement layoutToggleIcon;

    public GuiScrollElement editor;

    public GuiLabelListElement<String> forgeTriggers;

    public GuiTriggerElement forgeTrigger;

    public GuiElement globalTriggersLayout;

    public GuiElement forgeTriggersLayout;

    private ServerSettings settings;

    private String lastTarget;

    private String lastTrigger = "player_chat";

    private String lastForgeTrigger = "";

    private String lastStates = "~";

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

        this.globalTriggersLayout = new GuiElement(mc);
        this.globalTriggersLayout.flex().relative(this).wh(0.5F, 0.5F);

        this.triggers = new GuiLabelListElement<String>(mc, (l) -> this.fillTrigger(l.get(0), false));
        this.triggers.background().flex().relative(this).x(0.5F, 10).y(35).w(0.5F, -20).h(246);

        this.trigger = new GuiTriggerElement(mc).onClose(this::updateCurrentTrigger);
        this.trigger.flex().relative(this).x(1F, -10).y(1F, -10).wh(120, 20).anchor(1F, 1F);
        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this).x(0.5F).y(281).w(0.5F).h(1F, -311).column(5).scroll().stretch().padding(10);

        GuiLabel triggersLabel = Elements.label(IKey.lang("mappet.gui.settings.title")).anchor(0, 0.5F).background();

        triggersLabel.flex().relative(this).x(0.5F, 10).y(10).wh(120, 20);

        this.forgeTriggersLayout = new GuiElement(mc);
        this.forgeTriggersLayout.flex().relative(this).wh(0.5F, 0.5F);
        this.forgeTriggersLayout.setVisible(false);

        this.forgeTriggers = new GuiLabelListElement<String>(mc, (l) -> this.fillForgeTrigger(l.get(0), false));
        this.forgeTriggers.background().flex().relative(this).x(0.5F, 10).y(35).w(0.5F, -20).h(246);
        this.forgeTriggers.context(() ->
                new GuiSimpleContextMenu(mc)
                        .action(Icons.ADD, IKey.lang("mappet.gui.settings.forge.add"), this::addForgeTrigger)
                        .action(Icons.ADD, IKey.lang("mappet.gui.settings.forge.add_from_list"), this::addForgeTriggerFromList)
                        .action(Icons.REMOVE, IKey.lang("mappet.gui.settings.forge.remove"), this::removeCurrentForgeTrigger));

        this.forgeTrigger = new GuiTriggerElement(mc).onClose(this::updateCurrentForgeTrigger);
        this.forgeTrigger.flex().relative(this).x(1F, -10).y(1F, -10).wh(120, 20).anchor(1F, 1F);

        GuiText forgeAttention = new GuiText(this.mc).text(IKey.lang("mappet.gui.settings.forge.attention"));
        forgeAttention.flex().relative(this).x(0.5F).y(281).w(0.5F).h(1F, -311);
        forgeAttention.padding(10);


        GuiLabel forgeTriggersLabel = Elements.label(IKey.lang("mappet.gui.settings.forge_title")).anchor(0, 0.5F).background();

        forgeTriggersLabel.flex().relative(this).x(0.5F, 10).y(10).wh(120, 20);

        this.hotkeys = new GuiIconElement(mc, Icons.DOWNLOAD, (b) -> this.openHotkeysEditor());
        this.hotkeys.tooltip(IKey.lang("mappet.gui.settings.hotkeys"), Direction.LEFT);
        this.hotkeys.flex().relative(this).x(1F, -16).y(20).wh(20, 20).anchor(0.5F, 0.5F);

        this.layoutToggleIcon = new GuiIconElement(mc, Icons.PROCESSOR, (i) -> this.toggleTriggerLayouts());
        boolean enableForgeTriggers = Mappet.enableForgeTriggers.get();
        IKey tooltip = IKey.lang("mappet.gui.settings.forge." + (enableForgeTriggers ? "toggle_triggers" : "disabled"));
        this.layoutToggleIcon.tooltip(tooltip, Direction.LEFT).setEnabled(enableForgeTriggers);
        this.layoutToggleIcon.disabledColor(0xFF880000);
        this.layoutToggleIcon.flex().relative(this).x(1F, -48).y(20).wh(20, 20).anchor(0.5F, 0.5F);


        this.states.add(this.statesTitle, this.statesSwitch, this.statesAdd, this.statesEditor);
        this.globalTriggersLayout.add(this.triggers, this.editor, this.trigger, triggersLabel);
        this.forgeTriggersLayout.add(this.forgeTriggers, this.forgeTrigger, forgeTriggersLabel, forgeAttention);
        this.add(this.states, this.layoutToggleIcon, this.hotkeys, this.globalTriggersLayout, this.forgeTriggersLayout);
    }

    public void toggleTriggerLayouts()
    {
        boolean trigger = this.globalTriggersLayout.isVisible();

        this.layoutToggleIcon.both(trigger ? Icons.COPY : Icons.PROCESSOR);
        this.globalTriggersLayout.setVisible(!trigger);
        this.forgeTriggersLayout.setVisible(trigger);
    }

    public void addForgeTrigger()
    {
        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("mappet.gui.panels.modals.add"), this::addForgeTrigger));
    }

    public void addForgeTrigger(String name)
    {
        if (this.forgeTriggers.getList().contains(name))
        {
            return;
        }
        this.forgeTriggers.add(IKey.str(name), name);
        this.settings.registeredForgeTriggers.put(name, new Trigger());
    }

    public void removeCurrentForgeTrigger()
    {
        Label<String> current = this.forgeTriggers.getCurrentFirst();

        if (current == null)
        {
            return;
        }

        String name = current.value;

        this.forgeTriggers.remove(current);
        this.settings.registeredForgeTriggers.remove(name);
    }

    public void addForgeTriggerFromList()
    {
        Set<String> events = EventHandler.getRegisteredEvents().stream()
                .map(EventHandler::getEventClassName).collect(Collectors.toSet());
        GuiStringOverlayPanel overlay = new GuiStringOverlayPanel(this.mc, IKey.lang("mappet.gui.forge.pick"), false, events, this::addForgeTrigger);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.lastTarget), 0.5F, 0.6F);
    }

    private void updateCurrentTrigger()
    {
        Trigger trigger = this.settings.registered.get(this.lastTrigger);

        this.triggers.getCurrentFirst().title = this.createTooltip(this.lastTrigger, trigger);
    }

    private void updateCurrentForgeTrigger()
    {
        Trigger trigger = this.settings.registeredForgeTriggers.get(this.lastForgeTrigger);

        this.forgeTriggers.getCurrentFirst().title = this.createForgeTooltip(this.lastForgeTrigger, trigger);
    }

    public IKey createTooltip(String key, Trigger trigger)
    {
        IKey title = IKey.lang("mappet.gui.settings.triggers." + key);

        if (trigger.blocks.isEmpty())
        {
            return title;
        }

        IKey count = IKey.str(" §7(§6" + trigger.blocks.size() + "§7)§r");

        return IKey.comp(title, count);
    }

    public IKey createForgeTooltip(String key, Trigger trigger)
    {
        IKey title = IKey.str(key);

        if (trigger.blocks.isEmpty())
        {
            return title;
        }

        IKey count = IKey.str(" §7(§6" + trigger.blocks.size() + "§7)§r");

        return IKey.comp(title, count);
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
            this.triggers.add(this.createTooltip(key, this.settings.registered.get(key)), key);
        }

        this.triggers.sort();
        this.triggers.setCurrentValue(this.lastTrigger);

        this.forgeTriggers.clear();

        for (String key : this.settings.registeredForgeTriggers.keySet())
        {
            this.forgeTriggers.add(this.createForgeTooltip(key, this.settings.registeredForgeTriggers.get(key)), key);
        }

        this.forgeTriggers.sort();
        this.forgeTrigger.setVisible(false);

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

    private void fillForgeTrigger(Label<String> trigger, boolean select)
    {
        this.forgeTrigger.set(this.settings.registeredForgeTriggers.get(trigger.value));
        this.forgeTrigger.setVisible(true);

        if (select)
        {
            this.forgeTriggers.setCurrentScroll(trigger);
        }

        this.lastForgeTrigger = trigger.value;

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
        Dispatcher.sendToServer(new PacketRequestStates(this.lastStates));
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