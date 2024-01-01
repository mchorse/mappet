package mchorse.mappet.network.common.content;

import mchorse.mappet.api.utils.IContentType;

public class PacketContentRequestNames extends PacketContentBase
{
    public PacketContentRequestNames()
    {
        super();
    }

    public PacketContentRequestNames(IContentType type)
    {
        super(type);
    }

    public PacketContentRequestNames(IContentType type, int requestId)
    {
        super(type, requestId);
    }
}