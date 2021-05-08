package mchorse.mappet.network.client.blocks;

import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.GuiTriggerBlockScreen;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEditTrigger extends ClientMessageHandler<PacketEditTrigger>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEditTrigger message)
    {
        Trigger left = new Trigger();
        Trigger right = new Trigger();

        left.deserializeNBT(message.left);
        right.deserializeNBT(message.right);

        Minecraft.getMinecraft().displayGuiScreen(new GuiTriggerBlockScreen(message.pos, left, right, message.collidable));
    }
}