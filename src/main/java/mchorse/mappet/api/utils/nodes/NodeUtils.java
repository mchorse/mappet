package mchorse.mappet.api.utils.nodes;

import net.minecraft.nbt.NBTTagCompound;

public class NodeUtils
{
    public static <T extends Node> T nodeFromNBT(NodeSystem<T> system, NBTTagCompound tag)
    {
        String type = tag.getString("Type");
        T node = system.getFactory().create(type);

        node.deserializeNBT(tag);

        return node;
    }

    public static <T extends Node> NBTTagCompound nodeToNBT(NodeSystem<T> system, T node)
    {
        NBTTagCompound tag = node.serializeNBT();

        tag.setString("Type", system.getFactory().getType(node));

        return tag;
    }
}