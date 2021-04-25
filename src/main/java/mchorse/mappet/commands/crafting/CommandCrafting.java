package mchorse.mappet.commands.crafting;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandCrafting extends MappetSubCommandBase
{
    public CommandCrafting()
    {
        this.add(new CommandCraftingOpen());
    }

    @Override
    public String getName()
    {
        return "crafting";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.crafting.help";
    }
}