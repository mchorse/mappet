package mchorse.mappet.client.gui.utils;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;

import java.util.function.Consumer;

public class GuiVecPosElement extends GuiElement
{
    public GuiTrackpadElement x;
    public GuiTrackpadElement y;
    public GuiTrackpadElement z;

    public Consumer<Vec3d> callback;

    public GuiVecPosElement(Minecraft mc, Consumer<Vec3d> callback)
    {
        super(mc);

        this.callback = callback;
        this.x = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.y = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.z = new GuiTrackpadElement(mc, (v) -> this.callback());

        this.flex().row(5);
        this.add(this.x, this.y, this.z);

        this.context(this::createDefaultContextMenu);
    }

    public GuiSimpleContextMenu createDefaultContextMenu()
    {
        return new GuiSimpleContextMenu(this.mc).action(Icons.MOVE_TO, IKey.lang("mappet.gui.block_pos.context.paste"), this::pastePosition);
    }

    private void pastePosition()
    {
        this.set(new Vec3d(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ));
        this.callback();
    }

    private void callback()
    {
        if (this.callback != null)
        {
            this.callback.accept(this.get());
        }
    }

    private Vec3d get()
    {
        return new Vec3d(this.x.value, this.y.value, this.z.value);
    }

    public void set(Vec3d pos)
    {
        this.x.setValue(pos.x);
        this.y.setValue(pos.y);
        this.z.setValue(pos.z);
    }
}
