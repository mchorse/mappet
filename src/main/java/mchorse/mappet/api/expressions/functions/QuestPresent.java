package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.entity.player.EntityPlayer;

public class QuestPresent extends SNFunction
{
    public QuestPresent(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public double doubleValue()
    {
        if (Mappet.expressions.subject instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) Mappet.expressions.subject;
            ICharacter character = Character.get(player);

            if (character != null)
            {
                return character.getQuests().getByName(this.getArg(0).stringValue()) == null ? 0 : 1;
            }
        }

        return 0;
    }
}
