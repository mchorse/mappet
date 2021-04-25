package mchorse.mappet.api.expressions.functions.factions;

import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;

public class FactionNeutral extends FactionFunction
{
    public FactionNeutral(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(String id, ICharacter character)
    {
        Faction faction = this.getFaction(id);

        return faction.get(character.getStates()).isPassive() ? 1 : 0;
    }
}