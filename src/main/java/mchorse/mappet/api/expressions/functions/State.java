package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.EntityPlayer;

public class State extends SNFunction
{
    public State(IValue[] values, String name) throws Exception
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
        String target = this.args.length > 1 ? this.getArg(1).stringValue() : "";
        States states = this.getState(target);

        return states.get(this.getArg(0).stringValue());
    }

    private States getState(String target)
    {
        States states = Mappet.states;

        if (!target.equals("~"))
        {
            try
            {
                EntityPlayer player = CommandBase.getPlayer(Mappet.expressions.server, Mappet.expressions.server, target);
                ICharacter character = Character.get(player);

                if (character != null)
                {
                    states = character.getStates();
                }
            }
            catch (Exception e)
            {}
        }

        return states;
    }
}