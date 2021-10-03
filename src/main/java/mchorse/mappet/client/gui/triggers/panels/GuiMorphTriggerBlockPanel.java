package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.MorphTriggerBlock;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiMorphTriggerBlockPanel extends GuiAbstractTriggerBlockPanel<MorphTriggerBlock>
{
    public GuiTargetElement target;
    public GuiNestedEdit morph;

    public GuiMorphTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, MorphTriggerBlock block)
    {
        super(mc, overlay, block);

        this.target = new GuiTargetElement(mc, block.target).skipGlobal();
        this.morph = new GuiNestedEdit(mc, (editing) ->
        {
            AbstractMorph m = MorphManager.INSTANCE.morphFromNBT(this.block.morph);

            GuiMappetDashboard.get(mc).openMorphMenu(overlay.getParent(), editing, m, (morph) ->
            {
                this.block.morph = MorphUtils.toNBT(morph);
                this.morph.setMorph(morph);
            });
        });
        this.morph.setMorph(MorphManager.INSTANCE.morphFromNBT(this.block.morph));

        this.add(this.morph);
        this.add(this.target.marginTop(12));
    }
}