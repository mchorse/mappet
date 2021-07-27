package mchorse.mappet.commands.huds;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandHudBase extends MappetCommandBase
{
    protected HUDScene getHud(String id) throws CommandException
    {
        HUDScene scene = Mappet.huds.load(id);

        if (scene == null)
        {
            throw new CommandException("hud.missing", id);
        }

        return scene;
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.huds.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}