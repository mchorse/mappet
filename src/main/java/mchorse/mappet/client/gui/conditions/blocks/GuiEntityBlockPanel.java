package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.EntityBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiPropertyBlockElement;
import mchorse.mappet.client.gui.utils.overlays.GuiLabelOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.HashSet;
import java.util.Set;

public class GuiEntityBlockPanel extends GuiAbstractBlockPanel<EntityBlock>
{
    public GuiButtonElement id;
    public GuiPropertyBlockElement property;

    public GuiEntityBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, EntityBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.conditions.entity.id"), this::openProperties);
        this.property = new GuiPropertyBlockElement(mc, block);
        this.property.skipGlobal();

        this.add(this.id, this.property.targeter.marginTop(12));
        this.add(this.property.compare.marginTop(12));
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