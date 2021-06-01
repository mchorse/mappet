package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.scripts.Script;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiScriptPanel extends GuiMappetDashboardPanel<Script>
{
    public GuiTextEditor code;
    public GuiToggleElement unique;

    public GuiScriptPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.code = new GuiTextEditor(mc, (t) -> this.data.code = t);
        this.code.background().flex().relative(this.editor).wh(1F, 1F);

        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.meta.unique"), (b) -> this.data.unique = b.isToggled());
        this.unique.flex().relative(this.sidebar).x(10).y(1F, -10).w(1F, -20).anchorY(1F);

        this.names.flex().hTo(this.unique.area, -5);

        this.editor.add(this.code);
        this.sidebar.prepend(this.unique);

        this.fill(null);
    }

    @Override
    public ContentType getType()
    {
        return ContentType.SCRIPTS;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.scripts";
    }

    @Override
    public void fill(Script data, boolean allowed)
    {
        super.fill(data, allowed);

        this.editor.setVisible(data != null);
        this.unique.setVisible(data != null && allowed);

        if (data != null)
        {
            this.code.setText(data.code);
            this.unique.toggled(data.unique);
        }
    }
}