package mchorse.mappet.capabilities.character;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.quests.Quests;
import mchorse.mappet.api.states.States;
import mchorse.mappet.utils.CurrentSession;
import mchorse.mappet.utils.PositionCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    /* Prev position */

    public PositionCache getPositionCache();

    /* Admin editing */

    public CurrentSession getCurrentSession();

    public void copy(ICharacter character, EntityPlayer player);

    /* GUIs */

    public UIContext getUIContext();

    public void setUIContext(UIContext context);

    /* HUDs */
    public boolean setupHUD(String id, boolean addToDisplayedList);

    public void changeHUDMorph(String id, int index, NBTTagCompound tag);

    public void closeHUD(String id);

    public void closeAllHUD();

    public Map<String, List<HUDScene>> getDisplayedHUDs();
}