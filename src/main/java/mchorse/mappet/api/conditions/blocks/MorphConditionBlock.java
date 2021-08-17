package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.models.IMorphProvider;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.capabilities.morphing.Morphing;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MorphConditionBlock extends TargetConditionBlock
{
    public NBTTagCompound morph;
    public boolean onlyName;

    private AbstractMorph compiledMorph;

    @Override
    protected TargetMode getDefaultTarget()
    {
        return TargetMode.PLAYER;
    }

    @Override
    protected boolean evaluateBlock(DataContext context)
    {
        Entity entity = this.target.getEntity(context);
        AbstractMorph morph = getMorph(entity);

        if (morph == null && this.morph == null)
        {
            return true;
        }

        if (morph == null || this.morph == null)
        {
            return false;
        }

        if (this.onlyName)
        {
            return morph.name.equals(this.morph.getString("Name"));
        }

        if (this.compiledMorph == null)
        {
            this.compiledMorph = MorphManager.INSTANCE.morphFromNBT(this.morph);
        }

        return morph.equals(this.compiledMorph);
    }

    private AbstractMorph getMorph(Entity entity)
    {
        AbstractMorph morph = null;

        if (entity instanceof IMorphProvider)
        {
            morph = ((IMorphProvider) entity).getMorph();
        }
        else if (entity instanceof EntityPlayer)
        {
            morph = Morphing.get((EntityPlayer) entity).getCurrentMorph();
        }

        return morph;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        if (this.morph == null)
        {
            return I18n.format("mappet.gui.conditions.morph.no_morph");
        }

        return I18n.format("mappet.gui.conditions.morph.string", this.morph.getString("Name"));
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        if (this.morph != null)
        {
            tag.setTag("Morph", this.morph);
        }

        tag.setBoolean("OnlyName", this.onlyName);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.morph = null;
        this.compiledMorph = null;

        if (tag.hasKey("Morph", Constants.NBT.TAG_COMPOUND))
        {
            this.morph = tag.getCompoundTag("Morph");
        }

        this.onlyName = tag.getBoolean("OnlyName");
    }
}