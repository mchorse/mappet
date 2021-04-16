package mchorse.mappet.client.renders;

import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.utils.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Matrix3d;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3d;

@SideOnly(Side.CLIENT)
public class TileRegionRenderer extends TileBaseBlockRenderer<TileRegion>
{
    public static final float SEGMENTS = 16;

    private Matrix3d a1 = new Matrix3d();
    private Matrix3d a2 = new Matrix3d();
    private Matrix3d a3 = new Matrix3d();
    private Vector3d vec = new Vector3d();

    public TileRegionRenderer()
    {
        super(new Color(1F, 0.098F, 0.72F, 0.5F));

        this.a1.rotY(Math.PI / 2);
        this.a2.rotY(Math.PI / 4);
        this.a3.rotY(Math.PI / -4);
    }

    private Vector3d rotate(Matrix3d mat, double x, double y, double z)
    {
        this.vec.set(x, y, z);

        mat.transform(this.vec);

        return this.vec;
    }

    @Override
    protected void drawMoreDebug(TileRegion te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.region.shape instanceof AbstractShape)
        {
            AbstractShape shape = (AbstractShape) te.region.shape;
            Vector3d diff = new Vector3d(shape.pos.x + 0.5F, shape.pos.y + 0.5F, shape.pos.z + 0.5F);

            diff.x += x;
            diff.y += y;
            diff.z += z;

            if (shape instanceof BoxShape)
            {
                this.drawBox((BoxShape) shape, diff);
            }
            else if (shape instanceof CylinderShape)
            {
                this.drawCylinder((CylinderShape) shape, diff);
            }
            else if (shape instanceof SphereShape)
            {
                this.drawSphere((SphereShape) shape, diff);
            }
        }
    }

    private void drawBox(BoxShape shape, Vector3d diff)
    {
        RenderGlobal.drawBoundingBox(
            diff.x - shape.size.x, diff.y - shape.size.y, diff.z - shape.size.z,
            diff.x + shape.size.x, diff.y + shape.size.y, diff.z + shape.size.z,
            1F, 1F, 1F, 1F
        );
    }

    private void drawCylinder(CylinderShape shape, Vector3d diff)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        GlStateManager.color(1F, 1F, 1F, 1F);
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        for (int i = 0; i < SEGMENTS; i++)
        {
            double a1 = i / SEGMENTS * Math.PI * 2;
            double a2 = (i + 1) / SEGMENTS * Math.PI * 2;

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y + shape.vertical / 2,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y + shape.vertical / 2,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y + shape.vertical / 2,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y - shape.vertical / 2,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();

            if (i == SEGMENTS - 1)
            {
                buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y + shape.vertical / 2,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
                buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y - shape.vertical / 2,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
            }

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y - shape.vertical / 2,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y - shape.vertical / 2,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
        }

        Tessellator.getInstance().draw();
    }

    private void drawSphere(SphereShape shape, Vector3d diff)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        GlStateManager.color(1F, 1F, 1F, 1F);
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        Matrix3f matA = new Matrix3f();

        matA.rotY((float) (Math.PI / 2));

        for (int i = 0; i < SEGMENTS; i++)
        {
            double a1 = i / SEGMENTS * Math.PI * 2;
            double a2 = (i + 1) / SEGMENTS * Math.PI * 2;

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y + Math.sin(a1) * shape.vertical,  diff.z).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y + Math.sin(a2) * shape.vertical,  diff.z).endVertex();

            /* Rotate 90  */
            Vector3d vector = this.rotate(this.a1,Math.cos(a1) * shape.horizontal, Math.sin(a1) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();

            vector = this.rotate(this.a1,Math.cos(a2) * shape.horizontal, Math.sin(a2) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();

            /* Rotate 45 */
            vector = this.rotate(this.a2,Math.cos(a1) * shape.horizontal, Math.sin(a1) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();

            vector = this.rotate(this.a2,Math.cos(a2) * shape.horizontal, Math.sin(a2) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();

            /* Rotate -45 */
            vector = this.rotate(this.a3,Math.cos(a1) * shape.horizontal, Math.sin(a1) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();

            vector = this.rotate(this.a3,Math.cos(a2) * shape.horizontal, Math.sin(a2) * shape.vertical, 0);
            buffer.pos(diff.x + vector.x, diff.y + vector.y, diff.z + vector.z).endVertex();
        }

        Tessellator.getInstance().draw();
    }
}