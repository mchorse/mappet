package mchorse.mappet.api.utils;

import mchorse.mappet.utils.ExpressionRewriter;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataContext
{
    public static final ExpressionRewriter REWRITER = new ExpressionRewriter();

    public MinecraftServer server;
    public World world;
    public EntityLivingBase subject;
    public EntityLivingBase object;

    private TriggerSender sender;
    private Map<String, Object> values = new HashMap<String, Object>();

    public DataContext(EntityLivingBase subject, EntityLivingBase object)
    {
        this(subject);

        this.object = object;
    }

    public DataContext(EntityLivingBase subject)
    {
        this(subject.world);

        this.subject = subject;
    }

    public DataContext(World world)
    {
        this(world.getMinecraftServer());

        this.world = world;
    }

    public DataContext(MinecraftServer server)
    {
        this.server = server;

        this.set("subject", this.subject == null ? "" : this.subject.getCachedUniqueIdString());
        this.set("object", this.object == null ? "" : this.object.getCachedUniqueIdString());
    }

    public DataContext set(String key, double value)
    {
        this.values.put(key, value);

        return this;
    }

    public DataContext set(String key, String value)
    {
        this.values.put(key, value);

        return this;
    }

    public Set<Map.Entry<String, Object>> getValues()
    {
        return this.values.entrySet();
    }

    public Object getValue(String key)
    {
        return this.values.get(key);
    }

    public int execute(String command)
    {
        command = REWRITER.set(this).rewrite(command);

        return this.server.getCommandManager().executeCommand(this.getSender(), command);
    }

    public Set<String> getKeys()
    {
        return this.values.keySet();
    }

    public TriggerSender getSender()
    {
        if (this.sender == null)
        {
            this.sender = new TriggerSender();
        }

        if (this.subject == null)
        {
            this.sender.set(this.server);
        }
        else
        {
            this.sender.set(this.subject);
        }

        return this.sender;
    }
}