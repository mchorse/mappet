package mchorse.mappet.client.gui.regions;

import mchorse.mappet.api.regions.Region;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.client.gui.conditions.GuiCheckerElement;
import mchorse.mappet.client.gui.triggers.GuiTriggerElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiRegionEditor extends GuiElement
{
    public GuiToggleElement passable;
    public GuiCheckerElement enabled;
    public GuiTrackpadElement delay;
    public GuiTrackpadElement update;
    public GuiToggleElement checkEntities;
    public GuiTriggerElement onEnter;
    public GuiTriggerElement onExit;

    public GuiTriggerElement onTick;

    public GuiToggleElement writeState;
    public GuiElement stateOptions;
    public GuiTextElement state;
    public GuiCirculateElement target;
    public GuiToggleElement additive;
    public GuiToggleElement once;

    public GuiElement shapes;

    private Region region;

    public GuiRegionEditor(Minecraft mc)
    {
        super(mc);

        this.passable = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.passable"), (b) -> this.region.passable = b.isToggled());
        this.enabled = new GuiCheckerElement(mc);
        this.delay = new GuiTrackpadElement(mc, (value) -> this.region.delay = value.intValue()).limit(0).integer();
        this.update = new GuiTrackpadElement(mc, (value) -> this.region.update = value.intValue()).limit(1).integer();
        this.checkEntities = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.check_entities"), (b) -> this.region.checkEntities = b.isToggled());

        this.onEnter = new GuiTriggerElement(mc);
        this.onExit = new GuiTriggerElement(mc);
        this.onTick = new GuiTriggerElement(mc);

        this.writeState = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.write_states"), (b) -> this.toggleStates());
        this.stateOptions = Elements.column(mc, 5);
        this.state = new GuiTextElement(mc, (t) -> this.region.state = t);
        this.target = GuiMappetUtils.createTargetCirculate(mc, TargetMode.GLOBAL, (target) -> this.region.target = target);

        for (TargetMode target : TargetMode.values())
        {
            if (!(target == TargetMode.SUBJECT || target == TargetMode.GLOBAL))
            {
                this.target.disable(target.ordinal());
            }
        }

        this.additive = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.additive"), (b) -> this.region.additive = b.isToggled());
        this.additive.tooltip(IKey.lang("mappet.gui.region.additive_tooltip"));
        this.once = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.once"), (b) -> this.region.once = b.isToggled());
        this.once.tooltip(IKey.lang("mappet.gui.region.once_tooltip"));

        this.shapes = Elements.column(mc, 5);

        this.add(this.passable);
        this.add(this.checkEntities);
        this.add(Elements.label(IKey.lang("mappet.gui.region.enabled")).marginTop(6), this.enabled);
        this.add(Elements.label(IKey.lang("mappet.gui.region.delay")).marginTop(12), this.delay);
        this.add(Elements.label(IKey.lang("mappet.gui.region.update")).marginTop(12), this.update);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_enter")).background().marginTop(12).marginBottom(5), this.onEnter);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_exit")).background().marginTop(12).marginBottom(5), this.onExit);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_tick")).background().marginTop(12).marginBottom(5), this.onTick);

        this.add(this.writeState.marginTop(12));
        this.add(this.stateOptions);

        GuiLabel shapesLabel = Elements.label(IKey.lang("mappet.gui.region.shapes")).background();
        GuiIconElement addShape = new GuiIconElement(mc, Icons.ADD, this::addShape);

        addShape.flex().relative(shapesLabel).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        shapesLabel.marginTop(12).add(addShape);

        this.add(shapesLabel);
        this.add(this.shapes);

        this.flex().column(5).vertical().stretch();
    }

    private void addShape(GuiIconElement element)
    {
        AbstractShape shape = new BoxShape();
        GuiShapeEditor editor = new GuiShapeEditor(this.mc);

        this.region.shapes.add(shape);
        this.shapes.add(editor.marginTop(12));
        editor.set(this.region, shape);
    }

    private void toggleStates()
    {
        this.region.writeState = this.writeState.isToggled();

        this.stateOptions.removeAll();

        if (this.region.writeState)
        {
            this.stateOptions.add(Elements.label(IKey.lang("mappet.gui.conditions.state.id")).marginTop(6), this.state);
            this.stateOptions.add(this.target, this.additive, this.once);
        }

        this.getParentContainer().resize();
    }

    public void set(Region region)
    {
        this.region = region;

        if (region != null)
        {
            this.passable.toggled(region.passable);
            this.checkEntities.toggled(region.checkEntities);
            this.enabled.set(region.enabled);
            this.delay.setValue(region.delay);
            this.update.setValue(region.update);
            this.onEnter.set(region.onEnter);
            this.onExit.set(region.onExit);
            this.onTick.set(region.onTick);

            this.shapes.removeAll();

            for (AbstractShape shape : region.shapes)
            {
                GuiShapeEditor editor = new GuiShapeEditor(this.mc);

                this.shapes.add(editor.marginTop(12));
                editor.set(region, shape);
            }

            this.writeState.toggled(region.writeState);
            this.state.setText(region.state);
            this.target.setValue(region.target.ordinal());
            this.additive.toggled(region.additive);
            this.once.toggled(region.once);

            this.toggleStates();
        }
    }
}
