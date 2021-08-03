package mchorse.mappet.api.ui;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.ui.PacketUIData;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class UIContext
{
    public NBTTagCompound data = new NBTTagCompound();
    public EntityPlayer player;

    @SideOnly(Side.CLIENT)
    public Map<String, GuiElement> elements = new HashMap<String, GuiElement>();

    private String last = "";
    private boolean closed;

    public UIContext()
    {}

    public UIContext(EntityPlayer player)
    {
        this.player = player;
    }

    public String getLast()
    {
        return this.last;
    }

    @SideOnly(Side.CLIENT)
    public void buttonPressed(String id)
    {
        if (id.isEmpty())
        {
            return;
        }

        this.last = id;
        this.data.setInteger(id, this.data.getInteger(id) + 1);
        this.sendToServer();
    }

    @SideOnly(Side.CLIENT)
    private void sendToServer()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Data", this.data.copy());
        tag.setString("Last", this.last);

        Dispatcher.sendToServer(new PacketUIData(tag));
    }
}