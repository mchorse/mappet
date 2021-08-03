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

    public void setLast(String id)
    {
        this.last = id;
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

    public void dirty(long delay)
    {
        this.dirty = System.currentTimeMillis() + delay;
    }

    public void handleNewData(NBTTagCompound data)
    {
        if (this.player == null)
        {
            return;
        }

        this.data = data.getCompoundTag("Data");
        this.last = data.getString("Last");

        this.handleScript(new DataContext(this.player));
    }

    public void close()
    {
        if (this.player == null)
        {
            return;
        }

        this.closed = true;

        this.handleScript(new DataContext(this.player));
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
    public void sendToServer()
    {
        this.dirty = null;

        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Data", this.data.copy());
        tag.setString("Last", this.last);

        Dispatcher.sendToServer(new PacketUIData(tag));
    }

    private void handleScript(DataContext context)
    {
        if (!this.script.isEmpty() && !this.function.isEmpty())
        {
            try
            {
                Mappet.scripts.execute(this.script, this.function, context);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}