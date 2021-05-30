package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.manager.IManager;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Mappet content types
 */
public enum ContentType
{
    QUEST()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.quests;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.quest;
        }
    },
    CRAFTING_TABLE()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.crafting;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.crafting;
        }
    },
    EVENT()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.events;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.event;
        }
    },
    DIALOGUE()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.dialogues;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.dialogue;
        }
    },
    NPC()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.npcs;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.npc;
        }
    },
    FACTION()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.factions;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.faction;
        }
    },
    CHAINS()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.chains;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.chain;
        }
    },
    SCRIPTS()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.scripts;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.script;
        }
    };

    /* Every Karen be like :D */
    public abstract IManager<? extends AbstractData> getManager();

    @SideOnly(Side.CLIENT)
    public abstract GuiMappetDashboardPanel get(GuiMappetDashboard dashboard);
}
