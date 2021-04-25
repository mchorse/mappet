package mchorse.mappet.api.expressions.functions.factions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class FactionFunction extends SNFunction
{
    public FactionFunction(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    protected Faction getFaction(String id)
    {
        return Mappet.factions.load(id);
    }

    @Override
    public int getRequiredArguments()
    {
        return 2;
    }

    @Override
    public double doubleValue()
    {
        try
        {
            String id = this.getArg(0).stringValue();
            String target = this.getArg(1).stringValue();
            EntityPlayerMP player = CommandBase.getPlayer(Mappet.expressions.server, Mappet.expressions.server, target);
            ICharacter character = Character.get(player);

            if (character != null)
            {
                return this.apply(id, character);
            }
        }
        catch (Exception e)
        {}

        return 0;
    }

    protected abstract double apply(String id, ICharacter character);
}