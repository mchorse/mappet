package mchorse.mappet.entities;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CommandNpc
{
    public String command = "";

    public CommandNpc()
    {}

    public CommandNpc(String command)
    {
        this.command = command;
    }

    public void apply(EntityNpc npc)
    {
        if (!this.command.isEmpty())
        {
            MinecraftServer server = npc.world.getMinecraftServer();
            if (server != null)
            {
                ICommandManager manager = server.commandManager;
                manager.executeCommand(new CommandSender(npc), this.command);
            }
        }
    }

    public static ICommandSender getCommandSender(EntityNpc npc)
    {
        return new CommandSender(npc);
    }

    public static class CommandSender implements ICommandSender
    {
        public EntityNpc npc;

        public CommandSender(EntityNpc npc)
        {
            this.npc = npc;
        }

        @Override
        public String getName()
        {
            return "CommandNPC";
        }

        @Override
        public boolean canUseCommand(int permLevel, String commandName)
        {
            return true;
        }

        @Override
        public World getEntityWorld()
        {
            return this.npc.world;
        }

        @Nullable
        @Override
        public MinecraftServer getServer()
        {
            return this.npc.world.getMinecraftServer();
        }
    }
}
