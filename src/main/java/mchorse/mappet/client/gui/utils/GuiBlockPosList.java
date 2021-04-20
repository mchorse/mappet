package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
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
        BlockPos pos = new BlockPos(this.mc.player);

        this.posList.add(pos);
        this.add(this.create(pos));

        this.getParentContainer().resize();
    }

    public void set(List<BlockPos> posList)
    {
        this.posList = posList;

        this.removeAll();

        for (BlockPos pos : posList)
        {
            GuiBlockPosElement posElement = this.create(pos);

            this.add(posElement);
        }

        this.getParentContainer().resize();
    }

    private GuiBlockPosElement create(BlockPos pos)
    {
        GuiBlockPosElement posElement = new GuiBlockPosElement(this.mc, null);

        posElement.set(pos);
        posElement.callback = (blockPos) ->
        {
            this.posList.set(this.getChildren().indexOf(posElement), blockPos);
        };
        posElement.context(() ->
        {
            return new GuiSimpleContextMenu(this.mc).action(Icons.REMOVE, IKey.str("Remove block position"), () -> this.removeBlock(posElement));
        });

        return posElement;
    }

    private void removeBlock(GuiBlockPosElement element)
    {
        int index = this.getChildren().indexOf(element);

        this.remove(element);
        this.posList.remove(index);

        this.getParentContainer().resize();
    }
}