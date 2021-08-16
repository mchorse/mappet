package mchorse.mappet;

import mchorse.mappet.api.crafting.CraftingManager;
import mchorse.mappet.api.data.DataManager;
import mchorse.mappet.api.dialogues.DialogueManager;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.api.factions.FactionManager;
import mchorse.mappet.api.huds.HUDManager;
import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.npcs.NpcManager;
import mchorse.mappet.api.quests.QuestManager;
import mchorse.mappet.api.quests.chains.QuestChainManager;
import mchorse.mappet.api.scripts.ScriptManager;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.blocks.BlockRegion;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mappet.utils.ValueButtons;
import mchorse.mappet.utils.ValueSyntaxStyle;
import mchorse.mclib.McLib;
import mchorse.mclib.commands.utils.L10n;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.config.values.ValueBoolean;
import mchorse.mclib.config.values.ValueInt;
import mchorse.mclib.config.values.ValueString;
import mchorse.mclib.events.RegisterConfigEvent;
import mchorse.mclib.events.RemoveDashboardPanels;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
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
    public static final String VERSION = "0.1";

    @Mod.Instance
    public static Mappet instance;

    @SidedProxy(serverSide = "mchorse.mappet.CommonProxy", clientSide = "mchorse.mappet.ClientProxy")
    public static CommonProxy proxy;

    public static L10n l10n = new L10n(MOD_ID);
    public static final EventBus EVENT_BUS = new EventBus();

    /* Content */
    public static Item npcTool;

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
    public static ServerSettings settings;
    public static States states;
    public static QuestManager quests;
    public static CraftingManager crafting;
    public static EventManager events;
    public static DialogueManager dialogues;
    public static ExpressionManager expressions;
    public static NpcManager npcs;
    public static FactionManager factions;
    public static DataManager data;
    public static QuestChainManager chains;
    public static ScriptManager scripts;
    public static HUDManager huds;

    /* Configuration */
    public static ValueString generalEncoding;
    public static ValueBoolean generalDataCaching;

    public static ValueBoolean npcsPeacefulDamage;
    public static ValueBoolean npcsToolOnlyOP;

    public static ValueInt eventMaxExecutions;
    public static ValueBoolean eventUseServerForCommands;

    public static ValueInt nodePulseBackgroundColor;
    public static ValueBoolean nodePulseBackgroundMcLibPrimary;
    public static ValueInt nodeThickness;
    public static ValueBoolean questsPreviewRewards;
    public static ValueInt journalButtonX;
    public static ValueInt journalButtonY;

    public static ValueSyntaxStyle scriptEditorSyntaxStyle;
    public static ValueBoolean scriptEditorSounds;
    public static ValueBoolean scriptUIDebug;

    public Mappet()
    {
        MinecraftForge.EVENT_BUS.register(new RegisterHandler());
    }

    @SubscribeEvent
    public void onConfigRegister(RegisterConfigEvent event)
    {
        ConfigBuilder builder = event.createBuilder(MOD_ID);

        builder.category("general").register(new ValueButtons("buttons").clientSide());
        generalEncoding = builder.getString("encoding", "UTF-8");
        generalEncoding.invisible();
        generalDataCaching = builder.getBoolean("data_caching", true);

        npcsPeacefulDamage = builder.category("npc").getBoolean("peaceful_damage", true);
        npcsToolOnlyOP = builder.getBoolean("tool_only_op", true);

        eventMaxExecutions = builder.category("events").getInt("max_executions", 10000, 100, 1000000);
        eventUseServerForCommands = builder.getBoolean("use_server_for_commands", false);

        nodePulseBackgroundColor = builder.category("gui").getInt("pulse_background_color", 0x000000).color();
        nodePulseBackgroundMcLibPrimary = builder.getBoolean("pulse_background_mclib", false);
        nodeThickness = builder.getInt("node_thickness", 3, 0, 20);
        questsPreviewRewards = builder.getBoolean("quest_preview_rewards", true);
        journalButtonX = builder.getInt("journal_button_x", 0, 0, 300);
        journalButtonY = builder.getInt("journal_button_y", 0, 0, 300);
        builder.getCategory().markClientSide();

        builder.category("script_editor").register(scriptEditorSyntaxStyle = new ValueSyntaxStyle("syntax_style"));
        scriptEditorSounds = builder.getBoolean("sounds", true);
        scriptUIDebug = builder.getBoolean("ui_debug", false);
        builder.getCategory().markClientSide();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDashboardPanelsRemove(RemoveDashboardPanels event)
    {
        GuiMappetDashboard.dashboard = null;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        McLib.EVENT_BUS.register(this);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        /* Register command */
        event.registerServerCommand(new CommandMappet());

        /* Initiate managers and global state*/
        File mappetWorldFolder = new File(DimensionManager.getCurrentSaveRootDirectory(), MOD_ID);

        mappetWorldFolder.mkdirs();

        settings = new ServerSettings(new File(mappetWorldFolder, "settings.json"));
        settings.load();
        states = new States(new File(mappetWorldFolder, "states.json"));
        states.load();

        quests = new QuestManager(new File(mappetWorldFolder, "quests"));
        crafting = new CraftingManager(new File(mappetWorldFolder, "crafting"));
        events = new EventManager(new File(mappetWorldFolder, "events"));
        dialogues = new DialogueManager(new File(mappetWorldFolder, "dialogues"));
        expressions = new ExpressionManager();
        npcs = new NpcManager(new File(mappetWorldFolder, "npcs"));
        factions = new FactionManager(new File(mappetWorldFolder, "factions"));
        data = new DataManager(new File(mappetWorldFolder, "data"));
        chains = new QuestChainManager(new File(mappetWorldFolder, "chains"));
        scripts = new ScriptManager(new File(mappetWorldFolder, "scripts"));
        huds = new HUDManager(new File(mappetWorldFolder, "huds"));

        /* Initiate */
        if (!settings.serverLoad.isEmpty())
        {
            settings.serverLoad.trigger(new DataContext(event.getServer()));
        }
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event)
    {
        if (settings != null)
        {
            settings.save();
            settings = null;
            states.save();
            states = null;

            quests = null;
            crafting = null;
            events = null;
            dialogues = null;
            expressions = null;
            npcs = null;
            factions = null;
            data = null;
            chains = null;
            scripts = null;
            huds = null;
        }

        CommonProxy.eventHandler.reset();
    }
}