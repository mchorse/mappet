package mchorse.mappet.api.expressions.functions.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class QuestCompleted extends SNFunction
{
    public QuestCompleted(IValue[] values, String name) throws Exception
    {
        super(values, name);
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
            String target = this.getArg(1).stringValue();
            String id = this.getArg(0).stringValue();

            if (target.equals("~"))
            {
                return Mappet.states.wasQuestCompleted(id) ? 1 : 0;
            }
            else
            {
                MinecraftServer server = Mappet.expressions.server;
                List<EntityPlayerMP> players = CommandBase.getPlayers(server, server, target);

                for (EntityPlayerMP player : players)
                {
                    ICharacter character = Character.get(player);

                    if (character != null && character.getStates().wasQuestCompleted(id))
                    {
                        return 1;
                    }
                }
            }
        }
        catch (Exception e)
        {}

        return 0;
    }
}