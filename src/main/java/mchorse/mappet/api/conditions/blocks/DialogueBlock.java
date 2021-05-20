package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.ICharacter;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DialogueBlock extends TargetBlock
{
    @Override
    public int getColor()
    {
        return 0x00ff33;
    }

    @Override
    public boolean evaluate(DataContext context)
    {
        if (this.target != Target.GLOBAL)
        {
            ICharacter character = this.getCharacter(context);

            return character != null && character.getStates().hasReadDialogue(this.id);
        }

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        String key = this.not
            ? "mappet.gui.conditions.dialogue.wasnt_read"
            : "mappet.gui.conditions.dialogue.was_read";

        return I18n.format(key, this.id);
    }
}