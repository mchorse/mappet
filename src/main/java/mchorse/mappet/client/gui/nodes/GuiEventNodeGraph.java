package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.utils.Colors;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiEventNodeGraph extends GuiNodeGraph<EventBaseNode>
{
    public GuiEventNodeGraph(Minecraft mc, IFactory<EventBaseNode> factory, Consumer<EventBaseNode> callback)
    {
        super(mc, factory, callback);
    }

    @Override
    protected int getIndexLabelColor(EventBaseNode lastSelected, int i)
    {
        return lastSelected.binary && i >= 2 ? 0x666666 : 0xffffff;
    }

    @Override
    protected int getNodeActiveColor(EventBaseNode output, int r)
    {
        if (output.binary)
        {
            return r == 0 ? Colors.POSITIVE : (r == 1 ? Colors.NEGATIVE : Colors.INACTIVE);
        }

        return super.getNodeActiveColor(output, r);
    }

    @Override
    protected float getNodeActiveColorOpacity(EventBaseNode output, int r)
    {
        if (output.binary && r >= 2)
        {
            return 0.25F;
        }

        return super.getNodeActiveColorOpacity(output, r);
    }
}