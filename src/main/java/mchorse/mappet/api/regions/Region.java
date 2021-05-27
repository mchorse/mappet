package mchorse.mappet.api.regions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
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

    /* Automatic state writing */
    public boolean writeState;
    public String state = "";
    public Target target = Target.GLOBAL;
    public boolean additive = true;

    public boolean isEnabled(World world, BlockPos pos)
    {
        return this.enabled.check(new DataContext(world, pos));
    }

    public void triggerEnter(EntityPlayer player)
    {
        if (this.writeState && !this.state.isEmpty())
        {
            States states = this.target == Target.GLOBAL ? Mappet.states : WorldUtils.getStates(player);

            if (this.additive)
            {
                states.add(this.state, 1);
            }
            else
            {
                states.set(this.state, 1);
            }
        }

        this.onEnter.trigger(player);
    }

    public void triggerExit(EntityPlayer player)
    {
        if (this.writeState && !this.state.isEmpty())
        {
            States states = this.target == Target.GLOBAL ? Mappet.states : WorldUtils.getStates(player);

            if (!this.additive)
            {
                states.reset(this.state);
            }
        }

        this.onEnter.trigger(player);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("Passable", this.passable);
        tag.setTag("Enabled", this.enabled.serializeNBT());
        tag.setInteger("Delay", this.delay);
        tag.setTag("OnEnter", this.onEnter.serializeNBT());
        tag.setTag("OnExit", this.onExit.serializeNBT());

        NBTTagCompound shape = this.shape.serializeNBT();

        shape.setString("Type", this.shape.getType());
        tag.setTag("Shape", shape);

        tag.setBoolean("WriteState", this.writeState);
        tag.setString("State", this.state.trim());
        tag.setInteger("Target", this.target.ordinal());
        tag.setBoolean("Additive", this.additive);

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
            this.enabled.deserializeNBT(tag.getTag("Enabled"));
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

        this.writeState = tag.getBoolean("WriteState");
        this.state = tag.getString("State");
        this.target = EnumUtils.getValue(tag.getInteger("Target"), Target.values(), Target.GLOBAL);
        this.additive = tag.getBoolean("Additive");
    }
}