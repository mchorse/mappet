package mchorse.mappet.client.gui.utils;

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

    public GuiBlockPosList(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).stretch().vertical();
    }

    public void addBlockPos()
    {
        this.posList.add(new BlockPos(0, 0, 0));
        this.add(this.create());

        this.getParentContainer().resize();
    }

    public void set(List<BlockPos> posList)
    {
        this.posList = posList;

        this.removeAll();

        for (BlockPos pos : posList)
        {
            GuiSteeringOffsetElement posElement = this.create();

            posElement.position.set(pos); // Set the position of the element

            this.add(posElement);
        }

        this.getParentContainer().resize();
    }

    private GuiSteeringOffsetElement create()
    {
        GuiSteeringOffsetElement element = new GuiSteeringOffsetElement(this.mc);

        element.position.callback = (blockPos) ->
        {
            this.posList.set(this.getChildren().indexOf(element), blockPos);
        };
        element.position.context(() ->
        {
            return element.position.createDefaultContextMenu(false).action(Icons.REMOVE, IKey.lang("mappet.gui.block_pos.context.remove"), () -> this.removeBlock(element), Colors.NEGATIVE);
        });

        return element;
    }

    private void removeBlock(GuiSteeringOffsetElement element)
    {
        int index = this.getChildren().indexOf(element);

        this.remove(element);
        this.posList.remove(index);

        this.getParentContainer().resize();
    }
}