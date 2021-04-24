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

public class QuestPresentCompleted extends SNFunction
{
    public QuestPresentCompleted(IValue[] values, String name) throws Exception
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
        try
        {
            String id = this.getArg(0).stringValue();

            if (Mappet.states.wasQuestCompleted(id))
            {
                return 1;
            }

            MinecraftServer server = Mappet.expressions.server;

            for (EntityPlayerMP player : server.getPlayerList().getPlayers())
            {
                ICharacter character = Character.get(player);

                if (character != null && (character.getStates().wasQuestCompleted(id) || character.getQuests().getByName(id) != null))
                {
                    return 1;
                }
            }
        }
        catch (Exception e)
        {}

        return 0;
    }
}