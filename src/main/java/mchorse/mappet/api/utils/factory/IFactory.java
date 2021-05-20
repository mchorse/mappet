package mchorse.mappet.api.utils.factory;

import java.util.Collection;

public interface IFactory <T>
{
    public String getType(T object);

    public T create(String type);

    public int getColor(T object);

    public int getColor(String type);

    public Collection<String> getKeys();
}