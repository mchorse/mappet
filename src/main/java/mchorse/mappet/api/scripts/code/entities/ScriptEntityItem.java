package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.api.scripts.code.items.ScriptItemStack;
import mchorse.mappet.api.scripts.user.entities.IScriptEntityItem;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;

public class ScriptEntityItem extends ScriptEntity<EntityItem> implements IScriptEntityItem
{

    public ScriptEntityItem(EntityItem entity)
    {
        super(entity);
    }

    @Override
    public int getAge()
    {
        return this.entity.age;
    }

    @Override
    public void setAge(int age)
    {
        this.entity.age = age;
    }

    @Override
    public int getPickupDelay()
    {
        return this.entity.pickupDelay;
    }

    @Override
    public void setPickupDelay(int delay)
    {
        this.entity.pickupDelay = delay;
    }

    @Override
    public int getLifespan()
    {
        return this.entity.lifespan;
    }

    @Override
    public void setLifespan(int lifespan)
    {
        this.entity.lifespan = lifespan;
    }

    @Override
    public String getOwner()
    {
        return this.entity.getOwner();
    }

    @Override
    public void setOwner(String owner)
    {
        this.entity.setOwner(owner);
    }

    @Override
    public String getThrower()
    {
        return this.entity.getThrower();
    }

    @Override
    public void setThrower(String thrower)
    {
        this.entity.setThrower(thrower);
    }

    @Override
    public IScriptItemStack getItem()
    {
        return ScriptItemStack.create(this.entity.getItem());
    }

    @Override
    public void setItem(IScriptItemStack itemStack)
    {
        this.entity.setItem(itemStack == null ? ItemStack.EMPTY : itemStack.getMinecraftItemStack());
    }

    @Override
    public void setInfinitePickupDelay()
    {
        this.entity.setInfinitePickupDelay();
    }

    @Override
    public void setDefaultPickupDelay()
    {
        this.entity.setDefaultPickupDelay();
    }

    @Override
    public void setNoDespawn()
    {
        this.entity.setNoDespawn();
    }

    @Override
    public boolean canPickup()
    {
        return !this.entity.cannotPickup();
    }
}
