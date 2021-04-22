package mchorse.mappet.commands.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
        return "{l}{6}/{r}mp {8}event trigger{r} {7}<player> <id>{r}";
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
        NodeSystem<EventNode> event = this.getEvent(args[1]);

        if (event.main == null)
        {
            throw new CommandException("event.empty", args[1]);
        }

        Mappet.events.execute(event, new EventContext(server, player));
    }
}
