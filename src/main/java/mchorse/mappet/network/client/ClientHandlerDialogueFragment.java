package mchorse.mappet.network.client;

import mchorse.mappet.client.gui.GuiDialogueScreen;
import mchorse.mappet.network.common.PacketDialogueFragment;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerDialogueFragment extends ClientMessageHandler<PacketDialogueFragment>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketDialogueFragment message)
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof GuiDialogueScreen)
        {
            GuiDialogueScreen dialogue = (GuiDialogueScreen) screen;

            dialogue.pickReply(message);
        }
        else if (!message.replies.isEmpty())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiDialogueScreen(message));
        }
    }
}