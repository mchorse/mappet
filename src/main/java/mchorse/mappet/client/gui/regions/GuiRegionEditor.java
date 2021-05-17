package mchorse.mappet.client.gui.regions;

import mchorse.mappet.api.regions.Region;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiRegionEditor extends GuiElement
{
    public GuiToggleElement passable;
    public GuiTextElement enabled;
    public GuiTrackpadElement delay;
    public GuiTriggerElement onEnter;
    public GuiTriggerElement onExit;
    public GuiCirculateElement shape;
    public GuiShapeEditor shapeEditor;

    private Region region;

    public GuiRegionEditor(Minecraft mc)
    {
        super(mc);

        this.passable = new GuiToggleElement(mc, IKey.lang("mappet.gui.region.passable"), (b) -> this.region.passable = b.isToggled());
        this.enabled = new GuiTextElement(mc, 1000, (text) -> this.region.enabled = text);
        this.delay = new GuiTrackpadElement(mc, (value) -> this.region.delay = value.intValue());
        this.onEnter = new GuiTriggerElement(mc);
        this.onExit = new GuiTriggerElement(mc);
        this.shape = new GuiCirculateElement(mc, this::changeShape);
        this.shape.flex().w(80);
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.box"));
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.sphere"));
        this.shape.addLabel(IKey.lang("mappet.gui.shapes.cylinder"));
        this.shapeEditor = new GuiShapeEditor(mc);

        this.add(this.passable);
        this.add(Elements.label(IKey.lang("mappet.gui.region.enabled")).marginTop(6), this.enabled);
        this.add(Elements.label(IKey.lang("mappet.gui.region.delay")).marginTop(12), this.delay);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_enter")).background().marginTop(12).marginBottom(5), this.onEnter);
        this.add(Elements.label(IKey.lang("mappet.gui.region.on_exit")).background().marginTop(12).marginBottom(5), this.onExit);
        this.add(Elements.label(IKey.lang("mappet.gui.region.shape")).background().marginTop(12).marginBottom(5), this.shape);
        this.add(this.shapeEditor);

        this.flex().column(5).vertical().stretch();
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
            this.enabled.setText(region.enabled);
            this.delay.setValue(region.delay);
            this.onEnter.set(region.onEnter);
            this.onExit.set(region.onExit);
            this.shape.setValue(region.shape instanceof BoxShape ? 0 : (region.shape instanceof CylinderShape ? 2 : 1));
            this.shapeEditor.set(region.shape);
        }
    }
}
