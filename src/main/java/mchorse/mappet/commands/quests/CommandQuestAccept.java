package mchorse.mappet.commands.quests;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandQuestAccept extends CommandQuestBase
{
    @Override
    public String getName()
    {
        return "accept";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.quest.accept";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}quest accept{r} {7}<player> <id>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayer player = getPlayer(server, sender, args[0]);
        String id = args[1];
        Quest quest = this.getQuest(id);
        ICharacter character = Character.get(player);

        if (character != null && character.getQuests().add(quest, player))
        {
            this.getL10n().success(sender, "quest.accepted", id, player.getName());
        }
    }
}
