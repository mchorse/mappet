package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.scripts.user.IScriptServer;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.Map;

public class ScriptEvent implements IScriptEvent
{
    private DataContext context;

    private IScriptEntity subject;
    private IScriptEntity object;
    private IScriptWorld world;
    private IScriptServer server;

    public ScriptEvent(DataContext context)
    {
        this.context = context;
    }

    @Override
    public IScriptEntity subject()
    {
        if (this.subject == null && this.context.subject != null)
        {
            this.subject = new ScriptEntity(this.context.subject);
        }

        return this.subject;
    }

    @Override
    public IScriptEntity object()
    {
        if (this.object == null && this.context.object != null)
        {
            this.object = new ScriptEntity(this.context.object);
        }

        return this.object;
    }

    @Override
    public IScriptWorld world()
    {
        if (this.world == null && this.context.world != null)
        {
            this.world = new ScriptWorld(this.context.world);
        }

        return this.world;
    }

    @Override
    public IScriptServer server()
    {
        if (this.server == null && this.context.server != null)
        {
            this.server = new ScriptServer(this.context.server);
        }

        return this.server;
    }

    @Override
    public Map<String, Object> values()
    {
        return this.context.getValues();
    }

    /* Useful methods */

    @Override
    public void executeCommand(String command)
    {
        this.context.execute(command);
    }

    @Override
    public void send(String message)
    {
        for (EntityPlayer player : this.context.server.getPlayerList().getPlayers())
        {
            player.sendMessage(new TextComponentString(message));
        }
    }

    @Override
    public boolean sendTo(IScriptEntity entity, String message)
    {
        if (entity.isPlayer())
        {
            ((ScriptEntity) entity).getEntity().sendMessage(new TextComponentString(message));
        }

        return false;
    }
}