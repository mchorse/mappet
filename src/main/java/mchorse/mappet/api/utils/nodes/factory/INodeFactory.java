package mchorse.mappet.api.utils.nodes.factory;

import mchorse.mappet.api.utils.nodes.Node;

public interface INodeFactory <T extends Node>
{
    public String getType(T node);

    public T create(String type);
}