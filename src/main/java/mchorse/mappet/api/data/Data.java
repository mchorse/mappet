package mchorse.mappet.api.data;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public class Data extends AbstractData
{
    public States global = new States();
    public States player = new States();
    public NonNullList<ItemStack> inventory = NonNullList.create();

    public void save(EntityPlayer player)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            this.global.copy(Mappet.states);
            this.player.copy(character.getStates());

            for (int i = 0, c = player.inventory.getSizeInventory(); i < c; i++)
            {
                this.inventory.add(player.inventory.getStackInSlot(i).copy());
            }
        }
    }

    public void apply(EntityPlayer player, boolean global)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            if (global)
            {
                Mappet.states.copy(this.global);
            }

            character.getStates().copy(this.player);

            player.inventory.clear();

            for (int i = 0, c = Math.min(this.inventory.size(), player.inventory.getSizeInventory()); i < c; i++)
            {
                player.inventory.setInventorySlotContents(i, this.inventory.get(i).copy());
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Global", this.global.serializeNBT());
        tag.setTag("Player", this.player.serializeNBT());

        NBTTagList inventory = new NBTTagList();

        for (ItemStack stack : this.inventory)
        {
            inventory.appendTag(stack.serializeNBT());
        }

        tag.setTag("Inventory", inventory);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Global"))
        {
            this.global.deserializeNBT(tag.getCompoundTag("Global"));
        }

        if (tag.hasKey("Player"))
        {
            this.player.deserializeNBT(tag.getCompoundTag("Player"));
        }

        NBTTagList inventory = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < inventory.tagCount(); i++)
        {
            this.inventory.add(new ItemStack(inventory.getCompoundTagAt(i)));
        }
    }
}