package mchorse.mappet.capabilities;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.quests.Quests;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICharacter extends INBTSerializable<NBTTagCompound>
{
    public Quests getQuests();

    /* Crafting table */

    public void setCraftingTable(CraftingTable table);

    public CraftingTable getCraftingTable();

    /* Dialogue */

    public void setDialogue(DialogueNodeSystem dialogue, DialogueContext context);

    public DialogueNodeSystem getDialogue();

    public DialogueContext getDialogueContext();
}