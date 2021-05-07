package mchorse.mappet.api.utils.nodes;

import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.nodes.factory.INodeFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NodeSystem <T extends Node> extends AbstractData
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

        if (this.relations.containsKey(node.getId()))
        {
            for (NodeRelation<T> relation : this.relations.get(node.getId()))
            {
                if (relation.output == node)
                {
                    children.add(relation.input);
                }
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

            if (this.relations.containsKey(node.getId()))
            {
                NBTTagList relations = new NBTTagList();

                for (NodeRelation<T> relation : this.relations.get(node.getId()))
                {
                    relations.appendTag(new NBTTagString(relation.input.getId().toString()));
                }

                nodeTag.setTag("Relations", relations);
            }

            nodes.appendTag(nodeTag);
        }

        if (nodes.tagCount() > 0)
        {
            if (this.main != null && this.nodes.containsKey(this.main.getId()))
            {
                tag.setString("Main", this.main.getId().toString());
            }

            tag.setTag("Nodes", nodes);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        Map<UUID, List<UUID>> map = new HashMap<UUID, List<UUID>>();

        if (tag.hasKey("Nodes", Constants.NBT.TAG_LIST))
        {
            NBTTagList nodes = tag.getTagList("Nodes", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < nodes.tagCount(); i++)
            {
                NBTTagCompound nodeTag = nodes.getCompoundTagAt(i);
                T node = this.factory.create(nodeTag.getString("Type"));

                node.deserializeNBT(nodeTag);

                /* Relations are not serialized by nodes themselves */
                if (nodeTag.hasKey("Relations"))
                {
                    List<UUID> uuids = new ArrayList<UUID>();
                    NBTTagList relations = nodeTag.getTagList("Relations", Constants.NBT.TAG_STRING);

                    map.put(node.getId(), uuids);

                    for (int j = 0; j < relations.tagCount(); j++)
                    {
                        uuids.add(UUID.fromString(relations.getStringTagAt(j)));
                    }
                }

                this.add(node);
            }
        }

        /* Tie collected nodes */
        for (Map.Entry<UUID, List<UUID>> entry : map.entrySet())
        {
            for (UUID input : entry.getValue())
            {
                T nodeOutput = this.nodes.get(entry.getKey());
                T nodeInput = this.nodes.get(input);

                if (nodeOutput == nodeInput || nodeInput == null || nodeOutput == null)
                {
                    continue;
                }

                this.tie(nodeOutput, nodeInput);
            }
        }

        if (tag.hasKey("Main"))
        {
            this.main = this.nodes.get(UUID.fromString(tag.getString("Main")));
        }
    }

    public List<T> getRoots()
    {
        List<T> roots = new ArrayList<T>();

        main:
        for (T node : this.nodes.values())
        {
            for (List<NodeRelation<T>> relations : this.relations.values())
            {
                for (NodeRelation<T> relation : relations)
                {
                    if (relation.input == node)
                    {
                        continue main;
                    }
                }
            }

            roots.add(node);
        }

        return roots;
    }
}