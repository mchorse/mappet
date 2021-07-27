package mchorse.mappet.commands.huds;

import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.huds.PacketHUDScene;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandHudSetup extends CommandHudBase
{
    @Override
    public String getName()
    {
        return "setup";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.hud.setup";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}hud setup{r} {7}<target> <id>{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        HUDScene scene = this.getHud(args[1]);

        Dispatcher.sendTo(new PacketHUDScene(scene.getId(), scene.serializeNBT()), player);
    }
}