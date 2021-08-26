package mchorse.mappet.api.regions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;

public class Region implements INBTSerializable<NBTTagCompound>
{
    public boolean passable = true;
    public Checker enabled = new Checker(true);
    public int delay;
    public int update = 3;
    public Trigger onEnter = new Trigger();
    public Trigger onExit = new Trigger();
    public List<AbstractShape> shapes = new ArrayList<AbstractShape>();

    public Region()
    {
        this.shapes.add(new BoxShape());
    }

    /* Automatic state writing */
    public boolean writeState;
    public String state = "";
    public TargetMode target = TargetMode.GLOBAL;
    public boolean additive = true;

    public boolean isEnabled(EntityPlayer player)
    {
        return this.enabled.check(new DataContext(player));
    }

    public boolean isPlayerInside(EntityPlayer player, BlockPos pos)
    {
        for (AbstractShape shape : this.shapes)
        {
            if (shape.isPlayerInside(player, pos))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isPlayerInside(double x, double y, double z, BlockPos pos)
    {
        for (AbstractShape shape : this.shapes)
        {
            if (shape.isPlayerInside(x, y, z, pos))
            {
                return true;
            }
        }

        return false;
    }

    public void triggerEnter(EntityPlayer player, BlockPos pos)
    {
        if (this.writeState && !this.state.isEmpty())
        {
            States states = this.target == TargetMode.GLOBAL ? Mappet.states : EntityUtils.getStates(player);

            if (this.additive)
            {
                states.add(this.state, 1);
            }
            else
            {
                states.setNumber(this.state, 1);
            }
        }

        this.onEnter.trigger(new DataContext(player)
            .set("x", pos.getX())
            .set("y", pos.getY())
            .set("z", pos.getZ()));
    }

    public void triggerExit(EntityPlayer player, BlockPos pos)
    {
        if (this.writeState && !this.state.isEmpty())
        {
            States states = this.target == TargetMode.GLOBAL ? Mappet.states : EntityUtils.getStates(player);

            if (!this.additive)
            {
                states.reset(this.state);
            }
        }

        this.onExit.trigger(new DataContext(player)
            .set("x", pos.getX())
            .set("y", pos.getY())
            .set("z", pos.getZ()));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("Passable", this.passable);
        tag.setTag("Enabled", this.enabled.serializeNBT());
        tag.setInteger("Delay", this.delay);
        tag.setInteger("Update", this.update);
        tag.setTag("OnEnter", this.onEnter.serializeNBT());
        tag.setTag("OnExit", this.onExit.serializeNBT());

        NBTTagList shapes = new NBTTagList();

        for (AbstractShape shape : this.shapes)
        {
            NBTTagCompound shapeTag = shape.serializeNBT();

            shapeTag.setString("Type", shape.getType());
            shapes.appendTag(shapeTag);
        }

        tag.setTag("Shapes", shapes);
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

        if (tag.hasKey("Update", Constants.NBT.TAG_ANY_NUMERIC))
        {
            this.update = tag.getInteger("Update");
        }

        if (tag.hasKey("OnEnter", Constants.NBT.TAG_COMPOUND))
        {
            this.onEnter.deserializeNBT(tag.getCompoundTag("OnEnter"));
        }

        if (tag.hasKey("OnExit", Constants.NBT.TAG_COMPOUND))
        {
            this.onExit.deserializeNBT(tag.getCompoundTag("OnExit"));
        }

        this.shapes.clear();

        if (tag.hasKey("Shape", Constants.NBT.TAG_COMPOUND))
        {
            AbstractShape shape = this.readShape(tag.getCompoundTag("Shape"));

            if (shape != null)
            {
                this.shapes.add(shape);
            }
        }
        else if (tag.hasKey("Shapes", Constants.NBT.TAG_LIST))
        {
            NBTTagList list = tag.getTagList("Shapes", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < list.tagCount(); i++)
            {
                AbstractShape shape = this.readShape(list.getCompoundTagAt(i));

                if (shape != null)
                {
                    this.shapes.add(shape);
                }
            }
        }

        if (this.shapes.isEmpty())
        {
            this.shapes.add(new BoxShape());
        }

        this.writeState = tag.getBoolean("WriteState");
        this.state = tag.getString("State");
        this.target = EnumUtils.getValue(tag.getInteger("Target"), TargetMode.values(), TargetMode.GLOBAL);
        this.additive = tag.getBoolean("Additive");
    }

    private AbstractShape readShape(NBTTagCompound shapeTag)
    {
        if (shapeTag.hasKey("Type"))
        {
            AbstractShape shape = AbstractShape.fromString(shapeTag.getString("Type"));

            if (shape != null)
            {
                shape.deserializeNBT(shapeTag);

                return shape;
            }
        }

        return null;
    }
}