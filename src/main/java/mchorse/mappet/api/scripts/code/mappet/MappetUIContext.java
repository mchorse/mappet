package mchorse.mappet.api.scripts.code.mappet;

import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.api.ui.components.UIComponent;

public class MappetUIContext implements IMappetUIContext
{
    private UIContext context;
    private INBTCompound data;

    public MappetUIContext(UIContext context)
    {
        this.context = context;
    }

    @Override
    public INBTCompound getData()
    {
        if (this.data == null)
        {
            this.data = new ScriptNBTCompound(this.context.data);
        }

        return this.data;
    }

    @Override
    public boolean isClosed()
    {
        return this.context.isClosed();
    }

    @Override
    public String getLast()
    {
        return this.context.getLast();
    }

    @Override
    public String getHotkey()
    {
        return this.context.getHotkey();
    }

    @Override
    public String getContext()
    {
        return this.context.getContext();
    }

    @Override
    public UIComponent get(String id)
    {
        return this.context.getById(id);
    }

    @Override
    public void sendToPlayer()
    {
        this.context.sendToPlayer();
    }
}