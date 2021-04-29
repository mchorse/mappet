package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.data.Data;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.commands.MappetCommandBase;
import mchorse.mclib.commands.McCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommandDataBase extends MappetCommandBase
{
    protected Data getData(String id) throws CommandException
    {
        Data data = Mappet.data.load(id);

        if (data == null)
        {
            throw new CommandException("data.missing", id);
        }

        return data;
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.data.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}