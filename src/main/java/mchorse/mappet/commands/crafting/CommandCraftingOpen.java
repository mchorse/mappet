package mchorse.mappet.commands.crafting;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.commands.CommandMappet;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCraftingOpen extends CommandCraftingBase
{
    @Override
    public String getName()
    {
        return "open";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.crafting.open";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}crafting open{r} {7}<player> <id>{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        CraftingTable table = this.getCraftingTable(args[1]);

        table.filter(player);

        if (table.recipes.isEmpty())
        {
            throw new CommandException("crafting.empty", args[1]);
        }

        Mappet.crafting.open(player, table);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, CommandMappet.listOfPlayers(server));
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
