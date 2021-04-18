package mchorse.mappet.api.npcs;

import mchorse.mclib.utils.MathUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class NpcDrop implements INBTSerializable<NBTTagCompound>
{
    public ItemStack stack = ItemStack.EMPTY;
    public float chance = 1F;

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Stack", this.stack.writeToNBT(new NBTTagCompound()));
        tag.setFloat("Chance", this.chance);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.stack = new ItemStack(tag.getCompoundTag("Stack"));
        this.chance = MathUtils.clamp(tag.getFloat("Chance"), 0, 1);
    }
}
