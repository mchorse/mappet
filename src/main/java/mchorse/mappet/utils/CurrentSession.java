package mchorse.mappet.utils;

import mchorse.mappet.api.utils.IContentType;

import java.util.Objects;

public class CurrentSession
{
    public IContentType type;
    public String id = "";

    public IContentType activeType;
    public String activeId = "";

    public void set(IContentType type, String id)
    {
        this.type = type;
        this.id = id;
    }

    public void setActive(IContentType type, String id)
    {
        this.activeType = type;
        this.activeId = id;
    }

    public void reset()
    {
        this.set(null, "");
        this.setActive(null, "");
    }

    public boolean isEditing(IContentType type, String id)
    {
        return this.type == type && Objects.equals(this.id, id);
    }

    public boolean isActive(IContentType type, String id)
    {
        return this.activeType == type && Objects.equals(this.activeId, id);
    }
}