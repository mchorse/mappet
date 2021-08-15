package mchorse.mappet.commands;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.commands.crafting.CommandCrafting;
import mchorse.mappet.commands.data.CommandData;
import mchorse.mappet.commands.dialogues.CommandDialogue;
import mchorse.mappet.commands.events.CommandEvent;
import mchorse.mappet.commands.factions.CommandFaction;
import mchorse.mappet.commands.huds.CommandHud;
import mchorse.mappet.commands.npc.CommandNpc;
import mchorse.mappet.commands.quests.CommandQuest;
import mchorse.mappet.commands.scripts.CommandScript;
import mchorse.mappet.commands.states.CommandState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.stream.Collectors;

public class CommandMappet extends MappetSubCommandBase
{
    public static DataContext createContext(MinecraftServer server, ICommandSender sender, String argument) throws CommandException
    {
        if (argument.equals("~"))
        {
            return new DataContext(server);
        }

        return new DataContext(getPlayer(server, sender, argument));
    }

    public static List<String> listOfPlayersAndServer(MinecraftServer server)
    {
        List<String> list = listOfPlayers(server);

        list.add("~");

        return list;
    }

    public static List<String> listOfPlayers(MinecraftServer server)
    {
        return server.getPlayerList().getPlayers().stream().map(EntityPlayer::getName).collect(Collectors.toList());
    }

    public CommandMappet()
    {
        this.add(new CommandCrafting());
        this.add(new CommandData());
        this.add(new CommandDialogue());
        this.add(new CommandEvent());
        this.add(new CommandFaction());
        this.add(new CommandHud());
        this.add(new CommandNpc());
        this.add(new CommandQuest());
        this.add(new CommandScript());
        this.add(new CommandState());
    }

    @Override
    public String getName()
    {
        return "mp";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
}
