package mchorse.mappet.api.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.TriggerSender;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.EntityLivingBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class EventContext
{
    public NodeSystem<EventNode> system;

    public TriggerSender sender;
    public EntityLivingBase subject;
    public String subjectId;
    public EntityLivingBase object;

    public boolean debug;
    public StringBuilder log = new StringBuilder();
    public int nesting = 0;
    public int executions = 0;

    public List<EventExecutionFork> executionForks = new ArrayList<EventExecutionFork>();

    public EventContext(TriggerSender sender, EntityLivingBase subject)
    {
        this.sender = sender;
        this.subject = subject;
    }

    public EventContext(TriggerSender sender, EntityLivingBase subject, EntityLivingBase object)
    {
        this(sender, subject);

        this.object = object;
    }

    public EventContext debug()
    {
        this.debug = true;

        return this;
    }

    public String getSubjectId()
    {
        if (this.subjectId != null)
        {
            return this.subjectId;
        }

        if (this.subject instanceof EntityNpc)
        {
            return ((EntityNpc) this.subject).getId();
        }

        return "";
    }

    public void addExecutionFork(EventNode node, int timer)
    {
        if (this.system != null && timer > 0)
        {
            this.executionForks.add(new EventExecutionFork(this.system, node, this, timer));
        }
    }

    public void submitDelayedExecutions()
    {
        if (!this.executionForks.isEmpty())
        {
            CommonProxy.eventHandler.addExecutionForks(this.executionForks);

            this.executionForks.clear();
        }
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