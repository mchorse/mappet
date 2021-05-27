package mchorse.mappet.api.utils;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TriggerSender implements ICommandSender
{
    public MinecraftServer server;
    public World world;
    public BlockPos pos;

    public EntityLivingBase entity;

    public TriggerSender set(MinecraftServer server, World world, BlockPos pos)
    {
        this.server = server;
        this.world = world;
        this.pos = pos;
        this.entity = null;

        return this;
    }

    public TriggerSender set(EntityLivingBase entity)
    {
        this.entity = entity;
        this.server = entity.getServer();

        return this;
    }

    @Override
    public String getName()
    {
        return "Mappet command sender";
    }

    @Override
    public boolean canUseCommand(int permLevel, String commandName)
    {
        return true;
    }

    @Override
    public World getEntityWorld()
    {
        if (this.entity != null)
        {
            return this.entity.world;
        }

        return this.world == null ? this.server.getEntityWorld() : this.world;
    }

    @Nullable
    @Override
    public MinecraftServer getServer()
    {
        return this.server;
    }

    @Override
    public BlockPos getPosition()
    {
        if (this.entity != null)
        {
            return new BlockPos(this.entity);
        }

        return this.pos == null ? BlockPos.ORIGIN : this.pos;
    }

    @Override
    public Vec3d getPositionVector()
    {
        if (this.entity != null)
        {
            return this.entity.getPositionVector();
        }

        return this.pos == null ? Vec3d.ZERO : new Vec3d(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5);
    }

    @Nullable
    @Override
    public Entity getCommandSenderEntity()
    {
        return this.entity;
    }
}