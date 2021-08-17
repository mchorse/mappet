package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.Target;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.metamorph.api.MorphAPI;
import mchorse.metamorph.api.MorphManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MorphTriggerBlock extends AbstractTriggerBlock
{
    public Target target = new Target(TargetMode.SUBJECT);
    public NBTTagCompound morph;

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        if (this.morph == null)
        {
            return I18n.format("mappet.gui.triggers.morph.demorph");
        }

        return I18n.format("mappet.gui.triggers.morph.morph", this.morph.getString("Name"));
    }

    @Override
    public void trigger(DataContext context)
    {
        EntityPlayer player = this.target.getPlayer(context);

        if (player != null)
        {
            if (this.morph == null)
            {
                MorphAPI.demorph(player);
            }
            else
            {
                MorphAPI.morph(player, MorphManager.INSTANCE.morphFromNBT(this.morph), true);
            }
        }
    }

    @Override
    public boolean isEmpty()
    {
        return this.morph == null;
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setTag("Target", this.target.serializeNBT());

        if (this.morph != null)
        {
            tag.setTag("Morph", this.morph);
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Target"))
        {
            this.target.deserializeNBT(tag.getCompoundTag("Target"));
        }

        this.morph = null;

        if (tag.hasKey("Morph"))
        {
            this.morph = tag.getCompoundTag("Morph");
        }
    }
}