package mchorse.mappet.capabilities;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.quests.Quests;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class Character implements ICharacter
{
    public static ICharacter get(EntityPlayer player)
    {
        return player.getCapability(CharacterProvider.CHARACTER, null);
    }

    private Quests quests = new Quests();
    private CraftingTable table;

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
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Quests", this.quests.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Quests"))
        {
            this.quests.deserializeNBT(tag.getTagList("Quests", Constants.NBT.TAG_COMPOUND));
        }
    }
}