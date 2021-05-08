package mchorse.mappet.api.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.quests.chains.QuestContext;
import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.api.utils.TriggerSender;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueManager extends BaseManager<Dialogue>
{
    public static final MapNodeFactory FACTORY = EventManager.FACTORY.copy()
        .register("reply", ReplyNode.class)
        .register("reaction", ReactionNode.class)
        .register("crafting", CraftingNode.class)
        .register("quest", QuestChainNode.class)
        .unregister("timer");

    public DialogueManager(File folder)
    {
        super(folder);
    }

    @Override
    protected Dialogue createData(NBTTagCompound tag)
    {
        Dialogue dialogue = new Dialogue(FACTORY);

        if (tag != null)
        {
            dialogue.deserializeNBT(tag);
        }

        return dialogue;
    }

    public void open(EntityPlayerMP player, Dialogue dialogue)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            DialogueContext context = new DialogueContext(new TriggerSender().set(player), player);

            character.setDialogue(dialogue, context);
            character.getStates().readDialogue(dialogue.getId());
            Mappet.dialogues.execute(dialogue, context);

            this.handleContext(player, dialogue, context);
        }
    }

    public void handleContext(EntityPlayerMP player, Dialogue dialogue, DialogueContext context)
    {
        List<DialogueFragment> replies = context.replyNodes.stream().map((r) -> r.message).collect(Collectors.toList());
        DialogueFragment reaction = context.reactionNode == null ? new DialogueFragment() : context.reactionNode.message;

        PacketDialogueFragment packet = new PacketDialogueFragment(dialogue.title, reaction, replies);
        ICharacter character = Character.get(player);

        if (context.reactionNode != null)
        {
            packet.addMorph(context.reactionNode.morph);
        }

        if (context.quest != null)
        {
            /* TODO: subject */
            packet.addQuests(Mappet.chains.evaluate(context.quest.chain, player, "test"));
        }
        else if (context.crafting != null)
        {
            CraftingTable table = Mappet.crafting.load(context.crafting.table);

            if (table != null)
            {
                table.filter(player);

                character.setCraftingTable(table);
                packet.addCraftingTable(table);
            }
        }
        else
        {
            character.setCraftingTable(null);
        }

        Dispatcher.sendTo(packet, player);
    }

    /* Dialogue execution */

    public DialogueContext execute(Dialogue event, DialogueContext context)
    {
        if (event.main != null)
        {
            this.recursiveExecute(event, event.main, context, false);
        }

        return context;
    }

    public void recursiveExecute(Dialogue system, EventNode node, DialogueContext context, boolean skipFirst)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        int result = skipFirst ? EventNode.ALL : node.execute(context);

        if (result >= EventNode.ALL)
        {
            context.nesting += 1;

            List<EventNode> children = system.getChildren(node);

            if (result == EventNode.ALL)
            {
                for (EventNode child : children)
                {
                    this.recursiveExecute(system, child, context, false);
                }
            }
            else if (result <= children.size())
            {
                this.recursiveExecute(system, children.get(result - 1), context, false);
            }

            context.nesting -= 1;
        }

        context.executions += 1;
    }
}