package mchorse.mappet.client.gui;

import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.client.gui.utils.GuiCheckerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class GuiEmitterBlockScreen extends GuiBase
{
    public GuiCheckerElement checker;
    public GuiTrackpadElement radius;

    private BlockPos pos;

    public GuiEmitterBlockScreen(BlockPos pos, Checker checker, float radius)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();

        /* TODO: tooltips */
        this.checker = new GuiCheckerElement(mc, checker);
        this.checker.flex().relative(this.viewport).x(0.5F).y(0.5F).w(0.5F).anchor(0.5F, 0.5F);

        this.radius = new GuiTrackpadElement(mc, (Consumer<Double>) null);
        this.radius.flex().relative(this.checker).y(1F, 20).w(1F);
        this.radius.limit(0).setValue(radius);

        this.root.add(this.checker, this.radius);
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

        Dispatcher.sendToServer(new PacketEditEmitter(this.pos, this.checker.get().toNBT(), (float) this.radius.value));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.emitter_block.expression"), this.checker.area.x, this.checker.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.emitter_block.radius"), this.radius.area.x, this.radius.area.y - 12, 0xffffff);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}