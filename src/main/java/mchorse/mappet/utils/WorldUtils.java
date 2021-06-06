package mchorse.mappet.utils;

import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
    public static TileEntity getTileEntity(World world, BlockPos pos)
    {
        if (world.isBlockLoaded(pos))
        {
            return world.getTileEntity(pos);
        }

        return null;
    }

    public static void playSound(EntityPlayerMP player, String soundEvent)
    {
        player.connection.sendPacket(new SPacketCustomSound(soundEvent, SoundCategory.MASTER, player.posX, player.posY, player.posZ, 1, 1));
    }

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
}