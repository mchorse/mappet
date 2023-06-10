package mchorse.mappet.network.client.blocks;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.blocks.PacketEditConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.network.ClientMessageHandler;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEditConditionModel extends ClientMessageHandler<PacketEditConditionModel>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEditConditionModel message)
    {
        TileEntity tile = WorldUtils.getTileEntity(player.world, message.pos);

        if (!(tile instanceof TileConditionModel))
        {
            return;
        }

        TileConditionModel tileConditionModel = (TileConditionModel) tile;

        GuiMappetDashboard dashboard = GuiMappetDashboard.get(Minecraft.getMinecraft());
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (message.isEdit)
        {
            tileConditionModel.fill(message.tag);

            dashboard.panels.setPanel(dashboard.conditionModel);
            dashboard.conditionModel.fill(tileConditionModel, true);

            Minecraft.getMinecraft().displayGuiScreen(dashboard);
        }
        else if (!dashboard.equals(screen) || !dashboard.panels.view.delegate.equals(dashboard.conditionModel))
        {
            AbstractMorph morph = MorphManager.INSTANCE.morphFromNBT(message.tag.getCompoundTag("morph"));

            tileConditionModel.isGlobal = message.tag.getBoolean("global");
            tileConditionModel.isShadow = message.tag.getBoolean("shadow");

            if (tileConditionModel.entity == null)
            {
                tileConditionModel.createEntity(player.world);
            }

            if (tileConditionModel.entity.morph.get() == null || !tileConditionModel.entity.morph.get().equals(morph))
            {
                tileConditionModel.entity.morph.set(null);
                tileConditionModel.entity.morph(morph, true);
                tileConditionModel.entity.ticksExisted = 0;
            }
        }
    }
}