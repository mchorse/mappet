package mchorse.mappet.api.events;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

public class EventContext
{
    public MinecraftServer server;
    public EntityLivingBase subject;
    public EntityLivingBase object;

    public boolean debug;
    public StringBuilder log = new StringBuilder();
    public int nesting = 0;
    public int executions = 0;

    public EventContext(MinecraftServer server, EntityLivingBase subject)
    {
        this(server, subject, false);
    }

    public EventContext(MinecraftServer server, EntityLivingBase subject, boolean debug)
    {
        this.server = server;
        this.subject = subject;
        this.debug = debug;
    }

    public EventContext(MinecraftServer server, EntityLivingBase subject, EntityLivingBase object, boolean debug)
    {
        this(server, subject, debug);

        this.object = object;
    }

    public String processCommand(String command)
    {
        if (this.subject != null)
        {
            command = command.replace("${subject}", this.subject.getCachedUniqueIdString());
        }

        if (this.object != null)
        {
            command = command.replace("${object}", this.object.getCachedUniqueIdString());
        }

        return command;
    }

    public void log(String message)
    {
        if (this.debug)
        {
            if (this.nesting > 0)
            {
                message = StringUtils.repeat("-", this.nesting) + " " + message;
            }

            this.log.append(message + "\n");
        }
    }
}