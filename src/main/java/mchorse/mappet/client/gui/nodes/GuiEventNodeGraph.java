package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.factory.IFactory;
import mchorse.mappet.utils.Colors;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiEventNodeGraph extends GuiNodeGraph<EventNode>
{
    public GuiEventNodeGraph(Minecraft mc, IFactory<EventNode> factory, Consumer<EventNode> callback)
    {
        super(mc, factory, callback);
    }

    @Override
    protected int getIndexLabelColor(EventNode lastSelected, int i)
    {
        return lastSelected.binary && i >= 2 ? 0x666666 : 0xffffff;
    }

    @Override
    protected int getNodeActiveColor(EventNode output, int r)
    {
        if (output.binary)
        {
            return r == 0 ? Colors.POSITIVE : (r == 1 ? Colors.NEGATIVE : Colors.INACTIVE);
        }

        return super.getNodeActiveColor(output, r);
    }

    @Override
    protected float getNodeActiveColorOpacity(EventNode output, int r)
    {
        if (output.binary && r >= 2)
        {
            return 0.25F;
        }

        return super.getNodeActiveColorOpacity(output, r);
    }
}