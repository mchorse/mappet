package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UIToggleComponent extends UILabelBaseComponent
{
    public boolean state;

    public UIToggleComponent state(boolean state)
    {
        this.state = state;

        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiToggleElement toggle = new GuiToggleElement(mc, IKey.str(this.label), (b) ->
        {
            if (!this.id.isEmpty())
            {
                context.data.setBoolean(this.id, b.isToggled());
                context.dirty(this.id, this.updateDelay);
            }
        });

        toggle.toggled(this.state);

        return this.apply(toggle, context);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setBoolean("State", this.state);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.state = tag.getBoolean("State");
    }
}