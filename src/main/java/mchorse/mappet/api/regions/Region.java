package mchorse.mappet.api.regions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.IShape;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Region implements INBTSerializable<NBTTagCompound>
{
    public Trigger onEnter = new Trigger("entity.item.pickup", "");
    public Trigger onExit = new Trigger("ui.button.click", "test");
    public String enabled = "state(\"i\") == 12";
    public IShape shape = new BoxShape();

    public void setInitialPos(BlockPos pos)
    {
        this.shape.setInitialPos(pos);
    }

    public boolean isEnabled()
    {
        if (this.enabled == null || this.enabled.isEmpty())
        {
            return true;
        }

        IValue value = Mappet.expressions.evalute(this.enabled, null);

        return value == null || value.booleanValue();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("OnEnter", this.onEnter.serializeNBT());
        tag.setTag("OnExit", this.onExit.serializeNBT());
        tag.setString("Enabled", this.enabled);

        NBTTagCompound shape = this.shape.serializeNBT();

        shape.setString("Type", this.shape.getType());
        tag.setTag("Shape", shape);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("OnEnter", Constants.NBT.TAG_COMPOUND))
        {
            this.onEnter.deserializeNBT(tag.getCompoundTag("OnEnter"));
        }

        if (tag.hasKey("OnExit", Constants.NBT.TAG_COMPOUND))
        {
            this.onExit.deserializeNBT(tag.getCompoundTag("OnExit"));
        }

        if (tag.hasKey("Enabled", Constants.NBT.TAG_STRING))
        {
            this.enabled = tag.getString("Enabled");
        }

        if (tag.hasKey("Shape", Constants.NBT.TAG_COMPOUND))
        {
            NBTTagCompound shapeTag = tag.getCompoundTag("Shape");

            if (shapeTag.hasKey("Type"))
            {
                IShape shape = IShape.fromString(shapeTag.getString("Type"));

                if (shape != null)
                {
                    shape.deserializeNBT(shapeTag);

                    this.shape = shape;
                }
            }
        }
    }
}