package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class SoundTriggerBlock extends StringTriggerBlock
{
    public TargetMode target = TargetMode.GLOBAL;

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

        if (this.target == TargetMode.GLOBAL)
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
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Target", this.target.ordinal());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.target = EnumUtils.getValue(tag.getInteger("Target"), TargetMode.values(), TargetMode.GLOBAL);
    }
}