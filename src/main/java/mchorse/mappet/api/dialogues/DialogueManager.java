package mchorse.mappet.api.dialogues;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.chains.QuestInfo;
import mchorse.mappet.api.quests.chains.QuestStatus;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class DialogueManager extends BaseManager<Dialogue>
{
    public DialogueManager(File folder)
    {
        super(folder);
    }

    @Override
    protected Dialogue createData(String id, NBTTagCompound tag)
    {
        Dialogue dialogue = new Dialogue(CommonProxy.getDialogues());

        if (tag != null)
        {
            dialogue.deserializeNBT(tag);
        }

        return dialogue;
    }

    public void open(EntityPlayerMP player, Dialogue dialogue, DialogueContext context)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.setDialogue(dialogue, context);
            Mappet.dialogues.execute(dialogue, context);

            if (context.reactionNode != null)
            {
                this.handleContext(player, dialogue, context, null);
            }
        }
    }

    public void handleContext(EntityPlayerMP player, Dialogue dialogue, DialogueContext context, ReactionNode last)
    {
        if (last != null && !last.sound.isEmpty())
        {
            WorldUtils.stopSound(player, last.sound);
        }

        List<DialogueFragment> replies = context.replyNodes.stream().map((r) -> r.message.copy().process(context.data)).collect(Collectors.toList());
        DialogueFragment reaction = context.reactionNode == null ? new DialogueFragment() : context.reactionNode.message.copy();

        reaction.process(context.data);

        PacketDialogueFragment packet = new PacketDialogueFragment(dialogue.closable, reaction, replies);
        ICharacter character = Character.get(player);

        if (context.reactionNode != null)
        {
            packet.addMorph(context.reactionNode.morph);
        }

        if (context.quest != null)
        {
            Quest quest = Mappet.quests.load(context.quest.quest);

            if (quest != null)
            {
                QuestStatus status = QuestStatus.AVAILABLE;

                if (character.getQuests().has(quest.getId()))
                {
                    Quest playerQuest = character.getQuests().getByName(quest.getId());

                    status = playerQuest.isComplete(player) ? QuestStatus.COMPLETED : QuestStatus.UNAVAILABLE;
                }

                packet.addQuest(new QuestInfo(quest, status));
            }
        }
        else if (context.questChain != null)
        {
            packet.addQuests(Mappet.chains.evaluate(context.questChain.chain, player, context.data.process(context.questChain.subject)));
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

        if (context.reactionNode != null)
        {
            if (context.reactionNode.read)
            {
                character.getStates().readDialogue(dialogue.getId(), context.reactionNode.marker);
            }

            WorldUtils.playSound(player, context.reactionNode.sound);
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

    public void recursiveExecute(Dialogue system, EventBaseNode node, DialogueContext context, boolean skipFirst)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        int result = skipFirst ? EventBaseNode.ALL : node.execute(context);

        if (result >= EventBaseNode.ALL)
        {
            context.nesting += 1;

            List<EventBaseNode> children = system.getChildren(node);

            if (result == EventBaseNode.ALL)
            {
                for (EventBaseNode child : children)
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