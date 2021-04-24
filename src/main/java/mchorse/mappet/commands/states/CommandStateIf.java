package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mclib.commands.SubCommandBase;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.Operation;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
        return "{l}{6}/{r}mp {8}state if{r} {7}<target> <id> <expression>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        States states = this.getStates(server, sender, args[0]);
        String id = args[1];

        if (!states.values.containsKey(id))
        {
            throw new CommandException("states.missing", id);
        }

        double previous = states.get(id);
        double value = 0;

        EntityLivingBase subject = sender instanceof EntityPlayer ? (EntityPlayer) sender : null;
        String expression = String.join(" ", SubCommandBase.dropFirstArguments(args, 2));
        IValue result = Mappet.expressions.evalute(expression, subject, previous);

        if (result != null && result.isNumber())
        {
            value = result.doubleValue();
        }

        if (Operation.equals(value, 0))
        {
            throw new CommandException("states.false", id);
        }

        this.getL10n().info(sender, "states.true", id);
    }
}