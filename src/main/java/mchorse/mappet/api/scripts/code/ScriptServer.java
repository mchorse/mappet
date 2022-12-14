package mchorse.mappet.api.scripts.code;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.entities.ScriptEntity;
import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.user.IScriptServer;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ScriptServer implements IScriptServer
{
    private MinecraftServer server;
    private IMappetStates states;

    public ScriptServer(MinecraftServer server)
    {
        this.server = server;
    }

    @Override
    public MinecraftServer getMinecraftServer()
    {
        return this.server;
    }

    @Override
    public IScriptWorld getWorld(int dimension)
    {
        return new ScriptWorld(this.server.getWorld(dimension));
    }

    @Override
    public List<IScriptEntity> getEntities(String targetSelector)
    {
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        try
        {
            for (Entity entity : EntitySelector.matchEntities(this.server, targetSelector, Entity.class))
            {
                entities.add(ScriptEntity.create(entity));
            }
        }
        catch (Exception e)
        {}

        return entities;
    }

    @Override
    public IScriptEntity getEntity(String uuid)
    {
        return ScriptEntity.create(this.server.getEntityFromUuid(UUID.fromString(uuid)));
    }

    @Override
    public List<IScriptPlayer> getAllPlayers()
    {
        List<IScriptPlayer> entities = new ArrayList<IScriptPlayer>();

        for (EntityPlayerMP player : this.server.getPlayerList().getPlayers())
        {
            entities.add(new ScriptPlayer(player));
        }

        return entities;
    }

    @Override
    public IScriptPlayer getPlayer(String username)
    {
        EntityPlayerMP player = this.server.getPlayerList().getPlayerByUsername(username);

        if (player != null)
        {
            return new ScriptPlayer(player);
        }

        return null;
    }

    @Override
    public IMappetStates getStates()
    {
        if (this.states == null)
        {
            this.states = new MappetStates(Mappet.states);
        }

        return this.states;
    }
}