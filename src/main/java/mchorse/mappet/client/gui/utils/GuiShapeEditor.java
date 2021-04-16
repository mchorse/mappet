package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector3d;

public class GuiShapeEditor extends GuiElement
{
    public GuiTrackpadElement x;
    public GuiTrackpadElement y;
    public GuiTrackpadElement z;
    public GuiTrackpadElement sizeX;
    public GuiTrackpadElement sizeY;
    public GuiTrackpadElement sizeZ;

    public GuiLabel bottomLabel;
    public GuiElement bottomRow;

    private AbstractShape shape;

    public GuiShapeEditor(Minecraft mc)
    {
        super(mc);

        this.x = new GuiTrackpadElement(mc, (v) -> this.shape.pos.x = v);
        this.y = new GuiTrackpadElement(mc, (v) -> this.shape.pos.y = v);
        this.z = new GuiTrackpadElement(mc, (v) -> this.shape.pos.z = v);

        this.sizeX = new GuiTrackpadElement(mc, (v) ->
        {
            if (this.shape instanceof BoxShape)
            {
                ((BoxShape) this.shape).size.x = v;
            }
            else if (this.shape instanceof SphereShape)
            {
                ((SphereShape) this.shape).horizontal = v;
            }
        });
        this.sizeY = new GuiTrackpadElement(mc, (v) ->
        {
            if (this.shape instanceof BoxShape)
            {
                ((BoxShape) this.shape).size.y = v;
            }
            else if (this.shape instanceof SphereShape)
            {
                ((SphereShape) this.shape).vertical = v;
            }
        });
        this.sizeZ = new GuiTrackpadElement(mc, (v) ->
        {
            if (this.shape instanceof BoxShape)
            {
                ((BoxShape) this.shape).size.z = v;
            }
        });

        this.flex().column(5).vertical().stretch();

        this.bottomLabel = Elements.label(IKey.str("Offset"));
        this.bottomRow = Elements.row(mc, 5, 0, this.sizeX, this.sizeY, this.sizeZ);

        this.add(Elements.label(IKey.str("Offset")));
        this.add(Elements.row(mc, 5, 0, this.x, this.y, this.z));
        this.add(this.bottomLabel, this.bottomRow);
    }

    public void set(AbstractShape shape)
    {
        this.shape = shape;

        this.x.setValue(shape.pos.x);
        this.y.setValue(shape.pos.y);
        this.z.setValue(shape.pos.z);

        this.sizeZ.removeFromParent();

        if (shape instanceof BoxShape)
        {
            Vector3d size = ((BoxShape) shape).size;

            this.sizeX.setValue(size.x);
            this.sizeY.setValue(size.y);
            this.sizeZ.setValue(size.z);

            this.bottomLabel.label.set("Half size");
            this.bottomRow.add(this.sizeZ);
        }
        else if (shape instanceof SphereShape)
        {
            this.sizeX.setValue(((SphereShape) shape).horizontal);
            this.sizeY.setValue(((SphereShape) shape).vertical);

            this.bottomLabel.label.set(shape instanceof CylinderShape ? "Ellipsoid radius" : "Radius and height");
        }

        this.getParentContainer().resize();
    }
}
