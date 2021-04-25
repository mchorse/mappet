package mchorse.mappet.commands.states;

import net.minecraft.command.ICommandSender;

public class CommandStateSub extends CommandStateAdd
{
    @Override
    public String getName()
    {
        return "sub";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.sub";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state sub{r} {7}<target> <id> <expression>{r}";
    }

    @Override
    protected double processValue(double value)
    {
        return -value;
    }
}