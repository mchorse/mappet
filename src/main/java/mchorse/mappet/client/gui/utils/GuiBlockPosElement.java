package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class GuiBlockPosElement extends GuiElement
{
    public GuiTrackpadElement x;
    public GuiTrackpadElement y;
    public GuiTrackpadElement z;

    public Consumer<BlockPos> callback;

    public GuiBlockPosElement(Minecraft mc, Consumer<BlockPos> callback)
    {
        super(mc);

        this.callback = callback;
        this.x = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.x.integer();
        this.y = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.y.integer();
        this.z = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.z.integer();

        this.flex().row(5);
        this.add(this.x, this.y, this.z);

        this.context(() -> new GuiSimpleContextMenu(this.mc).action(Icons.MOVE_TO, IKey.str("Paste your current position"), this::pastePosition));
    }

    private void pastePosition()
    {
        this.set(new BlockPos(this.mc.player));
        this.callback();
    }

    protected void callback()
    {
        if (this.callback != null)
        {
            this.callback.accept(this.get());
        }
    }

    private BlockPos get()
    {
        return new BlockPos((int) this.x.value, (int) this.y.value, (int) this.z.value);
    }

    public void set(BlockPos pos)
    {
        if (pos == null)
        {
            pos = BlockPos.ORIGIN;
        }

        this.x.setValue(pos.getX());
        this.y.setValue(pos.getY());
        this.z.setValue(pos.getZ());
    }
}