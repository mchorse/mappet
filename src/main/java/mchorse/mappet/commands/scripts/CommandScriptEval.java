package mchorse.mappet.commands.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandScriptEval extends CommandScriptBase
{
    @Override
    public String getName()
    {
        return "eval";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.script.eval";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}script eval{r} {7}<player> <id> [data]{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        DataContext context = new DataContext(player);

        if (args.length > 2)
        {
            context.parse(String.join(" ", SubCommandBase.dropFirstArguments(args, 2)));
        }

        if (!Mappet.scripts.execute(args[1], context))
        {
            throw new CommandException("script.empty", args[1]);
        }
    }
}