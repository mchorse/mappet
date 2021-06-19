package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.DataContext;
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
        if (this.target != Target.GLOBAL)
        {
            ICharacter character = this.getCharacter(context);

            return character != null && character.getStates().hasReadDialogue(this.id, this.marker);
        }

        return false;
    }

    @Override
    protected Target getDefaultTarget()
    {
        return Target.SUBJECT;
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