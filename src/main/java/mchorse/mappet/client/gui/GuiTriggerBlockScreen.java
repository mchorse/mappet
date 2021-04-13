package mchorse.mappet.client.gui;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiTriggerBlockScreen extends GuiBase
{
    public GuiTextElement left;
    public GuiTextElement right;

    private BlockPos pos;

    public GuiTriggerBlockScreen(BlockPos pos, String left, String right)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();

        /* TODO: tooltips */
        this.left = new GuiTextElement(mc, 10000, null);
        this.left.flex().relative(this.viewport).x(0.5F).y(0.5F).w(0.5F).anchor(0.5F, 0.5F);
        this.left.setText(left);

        this.right = new GuiTextElement(mc, 10000, null);
        this.right.flex().relative(this.left).y(1F, 20).w(1F);
        this.right.setText(right);

        this.root.add(this.left, this.right);
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

        Dispatcher.sendToServer(new PacketEditTrigger(this.pos, this.left.field.getText(), this.right.field.getText()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        /* TODO: extract */
        this.fontRenderer.drawStringWithShadow("Trigger event on left click", this.left.area.x, this.left.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow("Trigger event on right click", this.right.area.x, this.right.area.y - 12, 0xffffff);
    }
}