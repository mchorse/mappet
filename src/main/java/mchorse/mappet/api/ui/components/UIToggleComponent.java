package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mappet.client.gui.utils.text.GuiText;
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
        this.change("State");

        this.state = state;

        return this;
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    protected void applyProperty(UIContext context, String key, GuiElement element)
    {
        super.applyProperty(context, key, element);

        GuiToggleElement toggle = (GuiToggleElement) element;

        if (key.equals("Label"))
        {
            toggle.label = IKey.str(this.getLabel());
        }
        else if (key.equals("State"))
        {
            toggle.toggled(this.state);
        }
    }

    @Override
    @DiscardMethod
    @SideOnly(Side.CLIENT)
    public GuiElement create(Minecraft mc, UIContext context)
    {
        GuiToggleElement toggle = new GuiToggleElement(mc, IKey.str(this.getLabel()), (b) ->
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
    @DiscardMethod
    public void populateData(NBTTagCompound tag)
    {
        super.populateData(tag);

        if (!this.id.isEmpty())
        {
            tag.setBoolean(this.id, this.state);
        }
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setBoolean("State", this.state);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("State"))
        {
            this.state = tag.getBoolean("State");
        }
    }
}