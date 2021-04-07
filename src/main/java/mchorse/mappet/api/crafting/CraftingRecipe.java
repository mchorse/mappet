package mchorse.mappet.api.crafting;

import mchorse.mappet.utils.InventoryUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class CraftingRecipe implements INBTSerializable<NBTTagCompound>
{
    public String title = "";
    public NonNullList<ItemStack> input = NonNullList.create();
    public NonNullList<ItemStack> output = NonNullList.create();

    /* TODO: expression and trigger */

    public boolean craft(EntityPlayer player)
    {
        if (!this.isPlayerHasAllItems(player))
        {
            return false;
        }

        for (ItemStack stack : this.input)
        {
            player.inventory.clearMatchingItems(stack.getItem(), -1, stack.getCount(), null);
        }

        for (ItemStack stack : this.output)
        {
            this.addOrDrop(player, stack.copy());
        }

        return true;
    }

    public boolean isPlayerHasAllItems(EntityPlayer player)
    {
        for (ItemStack stack : this.input)
        {
            if (InventoryUtils.countItems(player, stack) < stack.getCount())
            {
                return false;
            }
        }

        return true;
    }

    private void addOrDrop(EntityPlayer player, ItemStack stack)
    {
        boolean flag = player.inventory.addItemStackToInventory(stack);

        if (flag)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, 1F);
            player.inventoryContainer.detectAndSendChanges();
        }
        else
        {
            EntityItem entityitem = player.dropItem(stack, false);

            if (entityitem != null)
            {
                entityitem.setNoPickupDelay();
                entityitem.setOwner(player.getName());
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList input = this.serializeList(this.input);
        NBTTagList output = this.serializeList(this.output);

        if (!this.title.isEmpty())
        {
            tag.setString("Title", this.title);
        }

        if (input != null)
        {
            tag.setTag("Input", input);
        }

        if (output != null)
        {
            tag.setTag("Output", output);
        }

        return tag;
    }

    private NBTTagList serializeList(NonNullList<ItemStack> list)
    {
        NBTTagList tagList = new NBTTagList();

        for (ItemStack stack : list)
        {
            if (!stack.isEmpty())
            {
                tagList.appendTag(stack.serializeNBT());
            }
        }

        return tagList.tagCount() == 0 ? null : tagList;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Title"))
        {
            this.title = tag.getString("Title");
        }

        if (tag.hasKey("Input"))
        {
            this.deserializeList(this.input, tag.getTagList("Input", Constants.NBT.TAG_COMPOUND));
        }

        if (tag.hasKey("Output"))
        {
            this.deserializeList(this.output, tag.getTagList("Output", Constants.NBT.TAG_COMPOUND));
        }
    }

    private void deserializeList(NonNullList<ItemStack> list, NBTTagList tagList)
    {
        list.clear();

        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound compound = tagList.getCompoundTagAt(i);
            ItemStack stack = new ItemStack(compound);

            if (!stack.isEmpty())
            {
                list.add(stack);
            }
        }
    }
}