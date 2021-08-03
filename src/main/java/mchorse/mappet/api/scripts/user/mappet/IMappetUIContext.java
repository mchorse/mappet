package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;

public interface IMappetUIContext
{
    public INBTCompound getData();

    public boolean isClosed();

    public String getLast();
}