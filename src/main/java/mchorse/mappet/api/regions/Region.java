package mchorse.mappet.api.regions;

import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.Trigger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Region implements INBTSerializable<NBTTagCompound>
{
    public boolean passable = true;
    public Checker enabled = new Checker(true);
    public int delay;
    public Trigger onEnter = new Trigger();
    public Trigger onExit = new Trigger();
    public AbstractShape shape = new BoxShape();

    public boolean isEnabled(World world)
    {
        return this.enabled.check(new DataContext(world));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (!this.passable)
        {
            tag.setBoolean("Passable", this.passable);
        }

        tag.setTag("Enabled", this.enabled.serializeNBT());

        if (this.delay > 0)
        {
            tag.setInteger("Delay", this.delay);
        }

        tag.setTag("OnEnter", this.onEnter.serializeNBT());
        tag.setTag("OnExit", this.onExit.serializeNBT());

        NBTTagCompound shape = this.shape.serializeNBT();

        shape.setString("Type", this.shape.getType());
        tag.setTag("Shape", shape);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Passable"))
        {
            this.passable = tag.getBoolean("Passable");
        }

        if (tag.hasKey("Enabled", Constants.NBT.TAG_COMPOUND))
        {
            this.enabled.deserializeNBT(tag.getCompoundTag("Enabled"));
        }

        if (tag.hasKey("Delay", Constants.NBT.TAG_ANY_NUMERIC))
        {
            this.delay = tag.getInteger("Delay");
        }

        if (tag.hasKey("OnEnter", Constants.NBT.TAG_COMPOUND))
        {
            this.onEnter.deserializeNBT(tag.getCompoundTag("OnEnter"));
        }

        if (tag.hasKey("OnExit", Constants.NBT.TAG_COMPOUND))
        {
            this.onExit.deserializeNBT(tag.getCompoundTag("OnExit"));
        }

        if (tag.hasKey("Shape", Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound shapeTag = tag.getCompoundTag("Shape");

            if (shapeTag.hasKey("Type"))
            {
                AbstractShape shape = AbstractShape.fromString(shapeTag.getString("Type"));

                if (shape != null)
                {
                    shape.deserializeNBT(shapeTag);

                    this.shape = shape;
                }
            }
        }
    }
}