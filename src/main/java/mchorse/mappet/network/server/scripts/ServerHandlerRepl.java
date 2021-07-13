package mchorse.mappet.network.server.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.scripts.PacketRepl;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextFormatting;

import javax.script.ScriptException;

public class ServerHandlerRepl extends ServerMessageHandler<PacketRepl>
{
    @Override
    public void run(EntityPlayerMP player, PacketRepl message)
    {
        if (!OpHelper.isPlayerOp(player))
        {
            return;
        }

        try
        {
            String output = Mappet.scripts.executeRepl(player, message.code);

            Dispatcher.sendTo(new PacketRepl(output), player);
        }
        catch (ScriptException e)
        {
            e.printStackTrace();

            Dispatcher.sendTo(new PacketRepl(TextFormatting.RED + e.getMessage()), player);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}