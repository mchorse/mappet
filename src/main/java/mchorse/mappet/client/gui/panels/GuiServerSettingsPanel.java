package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.misc.ServerSettings;
import mchorse.mappet.api.utils.Trigger;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketRequestServerSettings;
import mchorse.mappet.network.common.content.PacketServerSettings;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class GuiServerSettingsPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiScrollElement editor;

    private ServerSettings settings;

    public GuiServerSettingsPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.editor = new GuiScrollElement(mc);
        this.editor.flex().relative(this).wh(1F, 1F).column(5).scroll().width(180).padding(15);

        this.add(this.editor);
    }

    public void fill(NBTTagCompound tag)
    {
        this.settings = new ServerSettings(null);
        this.settings.deserializeNBT(tag);

        this.editor.removeAll();

        boolean first = true;

        for (Map.Entry<String, Trigger> entry : this.settings.registered.entrySet())
        {
            GuiTriggerElement trigger = new GuiTriggerElement(this.mc, entry.getValue());
            GuiLabel label = Elements.label(IKey.lang("mappet.gui.settings.triggers." + entry.getKey())).background();
            GuiElement element = Elements.column(this.mc, 5, label.marginBottom(4), trigger);

            if (!first)
            {
                element.marginTop(12);
            }

            this.editor.add(element);

            first = false;
        }

        this.resize();
    }

    public void save()
    {
        if (this.settings == null)
        {
            return;
        }

        Dispatcher.sendToServer(new PacketServerSettings(this.settings.serializeNBT()));
    }

    @Override
    public void appear()
    {
        super.appear();

        Dispatcher.sendToServer(new PacketRequestServerSettings());
    }

    @Override
    public void disappear()
    {
        super.disappear();

        this.save();
    }

    @Override
    public void close()
    {
        super.close();

        this.save();
    }
}