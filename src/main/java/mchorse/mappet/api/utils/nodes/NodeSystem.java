package mchorse.mappet.api.utils.nodes;

import mchorse.mappet.api.utils.nodes.factory.INodeFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NodeSystem <T extends Node> implements INBTSerializable<NBTTagCompound>
{
    private INodeFactory<T> factory;

    public Map<UUID, T> nodes = new HashMap<UUID, T>();
    public Map<UUID, List<NodeRelation<T>>> relations = new HashMap<UUID, List<NodeRelation<T>>>();
    public T main;

    public NodeSystem(INodeFactory<T> factory)
    {
        this.factory = factory;
    }

    public INodeFactory<T> getFactory()
    {
        return this.factory;
    }

    /**
     * Add a node to node system
     *
     * If the node doesn't have an UUID, then it will be given a random
     * one to avoid UUID collision
     *
     * @throws IllegalStateException when node's UUID is already present
     */
    public void add(T node)
    {
        if (node.getId() == null)
        {
            UUID id;

            do
            {
                id = UUID.randomUUID();
            }
            while (this.nodes.containsKey(id));

            node.setId(id);
        }
        else if (this.nodes.containsKey(node.getId()))
        {
            throw new IllegalStateException("Node by UUID " + node.getId() + " is already present in this node system!");
        }

        this.nodes.put(node.getId(), node);
    }

    /**
     * Tie an input node to an output node
     */
    public boolean tie(T output, T input)
    {
        if (output == input)
        {
            return false;
        }

        if (this.nodes.containsKey(output.getId()) && this.nodes.containsKey(input.getId()) && !this.hasRelation(output, input))
        {
            List<NodeRelation<T>> relations = this.relations.get(output.getId());

            if (relations == null)
            {
                relations = new ArrayList<NodeRelation<T>>();

                this.relations.put(output.getId(), relations);
            }

            relations.add(new NodeRelation<T>(output, input));

            return true;
        }

        return false;
    }

    public void untie(T output, T input)
    {
        List<NodeRelation<T>> relations = this.relations.get(output.getId());

        if (relations != null)
        {
            relations.removeIf((relation) -> relation.input == input);

            if (relations.isEmpty())
            {
                this.relations.remove(output.getId());
            }
        }
    }

    public void addTie(T output, T toAdd)
    {
        this.add(toAdd);
        this.tie(output, toAdd);
    }

    public void addMain(T node)
    {
        this.add(node);
        this.main = node;
    }

    public boolean remove(T node)
    {
        UUID key = node.getId();

        if (!this.nodes.containsKey(key))
        {
            return false;
        }

        this.nodes.remove(key);

        this.relations.remove(node.getId());

        Iterator<Map.Entry<UUID, List<NodeRelation<T>>>> it = this.relations.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry<UUID, List<NodeRelation<T>>> entry = it.next();

            entry.getValue().removeIf(relation -> relation.input == node);

            if (entry.getValue().isEmpty())
            {
                it.remove();
            }
        }

        return true;
    }

    /**
     * Checks whether output and input has a relationship in this
     * node system
     */
    public boolean hasRelation(T output, T input)
    {
        return this.getRelation(output, input) != null;
    }

    public NodeRelation<T> getRelation(T output, T input)
    {
        if (!this.relations.containsKey(output.getId()))
        {
            return null;
        }

        for (NodeRelation<T> relation : this.relations.get(output.getId()))
        {
            if (Objects.equals(relation.input.getId(), input.getId()))
            {
                return relation;
            }
        }

        return null;
    }

    public List<T> getChildren(T node)
    {
        List<T> children = new ArrayList<T>();

        for (NodeRelation<T> relation : this.relations.get(node.getId()))
        {
            if (relation.output == node)
            {
                children.add(relation.input);
            }
        }

        return children;
    }

    /* Serialization / deserialization */

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList nodes = new NBTTagList();

        for (T node : this.nodes.values())
        {
            NBTTagCompound nodeTag = node.serializeNBT();

            nodeTag.setString("Type", this.factory.getType(node));
            nodes.appendTag(nodeTag);
        }

        if (nodes.tagCount() > 0)
        {
            tag.setTag("Nodes", nodes);

            if (this.main != null && this.nodes.containsKey(this.main.getId()))
            {
                tag.setString("Main", this.main.getId().toString());
            }

            NBTTagList relations = new NBTTagList();

            for (List<NodeRelation<T>> list : this.relations.values())
            {
                for (NodeRelation<T> relation : list)
                {
                    NBTTagCompound tagRelation = new NBTTagCompound();

                    tagRelation.setString("Output", relation.output.getId().toString());
                    tagRelation.setString("Input", relation.input.getId().toString());

                    relations.appendTag(tagRelation);
                }
            }

            tag.setTag("Relations", relations);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Nodes", Constants.NBT.TAG_LIST))
        {
            NBTTagList nodes = tag.getTagList("Nodes", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < nodes.tagCount(); i++)
            {
                NBTTagCompound nodeTag = nodes.getCompoundTagAt(i);
                T node = this.factory.create(nodeTag.getString("Type"));

                node.deserializeNBT(nodeTag);

                this.add(node);
            }
        }

        if (tag.hasKey("Relations"))
        {
            NBTTagList relations = tag.getTagList("Relations", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < relations.tagCount(); i++)
            {
                NBTTagCompound relationTag = relations.getCompoundTagAt(i);
                T output = this.nodes.get(UUID.fromString(relationTag.getString("Output")));
                T input = this.nodes.get(UUID.fromString(relationTag.getString("Input")));

                if (output == input || input == null || output == null)
                {
                    continue;
                }

                this.tie(output, input);
            }
        }

        if (tag.hasKey("Main"))
        {
            this.main = this.nodes.get(UUID.fromString(tag.getString("Main")));
        }
    }
}