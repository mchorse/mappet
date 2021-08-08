package mchorse.mappet.client.gui;

import mchorse.mappet.api.ui.UI;
import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

public class GuiUserInterface extends GuiBase
{
    private UIContext context;

    public GuiUserInterface(Minecraft mc, UI ui)
    {
        this.context = new UIContext(ui);

        GuiElement element = ui.root.create(mc, this.context);

        element.flex().relative(this.root).wh(1F, 1F);
        this.root.add(element);
    }

    public void handleUIChanges(NBTTagCompound data)
    {
        for (String key : data.getKeySet())
        {
            NBTTagCompound tag = data.getCompoundTag(key);
            GuiElement element = this.context.getElement(key);

            this.context.getById(key).handleChanges(this.context, tag, element);
        }

        this.root.resize();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void closeScreen()
    {
        if (this.context.ui.closable)
        {
            super.closeScreen();
        }
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        Dispatcher.sendToServer(new PacketUI());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (this.context.isDirty())
        {
            this.context.sendToServer();
        }

        if (this.context.ui.background)
        {
            this.drawDefaultBackground();
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}