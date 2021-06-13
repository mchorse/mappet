package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.DataNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiDataNodePanel <T extends DataNode> extends GuiEventBaseNodePanel<T>
{
    public GuiButtonElement pick;
    public GuiTextElement data;

    public GuiDataNodePanel(Minecraft mc, GuiMappetDashboardPanel parentPanel)
    {
        super(mc);

        this.pick = new GuiButtonElement(mc, this.getLabel(), (b) -> this.openOverlay());

        this.data = GuiMappetUtils.fullWindowContext(
            new GuiTextElement(mc, 10000, (text) -> this.node.customData = text),
            IKey.lang("mappet.gui.nodes.event.data")
        );

        this.add(this.pick);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.data, this.binary);
    }

    protected abstract IKey getLabel();

    protected abstract ContentType getType();

    protected void openOverlay()
    {
        GuiMappetUtils.openPicker(this.getType(), this.node.dataId, this::setString);
    }

    private void setString(String string)
    {
        this.node.dataId = string;
    }

    @Override
    public void set(T node)
    {
        super.set(node);

        this.data.setText(node.customData);
    }
}