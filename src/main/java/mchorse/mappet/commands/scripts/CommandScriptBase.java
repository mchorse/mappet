package mchorse.mappet.commands.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandScriptBase extends MappetCommandBase
{
    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.scripts.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}