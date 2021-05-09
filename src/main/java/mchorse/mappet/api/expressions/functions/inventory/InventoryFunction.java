package mchorse.mappet.api.expressions.functions.inventory;

import com.google.common.collect.ImmutableList;
import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public abstract class InventoryFunction extends SNFunction
{
    public InventoryFunction(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected void verifyArgument(int index, IValue value)
    {
        /* Don't throw an exception for third argument */
        if (index != 2)
        {
            super.verifyArgument(index, value);
        }
    }

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public double doubleValue()
    {
        String id = this.getArg(0).stringValue();
        String target = this.args.length > 1 ? this.getArg(1).stringValue() : null;
        boolean all = this.args.length > 2 && this.getArg(2).booleanValue();
        List<EntityPlayerMP> players = null;

        try
        {
            if (target != null)
            {
                MinecraftServer server = Mappet.expressions.getServer();

                players = CommandBase.getPlayers(server, server, target);
            }
        }
        catch (Exception e)
        {}

        if (players == null && Mappet.expressions.context.subject instanceof EntityPlayerMP)
        {
            players = ImmutableList.of((EntityPlayerMP) Mappet.expressions.context.subject);
        }

        if (players != null)
        {
            int i = 0;

            for (EntityPlayerMP player : players)
            {
                boolean has = this.isTrue(id, player);

                if (!has)
                {
                    continue;
                }

                if (all)
                {
                    i += 1;
                }
                else
                {
                    return 1;
                }
            }

            return i == players.size() ? 1 : 0;
        }

        return 0;
    }

    protected abstract boolean isTrue(String id, EntityPlayer player);
}