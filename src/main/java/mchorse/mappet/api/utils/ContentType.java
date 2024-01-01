package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.manager.IManager;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Mappet content types
 */
public enum ContentType implements IContentType
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.quest");
        }

        @Override
        public String getName() {
            return "QUEST";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.crafting");
        }

        @Override
        public String getName() {
            return "CRAFTING_TABLE";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.event");
        }

        @Override
        public String getName() {
            return "EVENT";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.dialogue");
        }

        @Override
        public String getName() {
            return "DIALOGUE";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.npc");
        }

        @Override
        public String getName() {
            return "NPC";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.faction");
        }

        @Override
        public String getName() {
            return "FACTION";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.chain");
        }

        @Override
        public String getName() {
            return "CHAINS";
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

        @Override
        @SideOnly(Side.CLIENT)
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.script");
        }

        @Override
        public String getName() {
            return "SCRIPTS";
        }
    },
    HUDS()
    {
        @Override
        public IManager<? extends AbstractData> getManager()
        {
            return Mappet.huds;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.hud;
        }

        @Override
        public IKey getPickLabel()
        {
            return IKey.lang("mappet.gui.overlays.hud");
        }

        @Override
        public String getName() {
            return "HUDS";
        }
    };
}
