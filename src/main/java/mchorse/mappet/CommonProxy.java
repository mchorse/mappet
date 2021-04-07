package mchorse.mappet;

import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.CharacterStorage;
import mchorse.mappet.capabilities.ICharacter;
import mchorse.mappet.network.Dispatcher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Common proxy 
 */
public class CommonProxy
{
    /**
     * Client folder where saved selectors and animations are getting
     * stored.
     */
    public static File configFolder;

    public void preInit(FMLPreInitializationEvent event)
    {
        /* Setup config folder path */
        String path = event.getModConfigurationDirectory().getAbsolutePath();

        configFolder = new File(path, Mappet.MOD_ID);
        configFolder.mkdir();

        Dispatcher.register();

        MinecraftForge.EVENT_BUS.register(new EventHandler());

        CapabilityManager.INSTANCE.register(ICharacter.class, new CharacterStorage(), Character::new);
    }

    public void init(FMLInitializationEvent event)
    {}
}