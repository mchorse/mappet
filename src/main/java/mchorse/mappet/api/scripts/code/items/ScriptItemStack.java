package mchorse.mappet.api.scripts.code.items;

import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.items.IScriptItem;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScriptItemStack implements IScriptItemStack
{
    public static final ScriptItemStack EMPTY = new ScriptItemStack(ItemStack.EMPTY);

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

    @Override
    public String getLore(int index)
    {
        if (this.serialize().getCompound("tag").getCompound("display").getList("Lore").size() > index)
        {
            return this.serialize().getCompound("tag").getCompound("display").getList("Lore").getString(index);
        } else {
            throw new IllegalStateException("Lore index out of bounds, or no lore exists.");
        }
    }

    @Override
    public List<String> getLoreList()
    {
        List<String> loreList = new ArrayList<String>();
        for (int i = 0; i < this.serialize().getCompound("tag").getCompound("display").getList("Lore").size(); i++)
        {
            loreList.add(this.serialize().getCompound("tag").getCompound("display").getList("Lore").getString(i));
        }
        return loreList;
    }

    @Override
    public void setLore(int index, String lore)
    {
        this.serialize().getCompound("tag").getCompound("display").getList("Lore").setString(index, lore);
    }

    @Override
    public void addLore(String lore)
    {
        this.serialize().getCompound("tag").getCompound("display").getList("Lore").addString(lore);
    }

    @Override
    public void clearAllLores()
    {
        for (int i = this.serialize().getCompound("tag").getCompound("display").getList("Lore").size() - 1; i >= 0; i--)
        {
            this.serialize().getCompound("tag").getCompound("display").getList("Lore").remove(i);
        }
    }

    @Override
    public void clearLore(int index)
    {
        this.serialize().getCompound("tag").getCompound("display").getList("Lore").remove(index);
    }

    @Override
    public void clearAllEnchantments()
    {
        for (int i = this.serialize().getCompound("tag").getList("ench").size() - 1; i >= 0; i--)
        {
            this.serialize().getCompound("tag").getList("ench").remove(i);
        }
    }

    @Override
    public List<String> getCanDestroyBlocks()
    {
        List<String> canDestroyBlocks = new ArrayList<String>();
        for (int i = 0; i < this.serialize().getCompound("tag").getList("CanDestroy").size(); i++)
        {
            canDestroyBlocks.add(this.serialize().getCompound("tag").getList("CanDestroy").getString(i));
        }
        return canDestroyBlocks;
    }

    @Override
    public void addCanDestroyBlock(String block)
    {
        this.serialize().getCompound("tag").getList("CanDestroy").addString(block);
    }

    @Override
    public void clearAllCanDestroyBlocks()
    {
        for (int i = this.serialize().getCompound("tag").getList("CanDestroy").size() - 1; i >= 0; i--)
        {
            this.serialize().getCompound("tag").getList("CanDestroy").remove(i);
        }
    }

    @Override
    public void clearCanDestroyBlock(String block)
    {
        for (int i = 0; i < this.serialize().getCompound("tag").getList("CanDestroy").size(); i++)
        {
            if (this.serialize().getCompound("tag").getList("CanDestroy").getString(i).equals(block))
            {
                this.serialize().getCompound("tag").getList("CanDestroy").remove(i);
            }
        }
    }

    @Override
    public List<String> getCanPlaceOnBlocks()
    {
        List<String> canPlaceOnBlocks = new ArrayList<String>();
        for (int i = this.serialize().getCompound("tag").getList("CanPlaceOn").size() - 1; i >= 0; i--)
        {
            canPlaceOnBlocks.add(this.serialize().getCompound("tag").getList("CanPlaceOn").getString(i));
        }
        return canPlaceOnBlocks;
    }

    @Override
    public void addCanPlaceOnBlock(String block)
    {
        this.serialize().getCompound("tag").getList("CanPlaceOn").addString(block);
    }

    @Override
    public void clearAllCanPlaceOnBlocks()
    {
        for (int i = this.serialize().getCompound("tag").getList("CanPlaceOn").size() - 1; i >= 0; i--)
        {
            this.serialize().getCompound("tag").getList("CanPlaceOn").remove(i);
        }
    }

    @Override
    public void clearCanPlaceOnBlock(String block)
    {
        for (int i = this.serialize().getCompound("tag").getList("CanPlaceOn").size() - 1; i >= 0; i--)
        {
            if (this.serialize().getCompound("tag").getList("CanPlaceOn").getString(i).equals(block))
            {
                this.serialize().getCompound("tag").getList("CanPlaceOn").remove(i);
            }
        }
    }

    @Override
    public int getRepairCost()
    {
        return this.serialize().getCompound("tag").getInt("RepairCost");
    }

    @Override
    public void setRepairCost(int cost)
    {
        this.serialize().getCompound("tag").setInt("RepairCost", cost);
    }

    @Override
    public boolean isUnbreakable()
    {
        return this.serialize().getCompound("tag").getBoolean("Unbreakable");
    }

    @Override
    public void setUnbreakable(boolean unbreakable)
    {
        this.serialize().getCompound("tag").setBoolean("Unbreakable", unbreakable);
    }
}