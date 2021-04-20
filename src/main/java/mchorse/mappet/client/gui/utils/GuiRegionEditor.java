package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.regions.Region;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiRegionEditor extends GuiElement
{
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

        this.add(Elements.label(IKey.str("Enabled expression")).background(0x88000000), this.enabled);
        this.add(Elements.label(IKey.str("Trigger delay"), 20).anchor(0, 1F).background(0x88000000), this.delay);
        this.add(Elements.label(IKey.str("On player enter trigger"), 26).anchor(0, 0.75F).background(0x88000000), this.onEnter);
        this.add(Elements.label(IKey.str("On player exit trigger"), 26).anchor(0, 0.75F).background(0x88000000), this.onExit);
        this.add(Elements.label(IKey.str("Shape"), 26).anchor(0, 0.75F).background(0x88000000), this.shape);
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
            this.enabled.setText(region.enabled);
            this.delay.setValue(region.delay);
            this.onEnter.set(region.onEnter);
            this.onExit.set(region.onExit);
            this.shape.setValue(region.shape instanceof BoxShape ? 0 : (region.shape instanceof CylinderShape ? 2 : 1));
            this.shapeEditor.set(region.shape);
        }
    }
}
