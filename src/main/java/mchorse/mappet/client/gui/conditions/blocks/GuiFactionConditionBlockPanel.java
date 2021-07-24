package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.FactionConditionBlock;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiComparisonElement;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiFactionConditionBlockPanel extends GuiAbstractConditionBlockPanel<FactionConditionBlock>
{
    public GuiButtonElement id;
    public GuiTargetElement target;
    public GuiComparisonElement comparison;
    public GuiCirculateElement faction;

    public GuiFactionConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, FactionConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.faction"), (t) -> this.openFactions());
        this.comparison = new GuiComparisonElement(mc, block.comparison);
        this.target = new GuiTargetElement(mc, block.target).skipGlobal().skip(TargetMode.NPC);
        this.faction = new GuiCirculateElement(mc, this::toggleFaction);
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.aggressive"));
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.passive"));
        this.faction.addLabel(IKey.lang("mappet.gui.faction_attitudes.friendly"));
        this.faction.addLabel(IKey.lang("mappet.gui.conditions.faction.score"));
        this.faction.setValue(block.faction.ordinal());

        this.add(Elements.row(mc, 5,
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.faction.id")).marginTop(12), this.id),
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.faction.check")).marginTop(12), this.faction)
        ));
        this.add(this.target.marginTop(12));
        this.add(this.comparison.marginTop(12));
    }

    private void openFactions()
    {
        GuiMappetUtils.openPicker(ContentType.FACTION, this.block.id, (name) -> this.block.id = name);
    }

    private void toggleFaction(GuiCirculateElement b)
    {
        this.block.faction = FactionConditionBlock.FactionCheck.values()[b.getValue()];
    }
}