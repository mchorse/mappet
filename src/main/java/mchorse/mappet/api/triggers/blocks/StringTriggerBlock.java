package mchorse.mappet.api.triggers.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class StringTriggerBlock extends AbstractTriggerBlock
{
    public String string = "";

    public StringTriggerBlock()
    {}

    public StringTriggerBlock(String string)
    {
        this.string = string;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        if (this.string.isEmpty())
        {
            return super.stringify();
        }

        return this.string;
    }

    @Override
    public boolean isEmpty()
    {
        return this.string.isEmpty();
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString(this.getKey(), this.string);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.string = tag.getString(this.getKey());
    }

    protected abstract String getKey();
}