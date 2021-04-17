package mchorse.mappet.client.gui;

import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiTriggerBlockScreen extends GuiBase
{
    public GuiTriggerElement left;
    public GuiTriggerElement right;

    private BlockPos pos;

    public GuiTriggerBlockScreen(BlockPos pos, Trigger left, Trigger right)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();
        GuiElement element = new GuiElement(mc);

        element.flex().relative(this.viewport).xy(0.5F, 0.5F).w(0.5F).anchor(0.5F, 0.5F).column(5).vertical().stretch();

        this.left = new GuiTriggerElement(mc);
        this.left.flex().relative(this.viewport).x(0.5F).y(0.5F).w(0.5F).anchor(0.5F, 0.5F);
        this.left.set(left);

        this.right = new GuiTriggerElement(mc);
        this.right.flex().relative(this.left).y(1F, 20).w(1F);
        this.right.set(right);

        element.add(Elements.label(IKey.str("Trigger event on left click"), 14).background(0x88000000), this.left);
        element.add(Elements.label(IKey.str("Trigger event on right click"), 26).anchor(0, 0.75F).background(0x88000000), this.right);

        this.root.add(element);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketEditTrigger(this.pos, this.left.get().serializeNBT(), this.right.get().serializeNBT()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}