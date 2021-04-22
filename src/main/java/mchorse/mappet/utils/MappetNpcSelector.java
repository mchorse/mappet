package mchorse.mappet.utils;

import com.google.common.base.Predicate;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.IEntitySelectorFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MappetNpcSelector implements IEntitySelectorFactory
{
    public static final String ARGUMENT_MAPPET_NPC_ID = "mpid";

    @Nonnull
    @Override
    public List<Predicate<Entity>> createPredicates(Map<String, String> arguments, String mainSelector, ICommandSender sender, Vec3d position)
    {
        List<Predicate<Entity>> list = new ArrayList<Predicate<Entity>>();

        if (arguments.containsKey(ARGUMENT_MAPPET_NPC_ID))
        {
            String id = arguments.get(ARGUMENT_MAPPET_NPC_ID);
            boolean negative = id.startsWith("!");

            if (negative)
            {
                id = id.substring(1);
            }

            final String finalId = id;

            list.add((e) ->
            {
                if (e instanceof EntityNpc)
                {
                    String npcId = ((EntityNpc) e).getId();

                    return negative != npcId.equals(finalId);
                }

                return false;
            });
        }

        return list;
    }
}