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
        public INBTSerializable<NBTTagCompound> deserialize(NBTTagCompound data)
        {
            Quest quest = new Quest();

            quest.deserializeNBT(data);

            return quest;
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
        public INBTSerializable<NBTTagCompound> deserialize(NBTTagCompound data)
        {
            CraftingTable table = new CraftingTable();

            table.deserializeNBT(data);

            return table;
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
        public INBTSerializable<NBTTagCompound> deserialize(NBTTagCompound data)
        {
            NodeSystem<EventNode> system = Mappet.events.create();

            system.deserializeNBT(data);

            return system;
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
        public INBTSerializable<NBTTagCompound> deserialize(NBTTagCompound data)
        {
            DialogueNodeSystem dialogue = Mappet.dialogues.create();

            dialogue.deserializeNBT(data);

            return dialogue;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public GuiMappetDashboardPanel get(GuiMappetDashboard dashboard)
        {
            return dashboard.dialogue;
        }
    };

    /* Every Karen be like :D */
    public abstract IManager<? extends INBTSerializable<NBTTagCompound>> getManager();

    public abstract INBTSerializable<NBTTagCompound> deserialize(NBTTagCompound data);

    @SideOnly(Side.CLIENT)
    public abstract GuiMappetDashboardPanel get(GuiMappetDashboard dashboard);
}
