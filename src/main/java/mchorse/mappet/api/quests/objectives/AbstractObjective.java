package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.api.quests.INBTPartialSerializable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractObjective implements INBTSerializable<NBTTagCompound>, INBTPartialSerializable
{
    public String message = "";

    public static AbstractObjective fromType(String type)
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

    public abstract boolean isComplete(EntityPlayer player);

    public abstract void complete(EntityPlayer player);

    @SideOnly(Side.CLIENT)
    public abstract String stringify(EntityPlayer player);

    public abstract String getType();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Message", this.message);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Message"))
        {
            this.message = tag.getString("Message");
        }
    }
}