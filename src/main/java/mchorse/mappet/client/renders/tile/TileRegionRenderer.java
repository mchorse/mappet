package mchorse.mappet.client.renders.tile;

import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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

    private static final Color SELECTED = new Color(0, 0.5F, 1F, 0.5F);
    private static final Color NOT_SELECTED = new Color(1F, 1F, 1F, 1F);

    private Matrix3d a1 = new Matrix3d();
    private Matrix3d a2 = new Matrix3d();
    private Matrix3d a3 = new Matrix3d();
    private Vector3d vec = new Vector3d();

    private TileRegion selected;

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
    protected Color getBoxColor(TileRegion te)
    {
        if (te == this.selected)
        {
            return SELECTED;
        }

        return super.getBoxColor(te);
    }

    @Override
    protected boolean canRender(Minecraft mc, TileRegion te)
    {
        GuiScreen screen = mc.currentScreen;

        if (screen instanceof GuiMappetDashboard)
        {
            GuiMappetDashboard dashboard = (GuiMappetDashboard) screen;

            if (dashboard.panels.view.delegate == dashboard.region)
            {
                this.selected = dashboard.region.getTile();

                return true;
            }
        }

        this.selected = null;

        return super.canRender(mc, te);
    }

    @Override
    protected void renderMoreDebug(TileRegion te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Color color = te == this.selected ? SELECTED : NOT_SELECTED;

        for (AbstractShape shape : te.region.shapes)
        {
            this.renderShape(shape, x, y, z, color);
        }
    }

    public void renderShape(AbstractShape shape, double x, double y, double z, Color color)
    {
        Vector3d diff = new Vector3d(shape.pos.x + 0.5F, shape.pos.y + 0.5F, shape.pos.z + 0.5F);

        diff.x += x;
        diff.y += y;
        diff.z += z;

        if (shape instanceof BoxShape)
        {
            this.renderBoxShape((BoxShape) shape, diff, color);
        }
        else if (shape instanceof CylinderShape)
        {
            this.renderCylinderShape((CylinderShape) shape, diff, color);
        }
        else if (shape instanceof SphereShape)
        {
            this.renderSphereShape((SphereShape) shape, diff, color);
        }
    }

    private void renderBoxShape(BoxShape shape, Vector3d diff, Color color)
    {
        RenderGlobal.drawBoundingBox(
            diff.x - shape.size.x, diff.y - shape.size.y, diff.z - shape.size.z,
            diff.x + shape.size.x, diff.y + shape.size.y, diff.z + shape.size.z,
            color.r, color.g, color.b, 1
        );
    }

    private void renderCylinderShape(CylinderShape shape, Vector3d diff, Color color)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        GlStateManager.color(color.r, color.g, color.b, 1);
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        for (int i = 0; i < SEGMENTS; i++)
        {
            double a1 = i / SEGMENTS * Math.PI * 2;
            double a2 = (i + 1) / SEGMENTS * Math.PI * 2;

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y + shape.vertical,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y + shape.vertical,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y + shape.vertical,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y - shape.vertical,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();

            if (i == SEGMENTS - 1)
            {
                buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y + shape.vertical,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
                buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y - shape.vertical,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
            }

            buffer.pos(diff.x + Math.cos(a1) * shape.horizontal, diff.y - shape.vertical,  diff.z + Math.sin(a1) * shape.horizontal).endVertex();
            buffer.pos(diff.x + Math.cos(a2) * shape.horizontal, diff.y - shape.vertical,  diff.z + Math.sin(a2) * shape.horizontal).endVertex();
        }

        Tessellator.getInstance().draw();
    }

    private void renderSphereShape(SphereShape shape, Vector3d diff, Color color)
    {
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();

        GlStateManager.color(color.r, color.g, color.b, 1);
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

    @Override
    public boolean isGlobalRenderer(TileRegion te)
    {
        return true;
    }
}