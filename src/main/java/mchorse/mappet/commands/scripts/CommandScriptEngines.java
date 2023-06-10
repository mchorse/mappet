package mchorse.mappet.commands.scripts;

import mchorse.mappet.utils.ScriptUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.ArrayList;
import java.util.List;

public class CommandScriptEngines extends CommandScriptBase
{
    @Override
    public String getName()
    {
        return "engines";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.script.engines";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}script engines";
    }

    @Override
    public int getRequiredArgs()
    {
        return 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        List<ScriptEngine> engines = ScriptUtils.getAllEngines();

        List<String> strings = new ArrayList<>();

        for (ScriptEngine engine : engines)
        {
            ScriptEngineFactory factory = engine.getFactory();
            strings.add(factory.getEngineName() + " (" + factory.getLanguageName() + ")");
        }

        this.getL10n().info(sender, "scripts.engines", String.join(", ", strings));
    }
}