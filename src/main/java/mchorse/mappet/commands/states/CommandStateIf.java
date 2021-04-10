package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Operation;
import mchorse.mclib.math.Variable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStateIf extends CommandStateBase
{
    @Override
    public String getName()
    {
        return "if";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.if";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state if{r} {7}<id> <expression>{r}";
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

        if (!Mappet.states.values.containsKey(id))
        {
            throw new CommandException("states.missing", id);
        }

        /* TODO: switch to global expressions */
        double previous = Mappet.states.get(id);
        double value = 0;
        MathBuilder builder = new MathBuilder();

        builder.register(new Variable("value", previous));

        try
        {
            value = builder.parse(String.join(" ", SubCommandBase.dropFirstArgument(args))).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (Operation.equals(value, 0))
        {
            throw new CommandException("states.false", id);
        }

        this.getL10n().info(sender, "states.true", id);
    }
}