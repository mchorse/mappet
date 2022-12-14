package mchorse.mappet.api.scripts.code.items;

import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.items.IScriptItem;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScriptItemStack implements IScriptItemStack
{
    public static final ScriptItemStack EMPTY = new ScriptItemStack(ItemStack.EMPTY);

    private static final String CAN_DESTROY = "CanDestroy";
    private static final String CAN_PLACE_ON = "CanPlaceOn";

    private ItemStack stack;
    private IScriptItem item;

    public static IScriptItemStack create(ItemStack stack)
    {
        if (stack == null || stack.isEmpty())
        {
            return EMPTY;
        }

        return new ScriptItemStack(stack);
    }

    private ScriptItemStack(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public ItemStack getMinecraftItemStack()
    {
        return this.stack;
    }

    @Override
    public boolean isEmpty()
    {
        return this.stack.isEmpty();
    }

    @Override
    public IScriptItemStack copy()
    {
        return new ScriptItemStack(this.getMinecraftItemStack().copy());
    }

    @Override
    public IScriptItem getItem()
    {
        if (this.item == null)
        {
            this.item = new ScriptItem(this.stack.getItem());
        }

        return this.item;
    }

    @Override
    public int getMaxCount()
    {
        return this.stack.getMaxStackSize();
    }

    @Override
    public int getCount()
    {
        return this.stack.getCount();
    }

    @Override
    public void setCount(int count)
    {
        this.stack.setCount(count);
    }

    @Override
    public int getMeta()
    {
        return this.stack.getMetadata();
    }

    @Override
    public void setMeta(int meta)
    {
        this.stack.setItemDamage(meta);
    }

    @Override
    public boolean hasData()
    {
        return this.stack.hasTagCompound();
    }

    @Override
    public INBTCompound getData()
    {
        return new ScriptNBTCompound(this.stack.getTagCompound());
    }

    @Override
    public void setData(INBTCompound tag)
    {
        this.stack.setTagCompound(tag.getNBTTagCompound());
    }

    @Override
    public INBTCompound serialize()
    {
        return new ScriptNBTCompound(this.stack.serializeNBT());
    }

    @Override
    public String getDisplayName()
    {
        return this.stack.getDisplayName();
    }

    @Override
    public void setDisplayName(String name)
    {
        this.stack.setStackDisplayName(name);
    }

    private NBTTagList getLoreNBTList()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null && tag.hasKey("display", Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound display = tag.getCompoundTag("display");

            if (!display.hasKey("Lore", Constants.NBT.TAG_LIST))
            {
                display.setTag("Lore", new NBTTagList());
            }

            return display.getTagList("Lore", Constants.NBT.TAG_STRING);
        }

        return null;
    }

    @Override
    public String getLore(int index)
    {
        NBTTagList list = this.getLoreNBTList();

        if (list != null && index < list.tagCount())
        {
            return list.getStringTagAt(index);
        }

        throw new IllegalStateException("Lore index out of bounds, or no lore exists.");
    }

    @Override
    public List<String> getLoreList()
    {
        NBTTagList lore = this.getLoreNBTList();

        if (lore == null)
        {
            return Collections.emptyList();
        }

        List<String> loreList = new ArrayList<String>();

        for (int i = 0; i < lore.tagCount(); i++)
        {
            loreList.add(lore.getStringTagAt(i));
        }

        return loreList;
    }

    @Override
    public void setLore(int index, String string)
    {
        NBTTagList lore = this.getLoreNBTList();

        if (lore != null && index >= 0 && index < lore.tagCount())
        {
            lore.set(index, new NBTTagString(string));
        }
        else
        {
            throw new IllegalStateException("Lore index out of bounds, or no lore exists.");
        }
    }

    @Override
    public void addLore(String string)
    {
        NBTTagList lore = this.getLoreNBTList();

        if (lore != null)
        {
            lore.appendTag(new NBTTagString(string));
        }
    }

    @Override
    public void clearAllLores()
    {
        NBTTagList lore = this.getLoreNBTList();

        if (lore != null)
        {
            while (lore.tagCount() > 0)
            {
                lore.removeTag(lore.tagCount() - 1);
            }
        }
    }

    @Override
    public void clearLore(int index)
    {
        NBTTagList lore = this.getLoreNBTList();

        if (lore != null && index >= 0 && index < lore.tagCount())
        {
            lore.removeTag(index);
        }
        else
        {
            throw new IllegalStateException("Lore index out of bounds, or no lore exists.");
        }
    }

    @Override
    public void clearAllEnchantments()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            tag.removeTag("ench");
        }
    }

    @Override
    public List<String> getCanDestroyBlocks()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag == null || !tag.hasKey(CAN_DESTROY, Constants.NBT.TAG_LIST))
        {
            return Collections.emptyList();
        }

        List<String> canDestroyBlocks = new ArrayList<String>();
        NBTTagList list = tag.getTagList(CAN_DESTROY, Constants.NBT.TAG_STRING);

        for (int i = 0; i < list.tagCount(); i++)
        {
            canDestroyBlocks.add(list.getStringTagAt(i));
        }

        return canDestroyBlocks;
    }

    @Override
    public void addCanDestroyBlock(String block)
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag == null)
        {
            this.stack.setTagCompound(new NBTTagCompound());
        }

        if (!tag.hasKey(CAN_DESTROY, Constants.NBT.TAG_LIST))
        {
            tag.setTag(CAN_DESTROY, new NBTTagList());
        }

        tag.getTagList(CAN_DESTROY, Constants.NBT.TAG_STRING).appendTag(new NBTTagString(block));
    }

    @Override
    public void clearAllCanDestroyBlocks()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            tag.removeTag(CAN_DESTROY);
        }
    }

    @Override
    public void clearCanDestroyBlock(String block)
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            NBTTagList canPlaceOn = tag.getTagList(CAN_DESTROY, Constants.NBT.TAG_STRING);
            NBTTagList newCanPlaceOn = new NBTTagList();

            for (int i = 0; i < canPlaceOn.tagCount(); i++)
            {
                if (!canPlaceOn.getStringTagAt(i).equals(block))
                {
                    newCanPlaceOn.appendTag(canPlaceOn.get(i));
                }
            }

            tag.setTag(CAN_DESTROY, newCanPlaceOn);
        }
    }

    @Override
    public List<String> getCanPlaceOnBlocks()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag == null || !tag.hasKey(CAN_PLACE_ON, Constants.NBT.TAG_LIST))
        {
            return Collections.emptyList();
        }

        List<String> canPlaceOn = new ArrayList<String>();
        NBTTagList list = tag.getTagList(CAN_PLACE_ON, Constants.NBT.TAG_STRING);

        for (int i = 0; i < list.tagCount(); i++)
        {
            canPlaceOn.add(list.getStringTagAt(i));
        }

        return canPlaceOn;
    }

    @Override
    public void addCanPlaceOnBlock(String block)
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag == null)
        {
            this.stack.setTagCompound(new NBTTagCompound());
        }

        if (!tag.hasKey(CAN_PLACE_ON, Constants.NBT.TAG_LIST))
        {
            tag.setTag(CAN_PLACE_ON, new NBTTagList());
        }

        tag.getTagList(CAN_PLACE_ON, Constants.NBT.TAG_STRING).appendTag(new NBTTagString(block));
    }

    @Override
    public void clearAllCanPlaceOnBlocks()
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            tag.removeTag(CAN_PLACE_ON);
        }
    }

    @Override
    public void clearCanPlaceOnBlock(String block)
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            NBTTagList canPlaceOn = tag.getTagList(CAN_PLACE_ON, Constants.NBT.TAG_STRING);
            NBTTagList newCanPlaceOn = new NBTTagList();

            for (int i = 0; i < canPlaceOn.tagCount(); i++)
            {
                if (!canPlaceOn.getStringTagAt(i).equals(block))
                {
                    newCanPlaceOn.appendTag(canPlaceOn.get(i));
                }
            }

            tag.setTag(CAN_PLACE_ON, newCanPlaceOn);
        }
    }

    @Override
    public int getRepairCost()
    {
        return this.stack.getRepairCost();
    }

    @Override
    public void setRepairCost(int cost)
    {
        this.stack.setRepairCost(cost);
    }

    @Override
    public boolean isUnbreakable()
    {
        return !this.stack.isItemStackDamageable();
    }

    @Override
    public void setUnbreakable(boolean unbreakable)
    {
        NBTTagCompound tag = this.stack.getTagCompound();

        if (tag != null)
        {
            tag.setBoolean("Unbreakable", unbreakable);
        }
    }
}