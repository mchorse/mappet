package mchorse.mappet.capabilities.character;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.states.States;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.time.Instant;

public class Character implements ICharacter
{
    public static ICharacter get(EntityPlayer player)
    {
        return player.getCapability(CharacterProvider.CHARACTER, null);
    }

    private Quests quests = new Quests();
    private States states = new States();

    private CraftingTable table;

    private DialogueNodeSystem dialogue;
    private DialogueContext dialogueContext;

    private Instant lastClear = Instant.now();

    @Override
    public States getStates()
    {
        return this.states;
    }

    @Override
    public Quests getQuests()
    {
        return this.quests;
    }

    @Override
    public void setCraftingTable(CraftingTable table)
    {
        this.table = table;
    }

    @Override
    public CraftingTable getCraftingTable()
    {
        return this.table;
    }

    @Override
    public void setDialogue(DialogueNodeSystem dialogue, DialogueContext context)
    {
        this.dialogue = dialogue;
        this.dialogueContext = context;
    }

    @Override
    public DialogueNodeSystem getDialogue()
    {
        return this.dialogue;
    }

    @Override
    public DialogueContext getDialogueContext()
    {
        return this.dialogueContext;
    }

    @Override
    public Instant getLastClear()
    {
        return this.lastClear;
    }

    @Override
    public void updateLastClear(Instant instant)
    {
        this.lastClear = instant;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Quests", this.quests.serializeNBT());
        tag.setTag("States", this.states.serializeNBT());
        tag.setString("LastClear", this.lastClear.toString());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Quests"))
        {
            this.quests.deserializeNBT(tag.getCompoundTag("Quests"));
        }

        if (tag.hasKey("States"))
        {
            this.states.deserializeNBT(tag.getCompoundTag("States"));
        }

        if (tag.hasKey("LastClear"))
        {
            try
            {
                this.lastClear = Instant.parse(tag.getString("LastClear"));
            }
            catch (Exception e)
            {}
        }
    }
}