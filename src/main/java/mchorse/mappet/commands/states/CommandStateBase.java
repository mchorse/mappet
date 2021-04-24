package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandStateBase extends MappetCommandBase
{
    public States getStates(MinecraftServer server, ICommandSender sender, String target) throws CommandException
    {
        if (target.equals("~"))
        {
            return Mappet.states;
        }

        EntityPlayer player = getPlayer(server, sender, target);
        ICharacter character = Character.get(player);

        if (character != null)
        {
            return character.getStates();
        }

        throw new CommandException("states.invalid_target", target);
    }

    @Override
    public int getRequiredArgs()
    {
        return 3;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            List<String> list = server.getPlayerList().getPlayers().stream().map(EntityPlayer::getName).collect(Collectors.toList());

            list.add("~");

            return getListOfStringsMatchingLastWord(args, list);
        }

        if (args.length == 2)
        {
            try
            {
                States states = this.getStates(server, sender, args[0]);

                return getListOfStringsMatchingLastWord(args, states.values.keySet());
            }
            catch (Exception e)
            {}
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}