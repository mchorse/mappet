package mchorse.mappet.api.utils.factory;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of factory based on a map of
 * String key and Class value
 */
public class MapFactory <T> implements IFactory<T>
{
    private BiMap<String, Class<? extends T>> factory = HashBiMap.create();
    private Map<Class<? extends T>, Integer> colors = new HashMap<Class<? extends T>, Integer>();

    public MapFactory<T> copy()
    {
        MapFactory<T> factory = new MapFactory<T>();

        for (Map.Entry<String, Class<? extends T>> entry : this.factory.entrySet())
        {
            factory.register(entry.getKey(), entry.getValue(), this.colors.get(entry.getValue()));
        }

        return factory;
    }

    public MapFactory<T> register(String type, Class<? extends T> clazz, int color)
    {
        this.factory.put(type, clazz);
        this.colors.put(clazz, color);

        return this;
    }

    public MapFactory<T> unregister(String key)
    {
        Class<? extends T> clazz = this.factory.remove(key);

        this.colors.remove(clazz);

        return this;
    }

    @Override
    public String getType(T node)
    {
        String type = this.factory.inverse().get(node.getClass());

        if (type != null)
        {
            return type;
        }

        throw new IllegalStateException("Node " + node.getClass() + " is not part of event node system!");
    }

    @Override
    public T create(String type)
    {
        Class<? extends T> clazz = this.factory.get(type);

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

    @Override
    public int getColor(T object)
    {
        Integer color = this.colors.get(object.getClass());

        return color == null ? 0 : color;
    }

    @Override
    public int getColor(String type)
    {
        Integer color = this.colors.get(this.factory.get(type));

        return color == null ? 0 : color;
    }

    @Override
    public Collection<String> getKeys()
    {
        return this.factory.keySet();
    }
}
