package mchorse.mappet.client.gui.regions;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.regions.Region;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.client.gui.utils.GuiCheckerElement;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;

public class GuiRegionEditor extends GuiElement
{
    public GuiToggleElement passable;
    public GuiCheckerElement enabled;
    public GuiTrackpadElement delay;
    public GuiTriggerElement onEnter;
    public GuiTriggerElement onExit;
    public GuiCirculateElement shape;
    public GuiShapeEditor shapeEditor;

    public GuiToggleElement writeState;
    public GuiElement stateOptions;
    public GuiTextElement state;
    public GuiCirculateElement target;
    public GuiToggleElement additive;

    private Region region;

    public GuiRegionEditor(Minecraft mc)
    {
        super(mc);

        this.passable = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.passable"), (b) -> this.region.passable = b.isToggled());
        this.enabled = new GuiCheckerElement(mc);
        this.delay = new GuiTrackpadElement(mc, (value) -> this.region.delay = value.intValue());
        this.onEnter = new GuiTriggerElement(mc);
        this.onExit = new GuiTriggerElement(mc);
        this.shape = new GuiCirculateElement(mc, this::changeShape);
        this.shape.flex().w(80);
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.box"));
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.sphere"));
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.cylinder"));
        this.shapeEditor = new GuiShapeEditor(mc);

        this.writeState = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.write_states"), (b) -> this.toggleStates());
        this.stateOptions = Elements.column(mc, 5);
        this.state = new GuiTextElement(mc, (t) -> this.region.state = t);
        this.target = new GuiCirculateElement(mc, (b) -> this.region.target = Target.values()[this.target.getValue()]);

        for (Target target : Target.values())
        {
            this.target.addLabel(IKey.lang("mappet.gui.conditions.targets." + target.name().toLowerCase()));

            if (!(target == Target.SUBJECT || target == Target.GLOBAL))
            {
                this.target.disable(target.ordinal());
            }
        }

        this.additive = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.additive"), (b) -> this.region.additive = b.isToggled());
        this.additive.tooltip(IKey.lang("mappet.gui.region.additive_tooltip"), Direction.TOP);

        this.add(this.passable);
        this.add(Elements.label(IKey.lang("mappet.gui.region.enabled")).marginTop(6), this.enabled);
        this.add(Elements.label(IKey.lang("mappet.gui.region.delay")).marginTop(12), this.delay);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_enter")).background().marginTop(12).marginBottom(5), this.onEnter);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_exit")).background().marginTop(12).marginBottom(5), this.onExit);
        this.add(Elements.label(IKey.lang("mappet.gui.region.shape")).background().marginTop(12).marginBottom(5), this.shape);
        this.add(this.shapeEditor);

        this.add(this.writeState.marginTop(12));
        this.stateOptions.add(Elements.label(IKey.lang("mappet.gui.conditions.state.id")).marginTop(6), this.state);
        this.stateOptions.add(this.target, this.additive);

        this.flex().column(5).vertical().stretch();
    }

    private void toggleStates()
    {
        this.region.writeState = this.writeState.isToggled();

        this.stateOptions.removeFromParent();

        if (this.region.writeState)
        {
            this.add(this.stateOptions);
        }

        this.getParentContainer().resize();
    }

    private void changeShape(GuiButtonElement element)
    {
        int value = this.shape.getValue();
        AbstractShape shape = null;

        if (value == 0)
        {
            shape = new BoxShape();
        }
        else if (value == 1)
        {
            shape = new SphereShape();
        }
        else if (value == 2)
        {
            shape = new CylinderShape();
        }

        if (shape != null)
        {
            shape.copyFrom(this.region.shape);
            this.shapeEditor.set(shape);

            this.region.shape = shape;
        }
    }

    public void set(Region region)
    {
        this.region = region;

        if (region != null)
        {
            this.passable.toggled(region.passable);
            this.enabled.set(region.enabled);
            this.delay.setValue(region.delay);
            this.onEnter.set(region.onEnter);
            this.onExit.set(region.onExit);
            this.shape.setValue(region.shape instanceof BoxShape ? 0 : (region.shape instanceof CylinderShape ? 2 : 1));
            this.shapeEditor.set(region.shape);

            this.writeState.toggled(region.writeState);
            this.state.setText(region.state);
            this.target.setValue(region.target.ordinal());
            this.additive.toggled(region.additive);

            this.toggleStates();
        }
    }
}
