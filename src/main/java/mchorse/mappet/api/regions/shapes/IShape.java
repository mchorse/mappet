package mchorse.mappet.api.regions.shapes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IShape extends INBTSerializable<NBTTagCompound>
{
    public static IShape fromString(String string)
    {
        if (string.equals("box"))
        {
            return new BoxShape();
        }
        else if (string.equals("sphere"))
        {
            return new SphereShape();
        }
        else if (string.equals("cylinder"))
        {
            return new CylinderShape();
        }

        return null;
    }

    public boolean isPlayerInside(EntityPlayer player, BlockPos tile);

    public String getType();
}