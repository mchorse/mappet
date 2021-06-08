package mchorse.mappet.client.gui;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class GuiTriggerBlockScreen extends GuiBase
{
    public GuiTriggerElement left;
    public GuiTriggerElement right;
    public GuiToggleElement collidable;

    private BlockPos pos;

    public GuiTriggerBlockScreen(BlockPos pos, Trigger left, Trigger right, boolean collidable)
    {
        super();

        this.pos = pos;

        Minecraft mc = Minecraft.getMinecraft();
        GuiElement element = new GuiElement(mc);

        element.flex().relative(this.viewport).xy(0.5F, 0.5F).w(0.5F).anchor(0.5F, 0.5F).column(5).vertical().stretch();

        this.left = new GuiTriggerElement(mc);
        this.left.set(left);

        this.right = new GuiTriggerElement(mc);
        this.right.set(right);

        this.collidable = new GuiToggleElement(mc, IKey.lang("mappet.gui.trigger_block.collidable"), null);
        this.collidable.toggled(collidable);

        element.add(Elements.label(IKey.lang("mappet.gui.trigger_block.left")).background().marginBottom(5), this.left);
        element.add(Elements.label(IKey.lang("mappet.gui.trigger_block.right")).background().marginTop(12).marginBottom(5), this.right, this.collidable.marginTop(6));

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

        Dispatcher.sendToServer(new PacketEditTrigger(this.pos, this.left.get().serializeNBT(), this.right.get().serializeNBT(), this.collidable.isToggled()));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}