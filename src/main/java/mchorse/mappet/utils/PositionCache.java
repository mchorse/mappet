package mchorse.mappet.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class PositionCache
{
    public Vec3d lastPosition;
    public Vec3d lastLastPosition;
    private int timer;

    public void setLastPosition(EntityPlayer player)
    {
        this.lastLastPosition = this.lastPosition;
        this.lastPosition = new Vec3d(player.posX, player.posY, player.posZ);
    }

    public void resetLastPositionTimer()
    {
        this.timer = 0;
    }

    public boolean updatePlayer(EntityPlayer player)
    {
        this.timer += 1;

        if (this.timer >= 10)
        {
            this.setLastPosition(player);
            this.timer = 0;

            return true;
        }

        return false;
    }
}