package mchorse.mappet.client.gui;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.PacketEditEmitter;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiEmitterBlockScreen extends GuiBase
{
    public GuiTextElement expression;

    private BlockPos pos;

    public GuiEmitterBlockScreen(BlockPos pos, String expression)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();

        this.expression = new GuiTextElement(mc, 10000, null);
        this.expression.flex().relative(this.viewport).x(0.5F).y(0.5F).w(0.5F).anchor(0.5F, 0.5F);
        this.expression.setText(expression);

        this.root.add(this.expression);
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketEditEmitter(this.pos, this.expression.field.getText()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.fontRenderer.drawStringWithShadow("Expression", this.expression.area.x, this.expression.area.y - 12, 0xffffff);
    }
}