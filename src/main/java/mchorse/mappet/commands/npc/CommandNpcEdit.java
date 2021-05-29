package mchorse.mappet.commands.npc;

import mchorse.mappet.api.npcs.NpcLexer;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mclib.commands.McCommandBase;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandNpcEdit extends CommandNpcBase
{
    @Override
    public String getName()
    {
        return "edit";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.npc.edit";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}npc edit{r} {7}<target> <property> <value>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 3;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityNpc entity = getEntity(server, sender, args[0], EntityNpc.class);
        String property = args[1];

        if (!NpcLexer.PROPERTIES.contains(property))
        {
            throw new CommandException("npc.invalid_property", property);
        }

        NpcState state = entity.getState();
        String value = String.join(" ", SubCommandBase.dropFirstArguments(args, 2));

        try
        {
            if (state.edit(property, value.trim()))
            {
                entity.setState(state, !property.equals(NpcLexer.PROPERTIES.get(0)), true);
            }
            else
            {
                throw new CommandException("npc.cant_edit", property, value);
            }
        }
        catch (Exception e)
        {
            throw new CommandException("npc.cant_edit", property, value);
        }
    }
}