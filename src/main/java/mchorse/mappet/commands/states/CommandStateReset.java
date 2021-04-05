package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStateReset extends CommandStateBase
{
    @Override
    public String getName()
    {
        return "reset";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.reset";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state reset{r} {7}<id>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[0];

        Mappet.states.reset(id);

        this.getL10n().info(sender, "states.reset", id);
    }
}