package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.api.conditions.blocks.FactionBlock;
import mchorse.mappet.api.conditions.blocks.StateBlock;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiFactionBlockPanel extends GuiAbstractBlockPanel<FactionBlock>
{
    public GuiTextElement id;
    public GuiCirculateElement faction;
    public GuiCirculateElement target;
    public GuiCirculateElement comparison;
    public GuiTrackpadElement value;

    public GuiFactionBlockPanel(Minecraft mc, FactionBlock block)
    {
        super(mc, block);

        this.id = new GuiTextElement(mc, 1000, (t) -> this.block.id = t);
        this.id.setText(block.id);

        this.faction = new GuiCirculateElement(mc, this::toggleFaction);
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.aggressive"));
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.passive"));
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.friendly"));
        this.faction.addLabel(IKey.lang("mappet.gui.conditions.faction.score"));
        this.faction.setValue(block.faction.ordinal());

        this.target = new GuiCirculateElement(mc, this::toggleTarget);
        this.target.addLabel(IKey.lang("mappet.gui.conditions.targets.subject"));
        this.target.addLabel(IKey.lang("mappet.gui.conditions.targets.object"));
        this.target.setValue(block.target - 1);

        this.comparison = new GuiCirculateElement(mc, this::toggleComparison);

        for (Comparison comparison : Comparison.values())
        {
            this.comparison.addLabel(IKey.str(comparison.operation.sign));
        }

        this.comparison.setValue(block.comparison.ordinal());

        this.value = new GuiTrackpadElement(mc, (v) -> this.block.value = v.intValue());
        this.value.setValue(block.value);

        this.add(Elements.label(IKey.lang("mappet.gui.conditions.faction.id")).marginTop(12), this.id);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.faction.check")).marginTop(12), this.faction);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.target")).marginTop(12), this.target);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.comparison")).marginTop(12), this.comparison);
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.value")).marginTop(12), this.value);
    }

    private void toggleFaction(GuiButtonElement b)
    {
        this.block.faction = FactionBlock.FactionCheck.values()[this.faction.getValue()];
    }

    private void toggleTarget(GuiButtonElement b)
    {
        this.block.target = this.target.getValue() + 1;
    }

    private void toggleComparison(GuiButtonElement b)
    {
        this.block.comparison = Comparison.values()[this.comparison.getValue()];
    }
}