package mchorse.mappet.commands.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandEventTrigger extends CommandEventBase
{
    @Override
    public String getName()
    {
        return "trigger";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.event.trigger";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}event trigger{r} {7}<target> <id> [data]{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        NodeSystem<EventBaseNode> event = this.getEvent(args[1]);

        if (event.main == null)
        {
            throw new CommandException("event.empty", args[1]);
        }

        DataContext context = CommandMappet.createContext(server, sender, args[0]);

        if (args.length > 2)
        {
            context.parse(String.join(" ", SubCommandBase.dropFirstArguments(args, 2)));
        }

        Mappet.events.execute(event, new EventContext(context));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, CommandMappet.listOfPlayersAndServer(server));
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
