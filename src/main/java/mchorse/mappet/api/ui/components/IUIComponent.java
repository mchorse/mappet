package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IUIComponent extends INBTSerializable<NBTTagCompound>
{
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context);

    public List<IUIComponent> getChildComponents();

    @Override
    public default NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        this.serializeNBT(tag);

        return tag;
    }

    public void serializeNBT(NBTTagCompound tag);
}