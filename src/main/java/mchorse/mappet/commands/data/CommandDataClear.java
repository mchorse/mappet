package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.MappetCommandBase;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.quests.PacketQuests;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.time.Instant;

public class CommandDataClear extends MappetCommandBase
{
    public static void clear(EntityPlayerMP player)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.getStates().values.clear();
            character.getQuests().quests.clear();

            Dispatcher.sendTo(new PacketQuests(character.getQuests()), player);
        }

        player.inventory.clear();
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
        return "{l}{6}/{r}mp {8}data clear{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Mappet.states.values.clear();
        Mappet.data.updateLastClear();

        for (EntityPlayerMP player : server.getPlayerList().getPlayers())
        {
            clear(player);

            ICharacter character = Character.get(player);

            if (character != null)
            {
                character.updateLastClear(Instant.now());
            }
        }


        this.getL10n().info(sender, "data.cleared");
    }
}
