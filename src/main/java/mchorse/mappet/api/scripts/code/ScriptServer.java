package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptServer;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class ScriptServer implements IScriptServer
{
    private MinecraftServer server;

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
}