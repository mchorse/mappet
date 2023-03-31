package mchorse.mappet.client.gui;

import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.client.gui.panels.GuiConditionModelPanel;
import mchorse.mappet.client.gui.panels.GuiCraftingTablePanel;
import mchorse.mappet.client.gui.panels.GuiDialoguePanel;
import mchorse.mappet.client.gui.panels.GuiEventPanel;
import mchorse.mappet.client.gui.panels.GuiFactionPanel;
import mchorse.mappet.client.gui.panels.GuiHUDScenePanel;
import mchorse.mappet.client.gui.panels.GuiLogPanel;
import mchorse.mappet.client.gui.panels.GuiNpcPanel;
import mchorse.mappet.client.gui.panels.GuiQuestChainPanel;
import mchorse.mappet.client.gui.panels.GuiQuestPanel;
import mchorse.mappet.client.gui.panels.GuiRegionPanel;
import mchorse.mappet.client.gui.panels.GuiScriptPanel;
import mchorse.mappet.client.gui.panels.GuiServerSettingsPanel;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentExit;
import mchorse.mappet.utils.MPIcons;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanels;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiCreativeMorphsMenu;
import mchorse.metamorph.util.MMIcons;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiMappetDashboard extends GuiAbstractDashboard
{
    public static GuiMappetDashboard dashboard;

    public GuiServerSettingsPanel settings;
    public GuiCraftingTablePanel crafting;
    public GuiQuestPanel quest;
    public GuiEventPanel event;
    public GuiDialoguePanel dialogue;
    public GuiRegionPanel region;
    public GuiConditionModelPanel conditionModel;
    public GuiNpcPanel npc;
    public GuiFactionPanel faction;
    public GuiQuestChainPanel chain;
    public GuiScriptPanel script;
    public GuiHUDScenePanel hud;
    public GuiLogPanel logs;

    public GuiCreativeMorphsMenu morphs;

    public static GuiMappetDashboard get(Minecraft mc)
    {
        if (dashboard == null)
        {
            dashboard = new GuiMappetDashboard(mc);
        }

        return dashboard;
    }

    public GuiMappetDashboard(Minecraft mc)
    {
        super(mc);
    }

    @Override
    protected GuiDashboardPanels createDashboardPanels(Minecraft mc)
    {
        return new GuiDashboardPanels(mc);
    }

    public GuiCreativeMorphsMenu getMorphMenu()
    {
        if (this.morphs == null)
        {
            this.morphs = new GuiCreativeMorphsMenu(Minecraft.getMinecraft(), null).pickUponExit();
        }

        return this.morphs;
    }

    public void openMorphMenu(GuiElement parent, boolean editing, AbstractMorph morph, Consumer<AbstractMorph> callback)
    {
        GuiBase.getCurrent().unfocus();

        GuiCreativeMorphsMenu menu = this.getMorphMenu();

        menu.callback = callback;
        menu.flex().reset().relative(parent).wh(1F, 1F);
        menu.resize();
        menu.setSelected(morph);

        if (editing)
        {
            menu.enterEditMorph();
        }

        menu.removeFromParent();
        parent.add(menu);
    }

    @Override
    protected void registerPanels(Minecraft mc)
    {
        this.settings = new GuiServerSettingsPanel(mc, this);
        this.crafting = new GuiCraftingTablePanel(mc, this);
        this.quest = new GuiQuestPanel(mc, this);
        this.event = new GuiEventPanel(mc, this);
        this.dialogue = new GuiDialoguePanel(mc, this);
        this.region = new GuiRegionPanel(mc, this);
        this.conditionModel = new GuiConditionModelPanel(mc, this);
        this.npc = new GuiNpcPanel(mc, this);
        this.faction = new GuiFactionPanel(mc, this);
        this.chain = new GuiQuestChainPanel(mc, this);
        this.script = new GuiScriptPanel(mc, this);
        this.hud = new GuiHUDScenePanel(mc, this);
        this.logs = new GuiLogPanel(mc, this);

        this.panels.registerPanel(this.settings, IKey.lang("mappet.gui.panels.settings"), Icons.GEAR);
        this.panels.registerPanel(this.crafting, IKey.lang("mappet.gui.panels.crafting"), Icons.WRENCH);
        this.panels.registerPanel(this.quest, IKey.lang("mappet.gui.panels.quests"), Icons.EXCLAMATION);
        this.panels.registerPanel(this.event, IKey.lang("mappet.gui.panels.events"), Icons.FILE);
        this.panels.registerPanel(this.dialogue, IKey.lang("mappet.gui.panels.dialogues"), Icons.BUBBLE);
        this.panels.registerPanel(this.region, IKey.lang("mappet.gui.panels.regions"), Icons.FULLSCREEN);
        this.panels.registerPanel(this.conditionModel, IKey.lang("mappet.gui.panels.condition_models"), Icons.BLOCK);
        this.panels.registerPanel(this.npc, IKey.lang("mappet.gui.panels.npcs"), Icons.PROCESSOR);
        this.panels.registerPanel(this.faction, IKey.lang("mappet.gui.panels.factions"), Icons.BOOKMARK);
        this.panels.registerPanel(this.chain, IKey.lang("mappet.gui.panels.chains"), Icons.FOLDER);
        this.panels.registerPanel(this.script, IKey.lang("mappet.gui.panels.scripts"), MMIcons.PROPERTIES);
        this.panels.registerPanel(this.hud, IKey.lang("mappet.gui.panels.huds"), Icons.POSE);
        this.panels.registerPanel(this.logs, IKey.lang("mappet.gui.panels.logs"), MPIcons.REPL);

        this.panels.setPanel(this.settings);
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketContentExit());
        RenderingHandler.currentStage = null;
    }
}