package mchorse.mappet.commands.npc;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandNpcState extends CommandNpcBase
{
    @Override
    public String getName()
    {
        return "state";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.npc.state";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}npc state{r} {7}<target> <state>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityNpc entity = getEntity(server, sender, args[0], EntityNpc.class);

        String id = args[1];
        Npc npc = this.getNpc(entity.getId());
        NpcState state = npc.states.get(id);

        if (state == null)
        {
            throw new CommandException("npc.missing_state", args[1]);
        }

        entity.setState(state);
    }
}