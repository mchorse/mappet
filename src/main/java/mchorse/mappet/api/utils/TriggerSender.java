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
    public EntityLivingBase entity;

    public TriggerSender set(MinecraftServer server)
    {
        this.server = server;
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
        return this.entity == null ? this.server.getEntityWorld() : this.entity.world;
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
        return this.entity == null ? BlockPos.ORIGIN : new BlockPos(this.entity);
    }

    @Override
    public Vec3d getPositionVector()
    {
        return this.entity == null ? Vec3d.ZERO : this.entity.getPositionVector();
    }

    @Nullable
    @Override
    public Entity getCommandSenderEntity()
    {
        return this.entity;
    }
}