package mchorse.mappet.api.ui.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class UIKeybind implements INBTSerializable<NBTTagCompound>
{
    public int keyCode;
    public String action;
    public String label;
    public int modifier;

    public static int createModifier(boolean shift, boolean ctrl, boolean alt)
    {
        int modifier = shift ? 1 : 0;

        modifier += (ctrl ? 1 : 0) << 1;
        modifier += (alt ? 1 : 0) << 2;

        return modifier;
    }

    public UIKeybind()
    {}

    public UIKeybind(int keyCode, String action, String label, int modifier)
    {
        this.keyCode = keyCode;
        this.action = action;
        this.label = label;
        this.modifier = modifier;
    }

    public boolean isShift()
    {
        return (this.modifier & 0b1) == 1;
    }

    public boolean isCtrl()
    {
        return ((this.modifier >> 1) & 0b1) == 1;
    }

    public boolean isAlt()
    {
        return ((this.modifier >> 2) & 0b1) == 1;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("KeyCode", this.keyCode);
        tag.setString("Action", this.action);
        tag.setString("Label", this.label);
        tag.setInteger("Modifier", this.modifier);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.keyCode = tag.getInteger("KeyCode");
        this.action = tag.getString("Action");
        this.label = tag.getString("Label");
        this.modifier = tag.getInteger("Modifier");
    }
}