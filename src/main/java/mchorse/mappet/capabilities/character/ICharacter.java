package mchorse.mappet.capabilities.character;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.states.States;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;

public interface ICharacter extends INBTSerializable<NBTTagCompound>
{
    public States getStates();

    public Quests getQuests();

    /* Crafting table */

    public void setCraftingTable(CraftingTable table);

    public CraftingTable getCraftingTable();

    /* Dialogue */

    public void setDialogue(Dialogue dialogue, DialogueContext context);

    public Dialogue getDialogue();

    public DialogueContext getDialogueContext();

    /* Last clear */

    public Instant getLastClear();

    public void updateLastClear(Instant instant);
}