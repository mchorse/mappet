package mchorse.mappet.api.expressions.functions.dialogue;

import mchorse.mappet.api.expressions.functions.factions.FactionFunction;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;

public class DialogueRead extends FactionFunction
{
    public DialogueRead(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(String id, ICharacter character)
    {
        return character.getStates().hasReadDialogue(id) ? 1 : 0;
    }
}