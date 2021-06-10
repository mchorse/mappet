package mchorse.mappet.network.server.factions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.factions.PacketFactions;
import mchorse.mappet.network.common.factions.PacketRequestFactions;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;

public class ServerHandlerRequestFactions extends ServerMessageHandler<PacketRequestFactions>
{
    public static void collectFactions(EntityPlayerMP player, States states)
    {
        Map<String, Double> statesData = new HashMap<String, Double>();
        Map<String, Faction> factions = new HashMap<String, Faction>();

        for (Map.Entry<String, Object> entry : states.values.entrySet())
        {
            String key = entry.getKey();

            if (key.startsWith(States.FACTIONS_PREFIX) && entry.getValue() instanceof Number)
            {
                statesData.put(key.substring(States.FACTIONS_PREFIX.length()), ((Number) entry.getValue()).doubleValue());
            }
        }

        if (!statesData.isEmpty())
        {
            for (String key : statesData.keySet())
            {
                Faction faction = Mappet.factions.load(key);

                if (faction != null && faction.isVisible(player))
                {
                    factions.put(key, faction);
                }
            }

            if (!factions.isEmpty())
            {
                Dispatcher.sendTo(new PacketFactions(factions, statesData), player);
            }
        }
    }

    @Override
    public void run(EntityPlayerMP player, PacketRequestFactions message)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            collectFactions(player, character.getStates());
        }
    }
}