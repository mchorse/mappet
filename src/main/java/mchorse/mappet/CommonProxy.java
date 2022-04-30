package mchorse.mappet;

import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.api.conditions.blocks.ConditionConditionBlock;
import mchorse.mappet.api.conditions.blocks.DialogueConditionBlock;
import mchorse.mappet.api.conditions.blocks.EntityConditionBlock;
import mchorse.mappet.api.conditions.blocks.ExpressionConditionBlock;
import mchorse.mappet.api.conditions.blocks.FactionConditionBlock;
import mchorse.mappet.api.conditions.blocks.ItemConditionBlock;
import mchorse.mappet.api.conditions.blocks.MorphConditionBlock;
import mchorse.mappet.api.conditions.blocks.QuestConditionBlock;
import mchorse.mappet.api.conditions.blocks.StateConditionBlock;
import mchorse.mappet.api.conditions.blocks.WorldTimeConditionBlock;
import mchorse.mappet.api.dialogues.nodes.CommentNode;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.QuestDialogueNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.nodes.CancelNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.events.nodes.TriggerNode;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ItemTriggerBlock;
import mchorse.mappet.api.triggers.blocks.MorphTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.triggers.blocks.SoundTriggerBlock;
import mchorse.mappet.api.triggers.blocks.StateTriggerBlock;
import mchorse.mappet.api.ui.components.UIButtonComponent;
import mchorse.mappet.api.ui.components.UIClickComponent;
import mchorse.mappet.api.ui.components.UIComponent;
import mchorse.mappet.api.ui.components.UIGraphicsComponent;
import mchorse.mappet.api.ui.components.UIIconButtonComponent;
import mchorse.mappet.api.ui.components.UILabelComponent;
import mchorse.mappet.api.ui.components.UILayoutComponent;
import mchorse.mappet.api.ui.components.UIMorphComponent;
import mchorse.mappet.api.ui.components.UIStackComponent;
import mchorse.mappet.api.ui.components.UIStringListComponent;
import mchorse.mappet.api.ui.components.UITextComponent;
import mchorse.mappet.api.ui.components.UITextareaComponent;
import mchorse.mappet.api.ui.components.UITextboxComponent;
import mchorse.mappet.api.ui.components.UIToggleComponent;
import mchorse.mappet.api.ui.components.UITrackpadComponent;
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
import mchorse.mappet.events.RegisterUIComponentEvent;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.utils.Colors;
import mchorse.mappet.utils.MPIcons;
import mchorse.mappet.utils.MappetNpcSelector;
import mchorse.mappet.utils.MetamorphHandler;
import mchorse.mappet.utils.ScriptUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.script.ScriptEngine;
import java.io.File;

/**
 * Common proxy 
 */
public class CommonProxy
{
    private static IFactory<EventBaseNode> events;
    private static IFactory<EventBaseNode> dialogues;
    private static IFactory<QuestNode> chains;
    private static IFactory<AbstractConditionBlock> conditionBlocks;
    private static IFactory<AbstractTriggerBlock> triggerBlocks;
    private static IFactory<UIComponent> uiComponents;

