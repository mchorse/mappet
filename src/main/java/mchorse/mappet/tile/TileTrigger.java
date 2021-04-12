package mchorse.mappet.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileTrigger extends TileEntity
{
    public String leftClick = "test";
    public String rightClick = "test2";

    public void set(String left, String right)
    {
        this.leftClick = left;
        this.rightClick = right;

        this.markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        if (!this.leftClick.isEmpty())
        {
            tag.setString("Left", this.leftClick);
        }

        if (!this.rightClick.isEmpty())
        {
            tag.setString("Right", this.rightClick);
        }

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Left"))
        {
            this.leftClick = tag.getString("Left");
        }

        if (tag.hasKey("Right"))
        {
            this.rightClick = tag.getString("Right");
        }
    }
}