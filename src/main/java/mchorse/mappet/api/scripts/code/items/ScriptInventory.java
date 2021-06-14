package mchorse.mappet.api.scripts.code.items;

import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityLockableLoot;

public class ScriptInventory implements IScriptInventory
{
    private IInventory inventory;

    public ScriptInventory(IInventory inventory)
    {
        this.inventory = inventory;
    }

    @Override
    public IInventory getMinecraftInventory()
    {
        return this.inventory;
    }

    @Override
    public boolean isEmpty()
    {
        return this.inventory.isEmpty();
    }

    @Override
    public int size()
    {
        return this.inventory.getSizeInventory();
    }

    @Override
    public IScriptItemStack getStack(int index)
    {
        ItemStack stack = this.inventory.getStackInSlot(index);

        return stack.isEmpty() ? ScriptItemStack.EMPTY : new ScriptItemStack(stack);
    }

    @Override
    public IScriptItemStack removeStack(int index)
    {
        ItemStack stack = this.inventory.removeStackFromSlot(index);

        return stack.isEmpty() ? ScriptItemStack.EMPTY : new ScriptItemStack(stack);
    }

    @Override
    public void setStack(int index, IScriptItemStack stack)
    {
        if (stack == null)
        {
            stack = ScriptItemStack.EMPTY;
        }

        if (index >= 0 && index < this.size())
        {
            this.inventory.setInventorySlotContents(index, stack.getMinecraftItemStack());
        }
    }

    /* Basic inventory */

    @Override
    public String getName()
    {
        return this.inventory.getName();
    }

    @Override
    public boolean hasCustomName()
    {
        return this.inventory.hasCustomName();
    }

    @Override
    public void setName(String name)
    {
        if (this.inventory instanceof InventoryBasic)
        {
            ((InventoryBasic) this.inventory).setCustomName(name);
        }
        else if (this.inventory instanceof TileEntityLockableLoot)
        {
            ((TileEntityLockableLoot) this.inventory).setCustomName(name);
        }
    }
}