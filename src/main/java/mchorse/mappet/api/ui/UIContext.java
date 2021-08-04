package mchorse.mappet.api.ui;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
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

    private String script = "";
    private String function = "";

    @SideOnly(Side.CLIENT)
    public Map<String, GuiElement> elements = new HashMap<String, GuiElement>();

    private String last = "";
    private boolean closed;
    private Long dirty;

    public UIContext()
    {}

    public UIContext(EntityPlayer player, String script, String function)
    {
        this.player = player;
        this.script = script == null ? "" : script;
        this.function = function == null ? "" : function;
    }

    public String getLast()
    {
        return this.last;
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    public boolean isDirty()
    {
        if (this.dirty == null)
        {
            return false;
        }

        return System.currentTimeMillis() >= this.dirty;
    }

    /* Client side code */

    @SideOnly(Side.CLIENT)
    public void dirty(String id, long delay)
    {
        this.last = id;

        if (delay <= 0)
        {
            this.dirty = null;
            this.sendToServer();
        }
        else
        {
            this.dirty = System.currentTimeMillis() + delay;
        }
    }

    @SideOnly(Side.CLIENT)
    public void sendToServer()
    {
        this.dirty = null;

        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Data", this.data);
        tag.setString("Last", this.last);

        this.data = new NBTTagCompound();

        Dispatcher.sendToServer(new PacketUIData(tag));
    }

    /* Server side code */

    public void handleNewData(NBTTagCompound data)
    {
        if (this.player == null)
        {
            return;
        }

        this.data.merge(data.getCompoundTag("Data"));
        this.last = data.getString("Last");

        this.handleScript(this.player);
    }

    public void close()
    {
        if (this.player == null)
        {
            return;
        }

        this.closed = true;

        this.handleScript(this.player);
    }

    private void handleScript(EntityPlayer player)
    {
        if (this.script.isEmpty() || this.function.isEmpty())
        {
            return;
        }

        try
        {
            Mappet.scripts.execute(this.script, this.function, new DataContext(player));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}