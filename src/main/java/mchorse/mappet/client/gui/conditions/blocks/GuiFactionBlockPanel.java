package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.conditions.blocks.FactionBlock;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiPropertyBlockElement;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiFactionBlockPanel extends GuiAbstractBlockPanel<FactionBlock>
{
    public GuiButtonElement id;
    public GuiPropertyBlockElement property;
    public GuiCirculateElement faction;

    public GuiFactionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, FactionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.faction"), (t) -> this.openFactions());
        this.property = new GuiPropertyBlockElement(mc, block);
        this.property.skipGlobal().skip(Target.NPC);
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
        this.add(this.property.targeter.marginTop(12));
        this.add(Elements.row(mc, 5,
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.comparison")), this.property.comparison),
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.value")), this.property.value)
        ).marginTop(12));
    }

    private void openFactions()
    {
        ClientProxy.requestNames(ContentType.FACTION, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.faction"), ContentType.FACTION, names, (name) -> this.block.id = name);

            overlay.set(this.block.id);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void toggleFaction(GuiButtonElement b)
    {
        this.block.faction = FactionBlock.FactionCheck.values()[this.faction.getValue()];
    }
}