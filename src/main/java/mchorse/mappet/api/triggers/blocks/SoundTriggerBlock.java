package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class SoundTriggerBlock extends StringTriggerBlock
{
    public Target target = Target.GLOBAL;

    public SoundTriggerBlock()
    {
        super();
    }

    public SoundTriggerBlock(String string)
    {
        super(string);
    }

    @Override
    public void trigger(DataContext context)
    {
        if (this.string.isEmpty())
        {
            return;
        }

        if (this.target == Target.GLOBAL)
        {
            for (EntityPlayerMP player : context.server.getPlayerList().getPlayers())
            {
                WorldUtils.playSound(player, this.string);
            }
        }
        else
        {
            EntityPlayer player = context.getPlayer();

            if (player instanceof EntityPlayerMP)
            {
                WorldUtils.playSound((EntityPlayerMP) player, this.string);
            }
        }
    }

    @Override
    protected String getKey()
    {
        return "Sound";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setInteger("Target", this.target.ordinal());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.target = EnumUtils.getValue(tag.getInteger("Target"), Target.values(), Target.GLOBAL);
    }
}