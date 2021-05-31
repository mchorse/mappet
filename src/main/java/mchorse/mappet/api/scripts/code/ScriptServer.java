package mchorse.mappet.api.scripts.code;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.mappet.MappetStates;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptServer;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class ScriptServer implements IScriptServer
{
    private MinecraftServer server;
    private IMappetStates states;

    public ScriptServer(MinecraftServer server)
    {
        this.server = server;
    }

    public MinecraftServer getMinecraftServer()
    {
        return this.server;
    }

    @Override
    public List<IScriptEntity> query(String targetSelector)
    {
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        try
        {
            for (Entity entity : EntitySelector.matchEntities(this.server, targetSelector, Entity.class))
            {
                entities.add(new ScriptEntity(entity));
            }
        }
        catch (Exception e)
        {}

        return entities;
    }

    @Override
    public List<IScriptEntity> players()
    {
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        for (EntityPlayerMP player : this.server.getPlayerList().getPlayers())
        {
            entities.add(new ScriptEntity(player));
        }

        return entities;
    }

    @Override
    public IMappetStates states()
    {
        if (this.states == null)
        {
            this.states = new MappetStates(Mappet.states);
        }

        return this.states;
    }
}