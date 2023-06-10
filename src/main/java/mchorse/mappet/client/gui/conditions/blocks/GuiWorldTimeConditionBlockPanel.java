package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.WorldTimeConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiWorldTimeConditionBlockPanel extends GuiAbstractConditionBlockPanel<WorldTimeConditionBlock>
{
    public GuiCirculateElement type;
    public GuiTrackpadElement min;
    public GuiTrackpadElement max;

    private GuiElement[] elements;

    public GuiWorldTimeConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, WorldTimeConditionBlock block)
    {
        super(mc, overlay, block);

        this.type = new GuiCirculateElement(mc, this::toggleMode);

        for (WorldTimeConditionBlock.TimeCheck check : WorldTimeConditionBlock.TimeCheck.values())
        {
            this.type.addLabel(IKey.lang(check.getKey()));
        }

        this.type.setValue(block.check.ordinal());

        this.min = new GuiTrackpadElement(mc, (v) -> this.block.min = v.intValue());
        this.min.limit(0, 24000, true).setValue(block.min);
        this.max = new GuiTrackpadElement(mc, (v) -> this.block.max = v.intValue());
        this.max.limit(0, 24000, true).setValue(block.max);

        GuiElement a = Elements.label(IKey.lang("mappet.gui.conditions.world_time.range")).marginTop(12);
        GuiElement b = Elements.row(mc, 5, this.min, this.max);

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.world_time.check")).marginTop(12), this.type);
        this.add(a, b);

        this.elements = new GuiElement[] {a, b};
        this.toggleMode(this.type);
    }

    private void toggleMode(GuiCirculateElement b)
    {
        this.block.check = WorldTimeConditionBlock.TimeCheck.values()[b.getValue()];

        for (GuiElement element : this.elements)
        {
            element.setVisible(this.block.check == WorldTimeConditionBlock.TimeCheck.RANGE);
        }
    }
}