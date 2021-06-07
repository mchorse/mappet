package mchorse.mappet.api.scripts.code.items;

import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.items.IScriptItem;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.item.ItemStack;

public class ScriptItemStack implements IScriptItemStack
{
    public static final ScriptItemStack EMPTY = new ScriptItemStack(ItemStack.EMPTY);

    private ItemStack stack;
    private IScriptItem item;

    public ScriptItemStack(ItemStack stack)
    {
        this.stack = stack == null ? ItemStack.EMPTY : stack;
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
    public IScriptItem getItem()
    {
        if (this.item == null)
        {
            this.item = new ScriptItem(this.stack.getItem());
        }

        return this.item;
    }

    @Override
    public int getCount()
    {
        return this.stack.getCount();
    }

    @Override
    public int getMeta()
    {
        return this.stack.getMetadata();
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
        this.stack.setTagCompound(((ScriptNBTCompound) tag).getNBTCompound());
    }

    @Override
    public INBTCompound serialize()
    {
        return new ScriptNBTCompound(this.stack.serializeNBT());
    }
}