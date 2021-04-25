package mchorse.mappet;

import mchorse.mappet.api.crafting.CraftingManager;
import mchorse.mappet.api.dialogues.DialogueManager;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.api.factions.FactionManager;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcManager;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.quests.QuestManager;
import mchorse.mappet.api.states.States;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.blocks.BlockRegion;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mclib.McLib;
import mchorse.mclib.commands.utils.L10n;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.config.values.ValueBoolean;
import mchorse.mclib.config.values.ValueInt;
import mchorse.mclib.events.RegisterConfigEvent;
import mchorse.mclib.events.RemoveDashboardPanels;
import mchorse.metamorph.api.MorphManager;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    /* Content */
    public static BlockEmitter emitterBlock;
    public static BlockTrigger triggerBlock;
    public static BlockRegion regionBlock;

    public static CreativeTabs creativeTab = new CreativeTabs(MOD_ID)
    {
        @Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(emitterBlock);
        }
    };

    /* Server side data */
    public static States states;
    public static QuestManager quests;
    public static CraftingManager crafting;
    public static EventManager events;
    public static DialogueManager dialogues;
    public static ExpressionManager expressions;
    public static NpcManager npcs;
    public static FactionManager factions;

    /* Configuration */
    public static ValueInt eventMaxExecutions;

    public static ValueInt nodePulseBackgroundColor;
    public static ValueBoolean nodePulseBackgroundMcLibPrimary;

    public Mappet()
    {
        MinecraftForge.EVENT_BUS.register(new RegisterHandler());
    }

    @SubscribeEvent
    public void onConfigRegister(RegisterConfigEvent event)
    {
        ConfigBuilder builder = event.createBuilder(MOD_ID);

        eventMaxExecutions = builder.category("events").getInt("max_executions", 10000, 100, 1000000);

        nodePulseBackgroundColor = builder.category("gui").getInt("pulse_background_color", 0x000000).color();
        nodePulseBackgroundMcLibPrimary = builder.getBoolean("pulse_background_mclib", false);

        builder.getCategory().markClientSide();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDashboardPanelsRemove(RemoveDashboardPanels event)
    {
        GuiMappetDashboard.dashboard = null;
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
        crafting = new CraftingManager(new File(mappetWorldFolder, "crafting"));
        events = new EventManager(new File(mappetWorldFolder, "events"));
        dialogues = new DialogueManager(new File(mappetWorldFolder, "dialogues"));
        expressions = new ExpressionManager();
        npcs = new NpcManager(new File(mappetWorldFolder, "npcs"));
        factions = new FactionManager(new File(mappetWorldFolder, "factions"));

        event.registerServerCommand(new CommandMappet());
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        states.save();
        states = null;

        quests = null;
        crafting = null;
        events = null;
        dialogues = null;
        expressions = null;
        npcs = null;
        factions = null;
    }
}