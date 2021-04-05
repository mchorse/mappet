package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandStateBase extends MappetCommandBase
{
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.states.values.keySet());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}