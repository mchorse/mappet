package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiEventNodePanel extends GuiDataNodePanel<EventNode>
{
    public GuiEventNodePanel(Minecraft mc)
    {
        super(mc);
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.event");
    }

    @Override
    protected ContentType getType()
    {
        return ContentType.EVENT;
    }
}