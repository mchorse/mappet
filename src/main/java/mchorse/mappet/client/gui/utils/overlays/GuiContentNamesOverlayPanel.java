package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Collection;
import java.util.function.Consumer;

public class GuiContentNamesOverlayPanel extends GuiStringOverlayPanel
{
    public GuiIconElement edit;

    private ContentType type;

    public GuiContentNamesOverlayPanel(Minecraft mc, IKey title, ContentType type, Collection<String> strings, Consumer<String> callback)
    {
        super(mc, title, strings, callback);

        this.type = type;

        this.edit = new GuiIconElement(mc, Icons.EDIT, (b) -> this.edit(this.getValue()));
        this.edit.flex().wh(16, 16);

        this.icons.add(this.edit);
    }

    private void edit(String text)
    {
        this.close();

        GuiMappetDashboard dashboard = GuiMappetDashboard.get(this.mc);

        this.openPanel(text, dashboard, this.type.get(dashboard));
    }

    private void openPanel(String text, GuiMappetDashboard dashboard, GuiMappetDashboardPanel panel)
    {
        if (!text.isEmpty())
        {
            this.openDashboard(dashboard);

            dashboard.panels.setPanel(panel);
            panel.pickData(text);
            panel.names.list.setCurrentScroll(text);
        }
    }

    private void openDashboard(GuiMappetDashboard dashboard)
    {
        if (!(this.mc.currentScreen instanceof GuiMappetDashboard))
        {
            this.mc.displayGuiScreen(dashboard);
        }
    }
}