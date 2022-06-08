package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class GuiBlockPosList extends GuiElement
{
    private List<BlockPos> posList;

    private List<Trigger> triggerList;

    public GuiBlockPosList(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).stretch().vertical();
    }

    public void addBlockPos()
    {
        BlockPos pos = new BlockPos(this.mc.player);
        Trigger trigger = new Trigger();

        this.posList.add(pos);
        this.triggerList.add(trigger);
        this.add(this.create(pos, trigger));

        this.getParentContainer().resize();
    }

    public void set(List<BlockPos> posList, List<Trigger> triggerList)
    {
        this.posList = posList;
        this.triggerList = triggerList;

        this.removeAll();

        for (int i = 0; i < posList.size(); i++)
        {
            BlockPos pos = posList.get(i);

            /* backward compatibility */
            if (i >= triggerList.size())
            {
                triggerList.add(new Trigger());
            }

            Trigger trigger = triggerList.get(i);

            GuiPatrolPointElement posElement = this.create(pos, trigger);

            this.add(posElement);
        }

        this.getParentContainer().resize();
    }

    private GuiPatrolPointElement create(BlockPos pos, Trigger trigger)
    {
        GuiPatrolPointElement element = new GuiPatrolPointElement(this.mc);

        element.position.set(pos);
        element.trigger.set(trigger);
        element.position.callback = (blockPos) ->
        {
            this.posList.set(this.getChildren().indexOf(element), blockPos);
        };
        element.position.context(() ->
        {
            return element.position.createDefaultContextMenu().action(Icons.REMOVE, IKey.lang("mappet.gui.block_pos.context.remove"), () -> this.removeBlock(element), Colors.NEGATIVE);
        });

        return element;
    }

    private void removeBlock(GuiPatrolPointElement element)
    {
        int index = this.getChildren().indexOf(element);

        this.remove(element);
        this.posList.remove(index);
        this.triggerList.remove(index);

        this.getParentContainer().resize();
    }
}