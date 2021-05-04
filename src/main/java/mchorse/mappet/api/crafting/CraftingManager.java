package mchorse.mappet.api.crafting;

import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class CraftingManager extends BaseManager<CraftingTable>
{
    public CraftingManager(File folder)
    {
        super(folder);
    }

    @Override
    protected CraftingTable createData(NBTTagCompound tag)
    {
        CraftingTable table = new CraftingTable();

        if (tag != null)
        {
            table.deserializeNBT(tag);
        }

        return table;
    }

    public void open(EntityPlayerMP player, CraftingTable table)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.setCraftingTable(table);
            Dispatcher.sendTo(new PacketCraftingTable(table), player);
        }
    }
}