package mchorse.mappet.commands.scripts;

import com.caoccao.javet.exceptions.BaseJavetScriptingException;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.exceptions.JavetScriptingError;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.script.ScriptException;

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
        return "{l}{6}/{r}mp {8}script eval{r} {7}<code...>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Object key = sender instanceof EntityPlayerMP ? sender : server;
        String code = String.join(" ", args);

        try
        {
            Mappet.scripts.executeRepl(key, code);
        }
        catch (BaseJavetScriptingException e)
        {
            JavetScriptingError scriptingError = e.getScriptingError();
            throw new CommandException("script.error", args[1], scriptingError.getStartColumn(), scriptingError.getLineNumber(), e.getMessage());
        }
        catch (Exception e)
        {
            throw new CommandException("script.empty", args[1], e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}