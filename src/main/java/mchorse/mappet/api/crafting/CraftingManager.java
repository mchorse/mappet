package mchorse.mappet.api.crafting;

import mchorse.mappet.api.utils.BaseManager;
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
}