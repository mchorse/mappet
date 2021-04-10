package mchorse.mappet.api.utils.nodes;

import mchorse.mappet.api.utils.nodes.factory.INodeFactory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NodeSystem <T extends Node> implements INBTSerializable<NBTTagCompound>
{
    private INodeFactory<T> factory;

    public Map<UUID, T> nodes = new HashMap<UUID, T>();
    public List<NodeRelation<T>> relations = new ArrayList<NodeRelation<T>>();
    public T main;

    public NodeSystem(INodeFactory<T> factory)
    {
        this.factory = factory;
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
        if (this.nodes.containsKey(output.getId()) && this.nodes.containsKey(input.getId()) && !this.hasRelation(output, input))
        {
            this.relations.add(new NodeRelation<T>(output, input));

            return true;
        }

        return false;
    }

    /**
     * Checks whether output and input has a relationship in this
     * node system
     */
    public boolean hasRelation(T output, T input)
    {
        for (NodeRelation<T> relation : this.relations)
        {
            if (Objects.equals(relation.output.getId(), output.getId()) && Objects.equals(relation.input.getId(), input.getId()))
            {
                return true;
            }
        }

        return false;
    }

    public List<T> getChildren(T node)
    {
        List<T> children = new ArrayList<T>();

        for (NodeRelation<T> relation : this.relations)
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
                tag.setUniqueId("Main", this.main.getId());
            }

            NBTTagList relations = new NBTTagList();

            for (NodeRelation<T> relation : this.relations)
            {
                NBTTagCompound tagRelation = new NBTTagCompound();

                tagRelation.setUniqueId("Output", relation.output.getId());
                tagRelation.setUniqueId("Input", relation.input.getId());

                relations.appendTag(tagRelation);
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
                T output = this.nodes.get(relationTag.getUniqueId("Output"));
                T input = this.nodes.get(relationTag.getUniqueId("Input"));

                this.relations.add(new NodeRelation<T>(output, input));
            }
        }

        if (tag.hasUniqueId("Main"))
        {
            this.main = this.nodes.get(tag.getUniqueId("Main"));
        }
    }
}