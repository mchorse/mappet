package mchorse.mappet;

import mchorse.mappet.client.KeyboardHandler;
import net.minecraftforge.common.MinecraftForge;
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
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }
}