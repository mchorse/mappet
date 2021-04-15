package mchorse.mappet.client.gui;

import mchorse.mappet.client.gui.panels.GuiCraftingTablePanel;
import mchorse.mappet.client.gui.panels.GuiDialoguePanel;
import mchorse.mappet.client.gui.panels.GuiEventPanel;
import mchorse.mappet.client.gui.panels.GuiQuestPanel;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanels;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiMappetDashboard extends GuiAbstractDashboard
{
    public static GuiMappetDashboard dashboard;

    public GuiCraftingTablePanel crafting;
    public GuiQuestPanel quest;
    public GuiEventPanel event;
    public GuiDialoguePanel dialogue;

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

    @Override
    protected void registerPanels(Minecraft mc)
    {
        this.crafting = new GuiCraftingTablePanel(mc, this);
        this.quest = new GuiQuestPanel(mc, this);
        this.event = new GuiEventPanel(mc, this);
        this.dialogue = new GuiDialoguePanel(mc, this);

        this.panels.registerPanel(this.crafting, IKey.lang("mappet.gui.panels.crafting"), Icons.WRENCH);
        this.panels.registerPanel(this.quest, IKey.lang("mappet.gui.panels.quests"), Icons.EXCLAMATION);
        this.panels.registerPanel(this.event, IKey.lang("mappet.gui.panels.events"), Icons.FILE);
        this.panels.registerPanel(this.dialogue, IKey.lang("mappet.gui.panels.dialogues"), Icons.BUBBLE);

        this.panels.setPanel(this.crafting);
    }
}