package mchorse.mappet.api.scripts.user.mappet;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.ui.components.UIComponent;

public interface IMappetUIContext
{
    public INBTCompound getData();

    public boolean isClosed();

    public String getLast();

    /* Server side modification */

    public UIComponent get(String id);

    public void sendToPlayer();
}