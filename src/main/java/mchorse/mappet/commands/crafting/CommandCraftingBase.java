package mchorse.mappet.commands.crafting;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandCraftingBase extends MappetCommandBase
{
    protected CraftingTable getCraftingTable(String id) throws CommandException
    {
        CraftingTable quest = Mappet.crafting.load(id);

        if (quest == null)
        {
            throw new CommandException("crafting.missing", id);
        }

        return quest;
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.crafting.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}