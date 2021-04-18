package mchorse.mappet;

import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.client.renders.entity.RenderNpc;
import mchorse.mappet.client.renders.tile.TileRegionRenderer;
import mchorse.mappet.client.renders.tile.TileTriggerRenderer;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.tile.TileRegion;
import mchorse.mappet.tile.TileTrigger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        MinecraftForge.EVENT_BUS.register(new KeyboardHandler());

        ClientRegistry.bindTileEntitySpecialRenderer(TileTrigger.class, new TileTriggerRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRegion.class, new TileRegionRenderer());

        RenderingRegistry.registerEntityRenderingHandler(EntityNpc.class, new RenderNpc.Factory());
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }
}