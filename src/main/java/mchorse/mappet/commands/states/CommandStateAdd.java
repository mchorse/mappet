package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStateAdd extends CommandStateBase
{
    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.add";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state add{r} {7}<id> <expression>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[0];
        double value = CommandBase.parseDouble(args[1]);
        double previous = Mappet.states.get(id);

        Mappet.states.add(id, value);

        this.getL10n().info(sender, "states.changed", id, previous, Mappet.states.get(id));
    }
}