package mchorse.mappet.commands.factions;

import mchorse.mappet.Mappet;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandFactionBase extends MappetCommandBase
{
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

            return getListOfStringsMatchingLastWord(args, list);
        }

        if (args.length == 2)
        {
            try
            {
                return getListOfStringsMatchingLastWord(args, Mappet.factions.getKeys());
            }
            catch (Exception e)
            {}
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}