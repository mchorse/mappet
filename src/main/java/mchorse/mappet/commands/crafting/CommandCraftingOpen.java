package mchorse.mappet.commands.crafting;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.PacketCraftingTable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        CraftingTable table = this.getCraftingTable(args[1]);

        if (table.recipes.isEmpty())
        {
            throw new CommandException("crafting.empty", args[1]);
        }

        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.setCraftingTable(table);
            Dispatcher.sendTo(new PacketCraftingTable(table), player);
        }
    }
}
