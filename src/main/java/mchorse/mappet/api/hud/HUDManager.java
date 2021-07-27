package mchorse.mappet.api.hud;

import mchorse.mappet.api.utils.manager.BaseManager;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;

public class HUDManager extends BaseManager<HUDScene>
{
    public HUDManager(File folder)
    {
        super(folder);
    }

    @Override
    protected HUDScene createData(String id, NBTTagCompound tag)
    {
        HUDScene scene = new HUDScene();

        if (tag != null)
        {
            scene.deserializeNBT(tag);
        }

        return scene;
    }
}