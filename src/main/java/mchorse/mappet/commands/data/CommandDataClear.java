package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.MappetCommandBase;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.quests.PacketQuests;
import mchorse.mclib.commands.McCommandBase;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.List;

public class CommandDataClear extends MappetCommandBase
{
    public static void clear(EntityPlayerMP player, boolean inventory)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.getStates().clear();
            character.getQuests().quests.clear();

            Dispatcher.sendTo(new PacketQuests(character.getQuests()), player);
        }

        if (inventory)
        {
            player.inventory.clear();
        }
    }

    @Override
    public String getName()
    {
        return "clear";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.data.clear";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}data clear{r} {7}[inventory]{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        boolean inventory = args.length != 0 && CommandBase.parseBoolean(args[0]);

        Mappet.states.clear();
        Mappet.data.updateLastClear(inventory);

        for (EntityPlayerMP player : server.getPlayerList().getPlayers())
        {
            clear(player, inventory);

            ICharacter character = Character.get(player);

            if (character != null)
            {
                character.updateLastClear(Instant.now());
            }
        }

        this.getL10n().info(sender, inventory ? "data.cleared_inventory" : "data.cleared");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, McCommandBase.BOOLEANS);
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
