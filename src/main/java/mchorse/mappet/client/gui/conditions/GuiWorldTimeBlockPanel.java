package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.api.conditions.blocks.WorldTimeBlock;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiWorldTimeBlockPanel extends GuiAbstractBlockPanel<WorldTimeBlock>
{
    public GuiCirculateElement type;
    public GuiTrackpadElement min;
    public GuiTrackpadElement max;

    public GuiWorldTimeBlockPanel(Minecraft mc, WorldTimeBlock block)
    {
        super(mc, block);

        this.type = new GuiCirculateElement(mc, this::toggleMode);

        for (WorldTimeBlock.TimeCheck check : WorldTimeBlock.TimeCheck.values())
        {
            this.type.addLabel(IKey.lang(check.getKey()));
        }

        this.type.setValue(block.check.ordinal());

        this.min = new GuiTrackpadElement(mc, (v) -> this.block.min = v.intValue());
        this.min.limit(0, 24000, true).setValue(block.min);
        this.max = new GuiTrackpadElement(mc, (v) -> this.block.max = v.intValue());
        this.max.limit(0, 24000, true).setValue(block.max);

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.world_time.check")).marginTop(12), this.type);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.world_time.range")).marginTop(12), Elements.row(mc, 5, this.min, this.max));
    }

    private void toggleMode(GuiButtonElement b)
    {
        this.block.check = WorldTimeBlock.TimeCheck.values()[this.type.getValue()];
    }
}