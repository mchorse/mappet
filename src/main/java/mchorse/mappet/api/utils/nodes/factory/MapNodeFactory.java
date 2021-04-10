package mchorse.mappet.api.utils.nodes.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import mchorse.mappet.api.utils.nodes.Node;

import java.util.Map;

/**
 * Basic implementation of node factory based on a map of
 * String key and Class value
 */
public class MapNodeFactory implements INodeFactory
{
    private BiMap<String, Class<? extends Node>> factory = HashBiMap.create();

    public MapNodeFactory copy()
    {
        MapNodeFactory factory = new MapNodeFactory();

        for (Map.Entry<String, Class<? extends Node>> entry : this.factory.entrySet())
        {
            factory.register(entry.getKey(), entry.getValue());
        }

        return factory;
    }

    public MapNodeFactory register(String type, Class<? extends Node> clazz)
    {
        this.factory.put(type, clazz);

        return this;
    }

    @Override
    public String getType(Node node)
    {
        String type = this.factory.inverse().get(node.getClass());

        if (type != null)
        {
            return type;
        }

        throw new IllegalStateException("Node " + node.getClass() + " is not part of event node system!");
    }

    @Override
    public Node create(String type)
    {
        Class<? extends Node> clazz = this.factory.get(type);

        if (clazz != null)
        {
            try
            {
                return clazz.getConstructor().newInstance();
            }
            catch (Exception e)
            {}
        }

        throw new IllegalStateException("Node type " + type + " is not part of event node system!");
    }
}
