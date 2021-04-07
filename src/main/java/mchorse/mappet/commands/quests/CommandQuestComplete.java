package mchorse.mappet.commands.quests;

import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.ICharacter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandQuestComplete extends CommandQuestBase
{
    @Override
    public String getName()
    {
        return "complete";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.quest.complete";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}quest complete{r} {7}<player> <id>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = getPlayer(server, sender, args[0]);
        ICharacter character = Character.get(player);

        if (character != null && character.getQuests().complete(args[1], player))
        {
            this.getL10n().success(sender, "quest.completed", args[1], player.getName());
        }
    }
}
