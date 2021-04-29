package mchorse.mappet.commands.data;

import mchorse.mappet.api.data.Data;
import mchorse.mclib.commands.McCommandBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDataLoad extends CommandDataBase
{
    @Override
    public String getName()
    {
        return "load";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.data.load";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}data load{r} {7}<id> [global] [player]{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        boolean global = args.length <= 1 || CommandBase.parseBoolean(args[1]);

        String id = args[0];
        Data data = this.getData(id);
        EntityPlayer player = args.length > 2 ? getPlayer(server, sender, args[2]) : getCommandSenderAsPlayer(sender);

        data.apply(player, global);

        this.getL10n().success(sender, "data.loaded", id);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, McCommandBase.BOOLEANS);
        }

        if (args.length == 3)
        {
            List<String> list = server.getPlayerList().getPlayers().stream().map(EntityPlayer::getName).collect(Collectors.toList());

            return getListOfStringsMatchingLastWord(args, list);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}