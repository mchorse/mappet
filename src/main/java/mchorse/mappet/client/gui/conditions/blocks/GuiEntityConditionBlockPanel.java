package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.EntityConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiComparisonElement;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mappet.client.gui.utils.overlays.GuiLabelOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

public class GuiEntityConditionBlockPanel extends GuiAbstractConditionBlockPanel<EntityConditionBlock>
{
    public GuiButtonElement id;
    public GuiTargetElement target;
    public GuiComparisonElement comparison;

    public GuiEntityConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, EntityConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.conditions.entity.id"), this::openProperties);
        this.target = new GuiTargetElement(mc, block.target).skipGlobal();
        this.comparison = new GuiComparisonElement(mc, block.comparison);

        this.add(this.id, this.target.marginTop(12), this.comparison.marginTop(12));
    }

    private void openProperties(GuiButtonElement b)
    {
        Set<Label<String>> labels = new HashSet<Label<String>>();

        for (String property : EntityUtils.ENTITY_PROPERTIES)
        {
            labels.add(new Label<String>(IKey.lang("mappet.gui.entity_property." + property), property));
        }

        IKey title = IKey.lang("mappet.gui.conditions.entity.overlay.main");
        GuiLabelOverlayPanel<String> overlay = new GuiLabelOverlayPanel<String>(this.mc, title, labels, (l) -> this.block.id = l.value);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.block.id), 0.4F, 0.5F);
    }
}