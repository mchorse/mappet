package mchorse.mappet.commands.npc;

import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcLexer;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
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
        return 2;
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
        NpcLexer lexer = NpcLexer.parse(args[1], entity.getId());
        Npc npc = this.getNpc(lexer.id);
        NpcState state = npc.states.get(lexer.state);

        if (state == null)
        {
            throw new CommandException("npc.missing_state", args[1]);
        }

        if (!lexer.properties.isEmpty())
        {
            NBTTagCompound tag = state.partialSerializeNBT(lexer.properties);
            NBTTagCompound original = entity.getState().serializeNBT();

            for (String key : tag.getKeySet())
            {
                original.setTag(key, tag.getTag(key));
            }

            state = new NpcState();
            state.deserializeNBT(original);
        }

        state.id = entity.getId();
        entity.setState(state, true);
    }
}