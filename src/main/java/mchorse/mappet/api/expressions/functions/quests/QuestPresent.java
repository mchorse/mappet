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

public class QuestPresent extends SNFunction
{
    public QuestPresent(IValue[] values, String name) throws Exception
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
            String id = this.getArg(0).stringValue();
            String target = this.getArg(1).stringValue();

            MinecraftServer server = Mappet.expressions.server;
            List<EntityPlayerMP> players = CommandBase.getPlayers(server, server, target);

            for (EntityPlayerMP player : players)
            {
                ICharacter character = Character.get(player);

                if (character != null && character.getQuests().getByName(id) != null)
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
