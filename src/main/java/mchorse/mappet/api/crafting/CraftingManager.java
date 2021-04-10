package mchorse.mappet.api.crafting;

import mchorse.mappet.api.utils.BaseManager;

import java.io.File;

public class CraftingManager extends BaseManager<CraftingTable>
{
    public CraftingManager(File folder)
    {
        super(folder);
    }

    @Override
    public CraftingTable create()
    {
        return new CraftingTable();
    }
}