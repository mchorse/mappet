package mchorse.mappet.api.expressions.functions.factions;

import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;

public class FactionHas extends FactionFunction
{
    public FactionHas(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(String id, ICharacter character)
    {
        return character.getStates().hasFaction(id) ? 1 : 0;
    }
}