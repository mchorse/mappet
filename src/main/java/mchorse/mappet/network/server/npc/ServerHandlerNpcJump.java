package mchorse.mappet.network.server.npc;

import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.client.npc.PacketNpcJump;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerNpcJump extends ServerMessageHandler<PacketNpcJump> {
    @Override
    public void run(EntityPlayerMP player, PacketNpcJump message) {
        Entity npc = player.world.getEntityByID(message.entityId);

        if (npc instanceof EntityNpc) {
            EntityNpc entityNpc = (EntityNpc) npc;
            float jumpPower = message.getJumpPower();

            if (entityNpc.onGround) {
                entityNpc.motionY += jumpPower;
            }
        }
    }
}