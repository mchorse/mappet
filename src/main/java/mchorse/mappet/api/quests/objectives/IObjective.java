package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.api.quests.INBTPartialSerializable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IObjective extends INBTSerializable<NBTTagCompound>, INBTPartialSerializable
{
    public static IObjective fromType(String type)
    {
        if (type.equals("collect"))
        {
            return new CollectObjective();
        }
        else if (type.equals("kill"))
        {
            return new KillObjective();
        }

        return null;
    }

    public boolean isComplete(EntityPlayer player);

    public void complete(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public String stringify(EntityPlayer player);

    public String getType();
}