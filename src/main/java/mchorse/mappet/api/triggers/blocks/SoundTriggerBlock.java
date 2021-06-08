package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayerMP;

public class SoundTriggerBlock extends StringTriggerBlock
{
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
        if (!this.string.isEmpty())
        {
            for (EntityPlayerMP player : context.server.getPlayerList().getPlayers())
            {
                WorldUtils.playSound(player, this.string);
            }
        }
    }

    @Override
    protected String getKey()
    {
        return "Sound";
    }
}