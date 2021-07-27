package mchorse.mappet.commands.huds;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandHud extends MappetSubCommandBase
{
    public CommandHud()
    {
        this.add(new CommandHudClose());
        this.add(new CommandHudMorph());
        this.add(new CommandHudSetup());
    }

    @Override
    public String getName()
    {
        return "hud";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.hud.help";
    }
}