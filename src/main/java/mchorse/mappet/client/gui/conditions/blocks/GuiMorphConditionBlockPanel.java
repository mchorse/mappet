package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.MorphConditionBlock;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiMorphConditionBlockPanel extends GuiAbstractConditionBlockPanel<MorphConditionBlock>
{
    public GuiTargetElement target;
    public GuiNestedEdit morph;
    public GuiToggleElement onlyName;

    public GuiMorphConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, MorphConditionBlock block)
    {
        super(mc, overlay, block);

        this.target = new GuiTargetElement(mc, block.target).skipGlobal();

        this.morph = new GuiNestedEdit(mc, (editing) ->
        {
            AbstractMorph m = MorphManager.INSTANCE.morphFromNBT(this.block.morph);

            GuiMappetDashboard.get(mc).openMorphMenu(overlay.getParent(), editing, m, (morph) ->
            {
                this.block.morph = morph.toNBT();
                this.morph.setMorph(morph);
            });
        });
        this.morph.setMorph(MorphManager.INSTANCE.morphFromNBT(this.block.morph));

        this.onlyName = new GuiToggleElement(mc, IKey.lang("mappet.gui.conditions.morph.only_name"), (b) -> this.block.onlyName = b.isToggled());
        this.onlyName.toggled(this.block.onlyName).tooltip(IKey.lang("mappet.gui.conditions.morph.only_name_tooltip"));

        this.add(this.morph, this.onlyName);
        this.add(this.target.marginTop(12));
    }
}