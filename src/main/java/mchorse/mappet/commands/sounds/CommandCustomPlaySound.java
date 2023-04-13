package mchorse.mappet.commands.sounds;

import mchorse.mappet.client.SoundPack;
import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandPlaySound;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCustomPlaySound extends MappetSubCommandBase {
    private CommandPlaySound originalCommand = new CommandPlaySound();

    @Override
    public String getName() {
        return originalCommand.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return originalCommand.getUsage(sender);
    }

    @Override
    public List<String> getAliases() {
        return originalCommand.getAliases();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        originalCommand.execute(server, sender, args);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> completions = originalCommand.getTabCompletions(server, sender, args, targetPos);

        if (args.length == 1) {
            completions.removeAll(completions);
            completions.addAll(SoundPack.getCustomSoundEvents());
        }

        return completions;
    }
}