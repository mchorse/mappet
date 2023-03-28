package mchorse.mappet.commands.huds;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandHudClose extends CommandHudBase
{
    @Override
    public String getName()
    {
        return "close";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.hud.close";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}hud close{r} {7}<target> [id]{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
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
        ICharacter character = Character.get(player);

        if (character == null)
        {
            return;
        }

        if (args.length > 1)
        {
            character.closeHUD(args[1]);
        }
        else
        {
            character.closeAllHUD();
        }
    }
}