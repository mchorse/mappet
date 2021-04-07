package mchorse.mappet.capabilities;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.quests.Quests;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICharacter extends INBTSerializable<NBTTagCompound>
{
    public Quests getQuests();

    public void setCraftingTable(CraftingTable table);

    public CraftingTable getCraftingTable();
}