package mchorse.mappet.commands.npc;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.List;

public class CommandNpcSummon extends CommandNpcBase
{
    @Override
    public String getName()
    {
        return "summon";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.npc.summon";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}npc summon{r} {7}<id> [state] [x] [y] [z]{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[0];
        Npc npc = this.getNpc(id);

        if (npc.states.isEmpty())
        {
            throw new CommandException("npc.empty", id);
        }

        NpcState state = npc.states.get(args.length >= 2 ? args[1] : "default");

        if (state == null)
        {
            throw new CommandException("npc.missing_state", id, args[1]);
        }

        Vec3d position = sender.getPositionVector();
        double x = position.x;
        double y = position.y;
        double z = position.z;

        if (args.length >= 5)
        {
            x = CommandBase.parseDouble(x, args[2], true);
            y = CommandBase.parseDouble(y, args[3], false);
            z = CommandBase.parseDouble(z, args[4], true);
        }

        EntityNpc entity = new EntityNpc(sender.getEntityWorld());

        entity.setPosition(x, y, z);
        entity.setNpc(npc, state);

        entity.world.spawnEntity(entity);
        entity.initialize();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.npcs.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}