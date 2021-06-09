package mchorse.mappet.api.scripts.code;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.scripts.ScriptExecutionFork;
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
    private String script;
    private String function;

    private IScriptEntity subject;
    private IScriptEntity object;
    private IScriptWorld world;
    private IScriptServer server;

    public ScriptEvent(DataContext context, String script, String function)
    {
        this.context = context;
        this.script = script;
        this.function = function;
    }

    @Override
    public IScriptEntity getSubject()
    {
        if (this.subject == null && this.context.subject != null)
        {
            this.subject = new ScriptEntity(this.context.subject);
        }

        return this.subject;
    }

    @Override
    public IScriptEntity getObject()
    {
        if (this.object == null && this.context.object != null)
        {
            this.object = new ScriptEntity(this.context.object);
        }

        return this.object;
    }

    @Override
    public IScriptWorld getWorld()
    {
        if (this.world == null && this.context.world != null)
        {
            this.world = new ScriptWorld(this.context.world);
        }

        return this.world;
    }

    @Override
    public IScriptServer getServer()
    {
        if (this.server == null && this.context.server != null)
        {
            this.server = new ScriptServer(this.context.server);
        }

        return this.server;
    }

    @Override
    public Map<String, Object> getValues()
    {
        return this.context.getValues();
    }

    @Override
    public Object getValue(String key)
    {
        return this.context.getValue(key);
    }

    @Override
    public void setValue(String key, Object value)
    {
        this.context.getValues().put(key, value);
    }

    /* Useful methods */

    @Override
    public void scheduleScript(int delay)
    {
        this.scheduleScript(this.function, delay);
    }

    @Override
    public void scheduleScript(String function, int delay)
    {
        this.scheduleScript(this.script, function, delay);
    }

    @Override
    public void scheduleScript(String script, String function, int delay)
    {
        CommonProxy.eventHandler.addExecutable(new ScriptExecutionFork(this.context, script, function, delay));
    }

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
            entity.getMinecraftEntity().sendMessage(new TextComponentString(message));
        }

        return false;
    }
}