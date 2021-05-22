package mchorse.mappet.commands.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TriggerSender;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
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
        return "{l}{6}/{r}mp {8}event trigger{r} {7}<player> <id> [data]{r}";
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

        DataContext context = new DataContext(player);

        if (args.length > 2)
        {
            context.parse(String.join(" ", SubCommandBase.dropFirstArguments(args, 2)));
        }

        Mappet.events.execute(event, new EventContext(context));
    }
}