    public static IFactory<EventBaseNode> getEvents()
    {
        return events;
    }
    public static IFactory<EventBaseNode> getDialogues()
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
    public static IFactory<UIComponent> getUiComponents()
    {
        return uiComponents;
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
        MPIcons.register();
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
        MapFactory<EventBaseNode> eventNodes = new MapFactory<EventBaseNode>()
            .register("command", CommandNode.class, Colors.COMMAND)
            .register("condition", ConditionNode.class, Colors.CONDITION)
            .register("switch", SwitchNode.class, Colors.FACTION)
            .register("timer", TimerNode.class, Colors.TIME)
            .register("trigger", TriggerNode.class, Colors.STATE)
                /* Backward compatibility with gamma build */
                .alias("trigger", "event")
                .alias("trigger", "dialogue")
                .alias("trigger", "script")
            .register("cancel", CancelNode.class, Colors.CANCEL);

        events = eventNodes;
        Mappet.EVENT_BUS.post(new RegisterEventNodeEvent(eventNodes));

        /* Register dialogue nodes */
        MapFactory<EventBaseNode> dialogueNodes = eventNodes.copy()
            .register("reply", ReplyNode.class, Colors.REPLY)
            .register("comment", CommentNode.class, Colors.COMMENT)
            .register("reaction", ReactionNode.class, Colors.STATE)
            .register("crafting", CraftingNode.class, Colors.CRAFTING)
            .register("quest_chain", QuestChainNode.class, Colors.QUEST)
            .register("quest", QuestDialogueNode.class, Colors.QUEST)
            .unregister("timer");

        dialogues = dialogueNodes;
        Mappet.EVENT_BUS.post(new RegisterDialogueNodeEvent(dialogueNodes));

        /* Register quest chain blocks */
        MapFactory<QuestNode> questChainNodes = new MapFactory<QuestNode>()
            .register("quest", QuestNode.class, Colors.QUEST);

        chains = questChainNodes;
        Mappet.EVENT_BUS.post(new RegisterQuestChainNodeEvent(questChainNodes));

        /* Register condition blocks */
        MapFactory<AbstractConditionBlock> conditions = new MapFactory<AbstractConditionBlock>()
            .register("quest", QuestConditionBlock.class, Colors.QUEST)
            .register("state", StateConditionBlock.class, Colors.STATE)
            .register("dialogue", DialogueConditionBlock.class, Colors.DIALOGUE)
            .register("faction", FactionConditionBlock.class, Colors.FACTION)
            .register("item", ItemConditionBlock.class, Colors.CRAFTING)
            .register("world_time", WorldTimeConditionBlock.class, Colors.TIME)
            .register("entity", EntityConditionBlock.class, Colors.ENTITY)
            .register("condition", ConditionConditionBlock.class, Colors.CONDITION)
            .register("morph", MorphConditionBlock.class, Colors.MORPH)
            .register("expression", ExpressionConditionBlock.class, Colors.CANCEL);

        conditionBlocks = conditions;
        Mappet.EVENT_BUS.post(new RegisterConditionBlockEvent(conditions));

        /* Register condition blocks */
        MapFactory<AbstractTriggerBlock> triggers = new MapFactory<AbstractTriggerBlock>()
            .register("command", CommandTriggerBlock.class, Colors.COMMAND)
            .register("sound", SoundTriggerBlock.class, Colors.REPLY)
            .register("event", EventTriggerBlock.class, Colors.STATE)
            .register("dialogue", DialogueTriggerBlock.class, Colors.DIALOGUE)
            .register("script", ScriptTriggerBlock.class, Colors.ENTITY)
            .register("item", ItemTriggerBlock.class, Colors.CRAFTING)
            .register("state", StateTriggerBlock.class, Colors.STATE)
            .register("morph", MorphTriggerBlock.class, Colors.MORPH);

        triggerBlocks = triggers;
        Mappet.EVENT_BUS.post(new RegisterTriggerBlockEvent(triggers));

        /* Register UI components */
        MapFactory<UIComponent> ui = new MapFactory<UIComponent>()
            .register("graphics", UIGraphicsComponent.class, 0xffffff)
            .register("button", UIButtonComponent.class, 0xffffff)
            .register("icon", UIIconButtonComponent.class, 0xffffff)
            .register("label", UILabelComponent.class, 0xffffff)
            .register("text", UITextComponent.class, 0xffffff)
            .register("textbox", UITextboxComponent.class, 0xffffff)
            .register("textarea", UITextareaComponent.class, 0xffffff)
            .register("toggle", UIToggleComponent.class, 0xffffff)
            .register("trackpad", UITrackpadComponent.class, 0xffffff)
            .register("strings", UIStringListComponent.class, 0xffffff)
            .register("item", UIStackComponent.class, 0xffffff)
            .register("layout", UILayoutComponent.class, 0xffffff)
            .register("morph", UIMorphComponent.class, 0xffffff)
            .register("clickarea", UIClickComponent.class, 0xffffff);

        uiComponents = ui;
        Mappet.EVENT_BUS.post(new RegisterUIComponentEvent(ui));
    }
}