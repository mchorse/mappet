package mchorse.mappet.api.quests.rewards;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public interface IReward extends INBTSerializable<NBTTagCompound>, IMessage
{
    public static IReward fromType(String type)
    {
        if (type.equals("item"))
        {
            return new ItemStackReward();
        }

        return null;
    }

    public void reward(EntityPlayer player);

    public IReward copy();

    public String getType();
}