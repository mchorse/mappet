package mchorse.mappet.utils;

import com.google.common.collect.ImmutableSet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;

import java.util.Set;

public class EntityUtils
{
    public static final Set<String> ENTITY_PROPERTIES = ImmutableSet.of("xp", "xp_level", "hp", "hunger", "armor", "ticks", "light", "light_sky", "sneaking", "sprinting", "on_ground");

    public static States getStates(Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            ICharacter character = Character.get((EntityPlayer) entity);

            if (character != null)
            {
                return character.getStates();
            }
        }
        else if (entity instanceof EntityNpc)
        {
            return ((EntityNpc) entity).getStates();
        }

        return null;
    }

    public static double getProperty(Entity entity, String property)
    {
        EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        EntityLivingBase living = entity instanceof EntityLivingBase ? (EntityLivingBase) entity : null;

        switch (property)
        {
            case "xp":
                return player == null ? 0 : player.experienceTotal;

            case "xp_level":
                return player == null ? 0 : player.experienceLevel;

            case "hp":
                return living == null ? 0 : living.getHealth();

            case "hunger":
                return player == null ? 0 : player.getFoodStats().getFoodLevel();

            case "armor":
                return player == null ? 0 : ForgeHooks.getTotalArmorValue(player);

            case "ticks":
                return entity.ticksExisted;

            case "light":
                return getCombinedLight(entity) % 65536F / 15F;

            case "light_sky":
                return getCombinedLight(entity) / 65536F / 15F;

            case "sneaking":
                return entity.isSneaking() ? 1 : 0;

            case "sprinting":
                return entity.isSprinting() ? 1 : 0;

            case "on_ground":
                return entity.onGround ? 1 : 0;
        }

        return 0;
    }

    public static int getCombinedLight(Entity entity)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(entity.posX), 0, MathHelper.floor(entity.posZ));

        if (entity.world.isBlockLoaded(pos))
        {
            pos.setY(MathHelper.floor(entity.posY + entity.getEyeHeight()));

            return entity.world.getCombinedLight(pos, 0);
        }

        return 0;
    }
}