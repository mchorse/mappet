package mchorse.mappet.client.gui.regions;

import mchorse.mappet.api.regions.Region;
import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import javax.vecmath.Vector3d;

public class GuiShapeEditor extends GuiElement
{
    public GuiCirculateElement shapeSwitch;
    public GuiTrackpadElement x;
    public GuiTrackpadElement y;
    public GuiTrackpadElement z;
    public GuiTrackpadElement sizeX;
    public GuiTrackpadElement sizeY;
    public GuiTrackpadElement sizeZ;

    public GuiLabel bottomLabel;
    public GuiElement bottomRow;

    private Region region;
    private AbstractShape shape;

    public GuiShapeEditor(Minecraft mc)
    {
        super(mc);

        this.context(() -> new GuiSimpleContextMenu(this.mc)
                .action(Icons.REMOVE, IKey.lang("mappet.gui.region.context.remove"), this::removeShape, Colors.NEGATIVE));

        this.shapeSwitch = new GuiCirculateElement(mc, this::changeShape);
        this.shapeSwitch.addLabel(IKey.lang("mappet.gui.shapes.box"));
        this.shapeSwitch.addLabel(IKey.lang("mappet.gui.shapes.sphere"));
        this.shapeSwitch.addLabel(IKey.lang("mappet.gui.shapes.cylinder"));

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

        this.bottomLabel = Elements.label(IKey.lang(""));
        this.bottomRow = Elements.row(mc, 5, 0, this.sizeX, this.sizeY, this.sizeZ);

        this.add(this.shapeSwitch);
        this.add(Elements.label(IKey.lang("mappet.gui.region.offset")));
        this.add(Elements.row(mc, 5, 0, this.x, this.y, this.z));
        this.add(this.bottomLabel, this.bottomRow);
    }

    private void removeShape()
    {
        int index = this.parent.getChildren().indexOf(this);

        if (index >= 0)
        {
            GuiElement parent = this.getParentContainer();

            this.removeFromParent();
            this.region.shapes.remove(index);

            parent.resize();
        }
    }

    private void changeShape(GuiCirculateElement element)
    {
        int value = this.shapeSwitch.getValue();
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
            int index = this.parent.getChildren().indexOf(this);

            if (index >= 0)
            {
                shape.copyFrom(this.shape);

                this.region.shapes.set(index, shape);
                this.set(this.region, shape);
            }
        }
    }

    public void set(Region region, AbstractShape shape)
    {
        this.region = region;
        this.shape = shape;

        this.shapeSwitch.setValue(this.shape instanceof BoxShape ? 0 : (this.shape instanceof CylinderShape ? 2 : 1));
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

            this.bottomLabel.label.set("mappet.gui.region.box_size");
            this.bottomRow.add(this.sizeZ);
        }
        else if (shape instanceof SphereShape)
        {
            this.sizeX.setValue(((SphereShape) shape).horizontal);
            this.sizeY.setValue(((SphereShape) shape).vertical);

            this.bottomLabel.label.set(shape instanceof CylinderShape ? "mappet.gui.region.sphere_size" : "mappet.gui.region.ellipse_size");
        }

        if (this.hasParent())
        {
            this.getParentContainer().resize();
        }
    }
}
