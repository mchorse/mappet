package mchorse.mappet.api.npcs;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class parses NPC format for changing the data, which looks
 * something like this:
 *
 * [npc_id@]state_name[:property1[,property2,etc]]
 *
 * Where the NPC ID and properties are optional.
 */
public class NpcLexer
{
    public static final List<String> PROPERTIES = ImmutableList.of("max_health", "health", "regen_delay", "regen_frequency", "damage", "can_ranged", "can_fall_damage", "can_get_burned", "invincible", "killable", "speed", "can_walk", "can_swim", "can_fly", "has_post", "post", "post_radius", "fallback", "patrol_circulate", "patrol", "follow", "morph", "sight_distance", "sight_radius", "drops", "xp", "look_at_player", "look_around", "wander", "flee");

    public String id;
    public String state;
    public List<String> properties = new ArrayList<String>();

    public static NpcLexer parse(String input, String npcId)
    {
        String state = input;
        Set<String> properties = null;

        if (input.contains("@"))
        {
            String[] splits = input.split("@");

            npcId = splits[0];
            state = splits[1];
        }

        if (state.contains(":"))
        {
            String[] splits = state.split(":");
            String[] props = splits[1].split(",");

            state = splits[0];
            properties = new HashSet<String>();

            for (String property : props)
            {
                if (PROPERTIES.contains(property))
                {
                    properties.add(property);
                }
            }
        }

        NpcLexer lexer = new NpcLexer(npcId, state);

        if (properties != null)
        {
            lexer.properties.addAll(properties);
        }

        return lexer;
    }

    public NpcLexer(String id, String state)
    {
        this.id = id;
        this.state = state;
    }
}