package mchorse.mappet.tile;

import mchorse.mappet.api.utils.Trigger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileTrigger extends TileEntity
{
    public Trigger leftClick = new Trigger();
    public Trigger rightClick = new Trigger();

    public void set(NBTTagCompound left, NBTTagCompound right)
    {
        this.leftClick.deserializeNBT(left);
        this.rightClick.deserializeNBT(right);

        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Left", this.leftClick.serializeNBT());
        tag.setTag("Right", this.rightClick.serializeNBT());

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Left"))
        {
            this.leftClick.deserializeNBT(tag.getCompoundTag("Left"));
        }

        if (tag.hasKey("Right"))
        {
            this.rightClick.deserializeNBT(tag.getCompoundTag("Right"));
        }
    }
}