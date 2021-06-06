package mchorse.mappet.commands.factions;

import mchorse.mappet.api.states.States;
import mchorse.mappet.commands.MappetSubCommandBase;
import mchorse.mappet.utils.EntityUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

public class CommandFaction extends MappetSubCommandBase
{
    public static States getStates(MinecraftServer server, ICommandSender sender, String target) throws CommandException
    {
        Entity entity = getEntity(server, sender, target);
        States states = EntityUtils.getStates(entity);

        if (states != null)
        {
            return states;
        }

        throw new CommandException("states.invalid_target", target);
    }

    public CommandFaction()
    {
        this.add(new CommandFactionAdd());
        this.add(new CommandFactionClear());
        this.add(new CommandFactionSet());
    }

    @Override
    public String getName()
    {
        return "faction";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.faction.help";
    }
}