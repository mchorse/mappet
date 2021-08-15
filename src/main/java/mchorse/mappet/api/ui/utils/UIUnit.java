package mchorse.mappet.api.ui.utils;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.utils.resizers.Flex;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIUnit implements INBTSerializable<NBTTagCompound>
{
    public float value;
    public int offset;
    public int max;
    public float anchor;
    public String target = "";
    public float targetAnchor;

    @SideOnly(Side.CLIENT)
    public void apply(Flex.Unit unit, UIContext context)
    {
        GuiElement target = context.getElement(this.target);

        if (this.value != 0)
        {
            unit.set(this.value, Flex.Measure.RELATIVE);
            unit.offset = this.offset;
        }
        else
        {
            unit.set(this.offset, Flex.Measure.PIXELS);
        }

        unit.max = this.max;
        unit.anchor = this.anchor;
        unit.target = target == null ? null : target.flex();
        unit.targetAnchor = this.targetAnchor;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setFloat("Value", this.value);
        tag.setInteger("Offset", this.offset);
        tag.setFloat("Anchor", this.anchor);
        tag.setString("Target", this.target);
        tag.setFloat("TagetAnchor", this.targetAnchor);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.value = tag.getFloat("Value");
        this.offset = tag.getInteger("Offset");
        this.anchor = tag.getFloat("Anchor");
        this.target = tag.getString("Target");
        this.targetAnchor = tag.getFloat("TagetAnchor");
    }
}