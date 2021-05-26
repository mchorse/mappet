package mchorse.mappet.utils;

import mchorse.mappet.api.utils.ContentType;

import java.util.Objects;

public class CurrentSession
{
    public ContentType type;
    public String id = "";

    public ContentType activeType;
    public String activeId = "";

    public void set(ContentType type, String id)
    {
        this.type = type;
        this.id = id;
    }

    public void setActive(ContentType type, String id)
    {
        this.activeType = type;
        this.activeId = id;
    }

    public void reset()
    {
        this.set(null, "");
        this.setActive(null, "");
    }

    public boolean isEditing(ContentType type, String id)
    {
        return this.type == type && Objects.equals(this.id, id);
    }

    public boolean isActive(ContentType type, String id)
    {
        return this.activeType == type && Objects.equals(this.activeId, id);
    }
}