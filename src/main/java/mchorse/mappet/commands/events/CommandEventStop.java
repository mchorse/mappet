package mchorse.mappet.commands.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandEventStop extends CommandEventBase
{
    @Override
    public String getName()
    {
        return "stop";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.event.stop";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}event stop{r} {7}<id>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        int removed = CommonProxy.eventHandler.removeExecutables(args[0]);

        Mappet.l10n.success(sender, "events.stopped", removed, args[0]);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, CommonProxy.eventHandler.getIds());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
