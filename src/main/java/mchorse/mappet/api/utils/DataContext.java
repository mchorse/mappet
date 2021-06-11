package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.utils.ExpressionRewriter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataContext
{
    public static final ExpressionRewriter REWRITER = new ExpressionRewriter();

    public MinecraftServer server;
    public World world;
    public BlockPos pos;
    public EntityLivingBase subject;
    public EntityLivingBase object;

    public boolean canceled;

    private TriggerSender sender;
    private Map<String, Object> values = new HashMap<String, Object>();

    public DataContext(EntityLivingBase subject, EntityLivingBase object)
    {
        this(subject);

        this.object = object;
        this.setup();
    }

    public DataContext(EntityLivingBase subject)
    {
        this(subject.world);

        this.subject = subject;
        this.setup();
    }

    public DataContext(World world)
    {
        this(world.getMinecraftServer());

        this.world = world;
    }

    public DataContext(World world, BlockPos pos)
    {
        this(world.getMinecraftServer());

        this.world = world;
        this.pos = pos;
    }

    public DataContext(MinecraftServer server)
    {
        this.server = server;

        this.setup();
    }

    private void setup()
    {
        this.set("subject", this.subject == null ? "" : this.subject.getCachedUniqueIdString());
        this.set("subject_name", this.subject == null ? "" : this.subject.getName());
        this.set("object", this.object == null ? "" : this.object.getCachedUniqueIdString());
        this.set("object_name", this.object == null ? "" : this.object.getName());
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

    public DataContext parse(String nbt)
    {
        try
        {
            this.parse(JsonToNBT.getTagFromJson(nbt));
        }
        catch (Exception e)
        {}

        return this;
    }

    public DataContext parse(NBTTagCompound tag)
    {
        for (String key : tag.getKeySet())
        {
            NBTBase value = tag.getTag(key);

            if (value instanceof NBTPrimitive)
            {
                this.set(key, ((NBTPrimitive) value).getDouble());
            }
            else if (value instanceof NBTTagString)
            {
                this.set(key, ((NBTTagString) value).getString());
            }
        }

        return this;
    }

    public Map<String, Object> getValues()
    {
        return this.values;
    }

    public Object getValue(String key)
    {
        return this.values.get(key);
    }

    public int execute(String command)
    {
        command = this.process(command);

        return this.server.getCommandManager().executeCommand(this.getSender(), command);
    }

    public String process(String text)
    {
        /* Get rid off slash, even though it can be used, server API's like Mohist
         * seem to have problem with that, so this should automatically fix command
         * execution there */
        if (text.startsWith("/"))
        {
            text = text.substring(1);
        }

        if (!text.contains("${"))
        {
            return text;
        }

        return REWRITER.set(this).rewrite(text);
    }

    public EntityPlayer getPlayer()
    {
        if (this.subject instanceof EntityPlayer)
        {
            return (EntityPlayer) this.subject;
        }
        else if (this.object instanceof EntityPlayer)
        {
            return (EntityPlayer) this.object;
        }

        return null;
    }

    public EntityNpc getNPC()
    {
        if (this.subject instanceof EntityNpc)
        {
            return (EntityNpc) this.subject;
        }
        else if (this.object instanceof EntityNpc)
        {
            return (EntityNpc) this.object;
        }

        return null;
    }

    public Set<String> getKeys()
    {
        return this.values.keySet();
    }

    public ICommandSender getSender()
    {
        if (Mappet.eventUseServerForCommands.get())
        {
            return this.server;
        }

        if (this.sender == null)
        {
            this.sender = new TriggerSender();
        }

        if (this.subject == null)
        {
            this.sender.set(this.server, this.world, this.pos);
        }
        else
        {
            this.sender.set(this.subject);
        }

        return this.sender;
    }

    public DataContext copy()
    {
        DataContext context = new DataContext(this.server);

        context.subject = this.subject;
        context.object = this.object;
        context.pos = this.pos;
        context.world = this.world;
        context.values.putAll(this.values);
        context.setup();

        return context;
    }
}