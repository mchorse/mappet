package mchorse.mappet.client.gui;

import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class GuiEmitterBlockScreen extends GuiBase
{
    public GuiCheckerElement checker;
    public GuiTrackpadElement radius;
    public GuiTrackpadElement update;
    public GuiToggleElement disable;

    private BlockPos pos;

    public GuiEmitterBlockScreen(PacketEditEmitter message)
    {
        super();

        this.pos = message.pos;

        Minecraft mc = Minecraft.getMinecraft();

        this.checker = new GuiCheckerElement(mc, message.createChecker());

        this.radius = new GuiTrackpadElement(mc, (Consumer<Double>) null);
        this.radius.limit(0).setValue(message.radius);

        this.update = new GuiTrackpadElement(mc, (Consumer<Double>) null);
        this.update.limit(1).integer().setValue(message.update);

        this.disable = new GuiToggleElement(mc, IKey.lang("mappet.gui.emitter_block.disable"), null);
        this.disable.toggled(message.disable);
        this.disable.tooltip(IKey.lang("mappet.gui.emitter_block.disable_tootlip"));

        GuiElement frame = Elements.column(mc, 5,
            Elements.label(IKey.lang("mappet.gui.emitter_block.condition")),
            this.checker,
            Elements.row(mc, 5,
                Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.emitter_block.radius")), this.radius),
                Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.emitter_block.update")), this.update)
            ).marginTop(12),
            this.disable
        );

        frame.flex().relative(this.viewport).xy(0.5F, 0.5F).w(0.5F).anchor(0.5F, 0.5F);

        this.root.add(frame);
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

        Dispatcher.sendToServer(new PacketEditEmitter(this.pos, this.checker.get().toNBT(), (float) this.radius.value, (int) this.update.value, this.disable.isToggled()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}