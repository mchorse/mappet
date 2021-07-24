package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogueConditionBlock extends TargetConditionBlock
{
    public String marker = "";

    @Override
    public boolean evaluateBlock(DataContext context)
    {
        if (this.target.mode != TargetMode.GLOBAL)
        {
            ICharacter character = this.target.getCharacter(context);

            return character != null && character.getStates().hasReadDialogue(this.id, this.marker);
        }

        return false;
    }

    @Override
    protected TargetMode getDefaultTarget()
    {
        return TargetMode.SUBJECT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return I18n.format("mappet.gui.conditions.dialogue.was_read", this.id);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Marker", this.marker);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.marker = tag.getString("Marker");
    }
}