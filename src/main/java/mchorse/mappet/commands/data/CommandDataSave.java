package mchorse.mappet.commands.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.data.Data;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandDataSave extends CommandDataBase
{
    @Override
    public String getName()
    {
        return "save";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.data.save";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}data save{r} {7}<id>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[0];
        Data data = new Data();
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);

        data.save(player);

        if (Mappet.data.save(id, data))
        {
            this.getL10n().success(sender, "data.saved", id);
        }
    }
}