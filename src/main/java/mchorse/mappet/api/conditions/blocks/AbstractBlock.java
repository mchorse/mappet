package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.api.utils.factory.MapFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractBlock implements INBTSerializable<NBTTagCompound>
{
    public static final IFactory<AbstractBlock> FACTORY = new MapFactory<AbstractBlock>()
        .register("quest", QuestBlock.class)
        .register("state", StateBlock.class)
        .register("dialogue", DialogueBlock.class)
        .register("faction", FactionBlock.class)
        .register("world_time", WorldTimeBlock.class);

    public boolean not;
    public boolean or;

    public abstract int getColor();

    public abstract boolean evaluate(DataContext context);

    @SideOnly(Side.CLIENT)
    public abstract String stringify();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("Not", this.not);
        tag.setBoolean("Or", this.or);

        this.serializeNBT(tag);

        return tag;
    }

    public abstract void serializeNBT(NBTTagCompound tag);

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.not = tag.getBoolean("Not");
        this.or = tag.getBoolean("Or");
    }
}