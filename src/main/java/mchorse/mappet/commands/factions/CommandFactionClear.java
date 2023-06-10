package mchorse.mappet.commands.factions;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandFactionClear extends CommandFactionBase
{
    @Override
    public String getName()
    {
        return "clear";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.faction.clear";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}faction clear{r} {7}<target> [id]{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 1)
        {
            String id = args[1];

            /* Check that the faction exists */
            this.getFaction(id);

            CommandFaction.getStates(server, sender, args[0]).clearFactionScore(id);
            this.getL10n().info(sender, "factions.clear", id);
        }
        else
        {
            CommandFaction.getStates(server, sender, args[0]).clearAllFactionScores();
            this.getL10n().info(sender, "factions.clear_all", args[0]);
        }
    }
}