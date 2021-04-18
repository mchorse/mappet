package mchorse.mappet.commands.npc;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandNpcBase extends MappetCommandBase
{
    protected Npc getNpc(String id) throws CommandException
    {
        Npc npc = Mappet.npcs.load(id);

        if (npc == null)
        {
            throw new CommandException("npc.missing", id);
        }

        return npc;
    }
}