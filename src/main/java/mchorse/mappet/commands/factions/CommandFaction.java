package mchorse.mappet.commands.factions;

import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandFaction extends MappetSubCommandBase
{
    public static States getStates(MinecraftServer server, ICommandSender sender, String target) throws CommandException
    {
        EntityPlayer player = getPlayer(server, sender, target);
        ICharacter character = Character.get(player);

        if (character != null)
        {
            return character.getStates();
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