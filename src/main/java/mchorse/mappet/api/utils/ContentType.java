package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
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
        public IManager<? extends INBTSerializable<NBTTagCompound>> getManager()
        {
            return Mappet.factions;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.faction;
        }
    };

    /* Every Karen be like :D */
    public abstract IManager<? extends INBTSerializable<NBTTagCompound>> getManager();

    @SideOnly(Side.CLIENT)
    public abstract GuiMappetDashboardPanel get(GuiMappetDashboard dashboard);
}
