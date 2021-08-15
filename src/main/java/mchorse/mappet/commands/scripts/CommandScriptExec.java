package mchorse.mappet.commands.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import javax.script.ScriptException;
import java.util.List;

public class CommandScriptExec extends CommandScriptBase
{
    @Override
    public String getName()
    {
        return "exec";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.script.exec";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}script exec{r} {7}<target> <id> [function] [data]{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        DataContext context = CommandMappet.createContext(server, sender, args[0]);
        String function = args.length > 2 ? args[2] : "main";

        if (args.length > 3)
        {
            context.parse(String.join(" ", SubCommandBase.dropFirstArguments(args, 3)));
        }

        try
        {
            Mappet.scripts.execute(args[1], function, context);
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
            throw new CommandException("script.error", e.getFileName(), e.getLineNumber(), e.getColumnNumber(), e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new CommandException("script.empty", args[1], e.getClass().getSimpleName() + ": " + e.getMessage());
        }
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