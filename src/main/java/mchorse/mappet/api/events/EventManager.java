package mchorse.mappet.api.events;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;
import mchorse.mappet.utils.NBTToJson;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class EventManager
{
    public static final MapNodeFactory factory = new MapNodeFactory()
        .register("command", CommandNode.class)
        .register("condition", ConditionNode.class);

    private File folder;

    public EventManager(File folder)
    {
        this.folder = folder;
        this.folder.mkdirs();
    }

    /* Execution */

    public EventContext execute(NodeSystem<EventNode> event, EventContext context)
    {
        if (event.main != null)
        {
            this.recursiveExecute(event, event.main, context);
        }

        return context;
    }

    private void recursiveExecute(NodeSystem<EventNode> system, EventNode node, EventContext context)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        if (node.execute(context))
        {
            context.nesting += 1;

            List<EventNode> children = system.getChildren(node);

            for (EventNode child : children)
            {
                this.recursiveExecute(system, child, context);
            }

            context.nesting -= 1;
        }

        context.executions += 1;
    }

    /* Load and save */

    public NodeSystem<EventNode> create()
    {
        return new NodeSystem<EventNode>(factory);
    }

    public NodeSystem<EventNode> load(String name)
    {
        try
        {
            File file = this.getCraftingFile(name);
            String json = FileUtils.readFileToString(file, Charset.defaultCharset());
            JsonElement element = new JsonParser().parse(json);
            NBTTagCompound tag = (NBTTagCompound) NBTToJson.fromJson(element);
            NodeSystem<EventNode> table = this.create();

            table.deserializeNBT(tag);

            return table;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean save(String name, NodeSystem<EventNode> nodeSystem)
    {
        try
        {
            File file = this.getCraftingFile(name);
            NBTTagCompound tag = nodeSystem.serializeNBT();
            JsonElement element = NBTToJson.toJson(tag);

            FileUtils.writeStringToFile(file, JsonUtils.jsonToPretty(element), Charset.defaultCharset());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private File getCraftingFile(String name)
    {
        return new File(this.folder, name + ".json");
    }

    public List<String> getKeys()
    {
        List<String> list = new ArrayList<String>();

        for (File file : this.folder.listFiles())
        {
            String name = file.getName();

            if (file.isFile() && name.endsWith(".json"))
            {
                list.add(name.substring(0, name.lastIndexOf(".")));
            }
        }

        return list;
    }
}