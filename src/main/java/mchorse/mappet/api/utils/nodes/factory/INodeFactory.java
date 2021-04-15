package mchorse.mappet.api.utils.nodes.factory;

import mchorse.mappet.api.utils.nodes.Node;

import java.util.Collection;

public interface INodeFactory <T extends Node>
{
    public String getType(T node);

    public T create(String type);

    public Collection<String> getKeys();
}