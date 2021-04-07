package mchorse.mappet;

import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.QuestManager;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.quests.objectives.CollectObjective;
import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mappet.api.quests.rewards.ItemStackReward;
import mchorse.mappet.api.states.States;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mclib.McLib;
import mchorse.mclib.commands.utils.L10n;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.events.RegisterConfigEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

/**
 * Mappet mod
 * 
 * Adventure map toolset mod
 */
@Mod(modid = Mappet.MOD_ID, name = "Mappet", version = Mappet.VERSION, dependencies = "required-after:mclib@[%MCLIB%,);required-after:metamorph@[%METAMORPH%,);required-after:blockbuster@[%BLOCKBUSTER%,);", updateJSON = "https://raw.githubusercontent.com/mchorse/mappet/master/version.json")
public final class Mappet
{
    public static final String MOD_ID = "mappet";
    public static final String VERSION = "%VERSION%";

    @Mod.Instance
    public static Mappet instance;

    @SidedProxy(serverSide = "mchorse.mappet.CommonProxy", clientSide = "mchorse.mappet.ClientProxy")
    public static CommonProxy proxy;

    public static L10n l10n = new L10n(MOD_ID);

    /* Server side data */
    public static States states;
    public static QuestManager quests;

    @SubscribeEvent
    public void onConfigRegister(RegisterConfigEvent event)
    {
        ConfigBuilder builder = event.createBuilder(MOD_ID);

        /* ... */
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        McLib.EVENT_BUS.register(this);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        File mappetWorldFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), MOD_ID);

        mappetWorldFolder.mkdirs();
        states = new States(new File(mappetWorldFolder, "states.json"));
        states.load();

        quests = new QuestManager(new File(mappetWorldFolder, "quests"));
        quests.loadCache();

        event.registerServerCommand(new CommandMappet());
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        states.save();
        states = null;

        quests.save();
        quests = null;
    }
}