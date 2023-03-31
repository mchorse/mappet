package mchorse.mappet.client.gui.conditionModel;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mappet.client.gui.utils.GuiMorphRenderer;
import mchorse.mappet.utils.ConditionModel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiConditionModelElement extends GuiElement
{
    public GuiCheckerElement checker;
    public ConditionModel conditionModel;
    public GuiNestedEdit morph;
    public GuiMorphRenderer renderer;

    public GuiConditionModelElement(Minecraft mc)
    {
        super(mc);

        this.checker = new GuiCheckerElement(mc);
        this.renderer = new GuiMorphRenderer(mc);
        this.morph = new GuiNestedEdit(mc, (editing) -> GuiMappetDashboard.get(mc).openMorphMenu(this.parent, editing, this.renderer.morph.copy(), (morph) ->
        {
            AbstractMorph copy = MorphUtils.copy(morph);
            this.conditionModel.morph = copy;
            renderer.morph.setDirect(copy);
        }));

        this.renderer.flex().relative(this).w(1F, -40).h(1F, -90);

        GuiElement frame = new GuiElement(mc);
        frame.flex().relative(this).wh(1F, 1F).column(5).vertical().stretch().padding(20);
        frame.add(this.renderer, this.checker, this.morph);
        this.add(frame);
    }

    public void set(ConditionModel conditionModel)
    {
        this.conditionModel = conditionModel;
        this.checker.set(this.conditionModel.checker);
        this.renderer.morph.set(this.conditionModel.morph);
    }
}
