package mchorse.mappet;

import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mappet.api.conditions.blocks.ConditionBlock;
import mchorse.mappet.api.conditions.blocks.DialogueBlock;
import mchorse.mappet.api.conditions.blocks.EntityBlock;
import mchorse.mappet.api.conditions.blocks.FactionBlock;
import mchorse.mappet.api.conditions.blocks.ItemBlock;
import mchorse.mappet.api.conditions.blocks.QuestBlock;
import mchorse.mappet.api.conditions.blocks.StateBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeBlock;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.api.utils.factory.MapFactory;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.CharacterStorage;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.events.RegisterAbstractBlockEvent;
import mchorse.mappet.events.RegisterDialogueNodesEvent;
import mchorse.mappet.events.RegisterEventNodesEvent;
import mchorse.mappet.events.RegisterQuestChainNodesEvent;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.utils.MappetNpcSelector;
import mchorse.mappet.utils.MetamorphHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;

/**
 * Common proxy 
 */
public class CommonProxy
{
    private static IFactory<EventNode> events;
    private static IFactory<EventNode> dialogues;
    private static IFactory<QuestNode> chains;
    private static IFactory<AbstractBlock> conditionBlocks;

    /**
     * Client folder where saved selectors and animations are getting
     * stored.
     */
    public static File configFolder;

    public static EventHandler eventHandler;

    public static IFactory<EventNode> getEvents()
    {
        return events;
    }

    public static IFactory<EventNode> getDialogues()
    {
        return dialogues;
    }

    public static IFactory<QuestNode> getChains()
    {
        return chains;
    }

    public static IFactory<AbstractBlock> getConditionBlocks()
    {
        return conditionBlocks;
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        /* Setup config folder path */
        String path = event.getModConfigurationDirectory().getAbsolutePath();

        configFolder = new File(path, Mappet.MOD_ID);
        configFolder.mkdir();

        Dispatcher.register();

        MinecraftForge.EVENT_BUS.register(eventHandler = new EventHandler());

        GameRegistry.registerEntitySelector(new MappetNpcSelector(), MappetNpcSelector.ARGUMENT_MAPPET_NPC_ID, MappetNpcSelector.ARGUMENT_MAPPET_STATES);

        CapabilityManager.INSTANCE.register(ICharacter.class, new CharacterStorage(), Character::new);
    }

    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new MetamorphHandler());
        Mappet.EVENT_BUS.register(eventHandler);
    }

    public void postInit(FMLPostInitializationEvent event)
    {
        /* Register event nodes */
        MapFactory<EventNode> eventNodes = new MapFactory<EventNode>()
            .register("command", CommandNode.class, 0x942aff)
            .register("condition", ConditionNode.class, 0xff1493)
            .register("switch", SwitchNode.class, 0xaee300)
            .register("timer", TimerNode.class, 0x11ff33);

        events = eventNodes;
        Mappet.EVENT_BUS.post(new RegisterEventNodesEvent(eventNodes));

        /* Register dialogue nodes */
        MapFactory<EventNode> dialogueNodes = eventNodes.copy()
            .register("reply", ReplyNode.class, 0x00a0ff)
            .register("reaction", ReactionNode.class, 0xff2200)
            .register("crafting", CraftingNode.class, 0xff6600)
            .register("quest_chain", QuestChainNode.class, 0xffff00).alias("quest_chain", "quest")
            .unregister("timer");

        dialogues = dialogueNodes;
        Mappet.EVENT_BUS.post(new RegisterDialogueNodesEvent(dialogueNodes));

        /* Register quest chain blocks */
        MapFactory<QuestNode> questChainNodes = new MapFactory<QuestNode>()
            .register("quest", QuestNode.class, 0xffff00);

        chains = questChainNodes;
        Mappet.EVENT_BUS.post(new RegisterQuestChainNodesEvent(questChainNodes));

        /* Register condition blocks */
        MapFactory<AbstractBlock> blocks = new MapFactory<AbstractBlock>()
            .register("quest", QuestBlock.class, 0xffaa00)
            .register("state", StateBlock.class, 0xff0022)
            .register("dialogue", DialogueBlock.class, 0x00ff33)
            .register("faction", FactionBlock.class, 0x942aff)
            .register("item", ItemBlock.class, 0xff7700)
            .register("world_time", WorldTimeBlock.class, 0x0088ff)
            .register("entity", EntityBlock.class, 0x2d4163)
            .register("condition", ConditionBlock.class, 0xff1493);

        conditionBlocks = blocks;
        Mappet.EVENT_BUS.post(new RegisterAbstractBlockEvent(blocks));
    }
}