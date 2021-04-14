package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.nodes.NodeRelation;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mclib.client.gui.framework.elements.utils.GuiCanvas;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiNodeGraph extends GuiCanvas
{
    public NodeSystem<EventNode> system;

    private List<EventNode> selected = new ArrayList<EventNode>();
    private boolean lastSelected;
    private int lastNodeX;
    private int lastNodeY;

    public GuiNodeGraph(Minecraft mc)
    {
        super(mc);
    }

    public void set(NodeSystem<EventNode> system)
    {
        this.system = system;

        if (system != null)
        {
            int x = system.main == null ? 0 : system.main.x;
            int y = system.main == null ? 0 : system.main.y;

            if (system.main == null)
            {
                for (EventNode node : system.nodes.values())
                {
                    x += node.x;
                    y += node.y;
                }

                x /= system.nodes.size();
                y /= system.nodes.size();
            }

            this.scaleX.setShift(x);
            this.scaleY.setShift(y);
            this.scaleX.setZoom(0.5F);
            this.scaleY.setZoom(0.5F);
        }
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context) && context.mouseButton == 2)
        {
            return true;
        }

        if (this.system == null)
        {
            return false;
        }

        if (context.mouseButton == 0)
        {
            this.lastNodeX = (int) this.fromX(context.mouseX);
            this.lastNodeY = (int) this.fromY(context.mouseY);

            for (EventNode node : this.system.nodes.values())
            {
                int x1 = this.toX(node.x - 60);
                int y1 = this.toY(node.y - 35);
                int x2 = this.toX(node.x + 60);
                int y2 = this.toY(node.y + 35);

                Area.SHARED.setPoints(x1, y1, x2, y2);

                if (Area.SHARED.isInside(context))
                {
                    if (GuiScreen.isCtrlKeyDown())
                    {
                        if (!this.selected.contains(node))
                        {
                            this.selected.add(node);
                        }
                    }
                    else
                    {
                        this.selected.clear();
                        this.selected.add(node);
                    }

                    this.lastSelected = true;

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void mouseReleased(GuiContext context)
    {
        super.mouseReleased(context);

        this.lastSelected = false;
    }

    @Override
    protected void dragging(GuiContext context)
    {
        super.dragging(context);

        if (this.dragging && this.mouse == 0 && this.lastSelected && !this.selected.isEmpty())
        {
            int lastNodeX = (int) this.fromX(context.mouseX);
            int lastNodeY = (int) this.fromY(context.mouseY);

            for (EventNode node : this.selected)
            {
                node.x += lastNodeX - this.lastNodeX;
                node.y += lastNodeY - this.lastNodeY;
            }

            this.lastNodeX = lastNodeX;
            this.lastNodeY = lastNodeY;
        }
    }

    @Override
    protected void drawCanvas(GuiContext context)
    {
        super.drawCanvas(context);

        if (this.system == null)
        {
            return;
        }

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(3);
        GlStateManager.color(1F, 1F, 1F, 1F);

        BufferBuilder builder = Tessellator.getInstance().getBuffer();

        builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        for (NodeRelation<EventNode> relation : this.system.relations)
        {
            int x1 = this.toX(relation.input.x);
            int y1 = this.toY(relation.input.y);
            int x2 = this.toX(relation.output.x);
            int y2 = this.toY(relation.output.y);

            builder.pos(x1, y1, 0).color(0.0F, 0.0F, 0F, 0.75F).endVertex();
            builder.pos(x2, y2, 0).color(0.0F, 0.0F, 0F, 0.75F).endVertex();
        }

        Tessellator.getInstance().draw();

        for (EventNode node : this.system.nodes.values())
        {
            int x1 = this.toX(node.x - 60);
            int y1 = this.toY(node.y - 35);
            int x2 = this.toX(node.x + 60);
            int y2 = this.toY(node.y + 35);

            Area.SHARED.setPoints(x1, y1, x2, y2);

            boolean hover = Area.SHARED.isInside(context);
            int index = this.selected.indexOf(node);

            int colorBg = hover ? 0xff080808 : 0xff000000;
            int colorFg = 0xaa000000 + node.getColor();

            if (index >= 0)
            {
                int colorSh = index == 0 ? 0x0088ff : 0x0022aa;

                GuiDraw.drawDropShadow(x1 + 4, y1 + 4, x2 - 4, y2 - 4, 8, 0xff000000 + colorSh, colorSh);
            }

            Gui.drawRect(x1 + 1, y1, x2 - 1, y2, colorBg);
            Gui.drawRect(x1, y1 + 1, x2, y2 - 1, colorBg);
            GuiDraw.drawOutline(x1 + 3, y1 + 3, x2 - 3, y2 - 3, colorFg);
        }

        GlStateManager.glLineWidth(1);
    }
}