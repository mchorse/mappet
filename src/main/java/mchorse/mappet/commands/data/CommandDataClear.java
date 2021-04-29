package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandDataClear extends MappetCommandBase
{
    public static void clear(EntityPlayer player)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.getStates().values.clear();
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

        for (EntityPlayerMP player : server.getPlayerList().getPlayers())
        {
            clear(player);
        }

        Mappet.data.updateLastClear();

        this.getL10n().info(sender, "data.cleared");
    }
}
