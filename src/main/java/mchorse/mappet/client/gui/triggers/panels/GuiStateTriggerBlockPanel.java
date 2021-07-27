package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.StateTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiStateTriggerBlockPanel extends GuiAbstractTriggerBlockPanel<StateTriggerBlock>
{
    public GuiTextElement id;
    public GuiTargetElement target;
    public GuiCirculateElement mode;

    public GuiLabel valueLabel;
    public GuiElement valueRow;
    public GuiIconElement convert;
    public GuiElement value;

    public GuiStateTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, StateTriggerBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiTextElement(mc, 1000, (v) -> this.block.string = v);
        this.target = new GuiTargetElement(mc, null);
        this.mode = new GuiCirculateElement(mc, this::toggleItemCheck);

        for (StateTriggerBlock.StateMode mode : StateTriggerBlock.StateMode.values())
        {
            this.mode.addLabel(IKey.lang("mappet.gui.state_trigger.mode." + mode.name().toLowerCase()));
        }

        this.valueLabel = Elements.label(IKey.lang("mappet.gui.conditions.value"));
        this.valueRow = Elements.row(mc, 0);
        this.convert = new GuiIconElement(mc, Icons.REFRESH, this::convert);

        this.id.setText(block.string);
        this.target.setTarget(block.target);
        this.mode.setValue(block.mode.ordinal());

        this.add(this.mode);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.state.id")).marginTop(12), this.id);
        this.add(this.target.marginTop(12));
        this.add(this.valueLabel.marginTop(12), this.valueRow);

        this.toggleItemCheck(this.mode);
        this.updateValue();
    }

    private void toggleItemCheck(GuiCirculateElement b)
    {
        this.block.mode = StateTriggerBlock.StateMode.values()[b.getValue()];
        this.updateValue();
    }

    private void convert(GuiIconElement element)
    {
        Object object = this.block.value;

        if (object instanceof String)
        {
            this.block.value = 0D;
        }
        else
        {
            this.block.value = "";
        }

        this.updateValue();
    }

    private void updateValue()
    {
        Object object = this.block.value;

        if (object instanceof String)
        {
            if (this.block.mode == StateTriggerBlock.StateMode.ADD)
            {
                this.block.value = object = 0D;
            }
            else
            {
                GuiTextElement element = new GuiTextElement(this.mc, 10000, this::updateString);

                element.setText((String) object);
                this.value = element;
            }
        }

        if (object instanceof Number)
        {
            GuiTrackpadElement element = new GuiTrackpadElement(this.mc, this::updateNumber);

            element.setValue(((Number) object).doubleValue());
            this.value = element;
        }

        this.valueLabel.setVisible(this.block.mode != StateTriggerBlock.StateMode.REMOVE);
        this.valueRow.removeAll();

        if (this.block.mode != StateTriggerBlock.StateMode.REMOVE)
        {
            this.valueRow.add(this.value);

            if (this.block.mode != StateTriggerBlock.StateMode.ADD)
            {
                this.valueRow.add(this.convert);
            }
        }

        if (this.hasParent())
        {
            this.getParentContainer().resize();
        }
    }

    private void updateString(String s)
    {
        this.block.value = s;
    }

    private void updateNumber(double v)
    {
        this.block.value = v;
    }
}