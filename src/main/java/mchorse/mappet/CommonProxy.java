package mchorse.mappet;

import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ConditionConditionBlock;
import mchorse.mappet.api.conditions.blocks.DialogueConditionBlock;
import mchorse.mappet.api.conditions.blocks.EntityConditionBlock;
import mchorse.mappet.api.conditions.blocks.FactionConditionBlock;
import mchorse.mappet.api.conditions.blocks.ItemConditionBlock;
import mchorse.mappet.api.conditions.blocks.QuestConditionBlock;
import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeConditionBlock;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.triggers.blocks.SoundTriggerBlock;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.api.utils.factory.MapFactory;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.CharacterStorage;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.events.RegisterConditionBlockEvent;
import mchorse.mappet.events.RegisterDialogueNodeEvent;
import mchorse.mappet.events.RegisterEventNodeEvent;
import mchorse.mappet.events.RegisterQuestChainNodeEvent;
import mchorse.mappet.events.RegisterTriggerBlockEvent;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.utils.MappetNpcSelector;
import mchorse.mappet.utils.MetamorphHandler;
import mchorse.mappet.utils.ScriptUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

/**
 * Common proxy 
 */
public class CommonProxy
{
    private static IFactory<EventNode> events;
    private static IFactory<EventNode> dialogues;
    private static IFactory<QuestNode> chains;
    private static IFactory<AbstractConditionBlock> conditionBlocks;
    private static IFactory<AbstractTriggerBlock> triggerBlocks;

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
    public static IFactory<AbstractConditionBlock> getConditionBlocks()
    {
        return conditionBlocks;
    }
    public static IFactory<AbstractTriggerBlock> getTriggerBlocks()
    {
        return triggerBlocks;
    }

    /**
     * Client folder where saved selectors and animations are getting
     * stored.
     */
    public static File configFolder;

    public static EventHandler eventHandler;

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

        this.initiateJS();
    }

    /**
     * Run something in JavaScript to avoid it loading first time
     */
    private void initiateJS()
    {
        try
        {
            ScriptEngine engine = ScriptUtils.tryCreatingEngine();

            if (!engine.eval("true").equals(Boolean.TRUE))
            {
                throw new Exception("Something went wrong with JavaScript...");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        Mappet.EVENT_BUS.post(new RegisterEventNodeEvent(eventNodes));

        /* Register dialogue nodes */
        MapFactory<EventNode> dialogueNodes = eventNodes.copy()
            .register("reply", ReplyNode.class, 0x00a0ff)
            .register("reaction", ReactionNode.class, 0xff2200)
            .register("crafting", CraftingNode.class, 0xff6600)
            .register("quest_chain", QuestChainNode.class, 0xffff00).alias("quest_chain", "quest")
            .unregister("timer");

        dialogues = dialogueNodes;
        Mappet.EVENT_BUS.post(new RegisterDialogueNodeEvent(dialogueNodes));

        /* Register quest chain blocks */
        MapFactory<QuestNode> questChainNodes = new MapFactory<QuestNode>()
            .register("quest", QuestNode.class, 0xffff00);

        chains = questChainNodes;
        Mappet.EVENT_BUS.post(new RegisterQuestChainNodeEvent(questChainNodes));

        /* Register condition blocks */
        MapFactory<AbstractConditionBlock> conditions = new MapFactory<AbstractConditionBlock>()
            .register("quest", QuestConditionBlock.class, 0xffaa00)
            .register("state", StateConditionBlock.class, 0xff0022)
            .register("dialogue", DialogueConditionBlock.class, 0x11ff33)
            .register("faction", FactionConditionBlock.class, 0x942aff)
            .register("item", ItemConditionBlock.class, 0xff7700)
            .register("world_time", WorldTimeConditionBlock.class, 0x0088ff)
            .register("entity", EntityConditionBlock.class, 0x2d4163)
            .register("condition", ConditionConditionBlock.class, 0xff1493);

        conditionBlocks = conditions;
        Mappet.EVENT_BUS.post(new RegisterConditionBlockEvent(conditions));

        /* Register condition blocks */
        MapFactory<AbstractTriggerBlock> triggers = new MapFactory<AbstractTriggerBlock>()
            .register("command", CommandTriggerBlock.class, 0x942aff)
            .register("sound", SoundTriggerBlock.class, 0xff7700)
            .register("event", EventTriggerBlock.class, 0xff0022)
            .register("dialogue", DialogueTriggerBlock.class, 0x11ff33)
            .register("script", ScriptTriggerBlock.class, 0x2d4163);

        triggerBlocks = triggers;
        Mappet.EVENT_BUS.post(new RegisterTriggerBlockEvent(triggers));
    }
}