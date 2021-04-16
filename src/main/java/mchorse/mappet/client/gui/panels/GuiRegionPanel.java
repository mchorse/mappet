package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.regions.shapes.AbstractShape;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiShapeEditor;
import mchorse.mappet.client.gui.utils.GuiTriggerElement;
import mchorse.mappet.client.renders.TileRegionRenderer;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.function.Consumer;

public class GuiRegionPanel extends GuiDashboardPanel<GuiMappetDashboard>
{
    public GuiIconElement toggleSidebar;
    public GuiElement sidebar;
    public GuiTileRegionListElement tiles;

    public GuiScrollElement editor;
    public GuiTextElement enabled;
    public GuiTrackpadElement delay;
    public GuiTriggerElement onEnter;
    public GuiTriggerElement onExit;
    public GuiCirculateElement shape;
    public GuiShapeEditor shapeEditor;

    protected boolean update;
    protected TileRegion tile;

    public GuiRegionPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.sidebar = new GuiElement(mc);
        this.sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);

        this.toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, (element) -> this.toggleSidebar());
        this.toggleSidebar.flex().relative(this.sidebar).x(-20);

        GuiDrawable drawable = new GuiDrawable((context) -> this.font.drawStringWithShadow(this.getTitle(), this.tiles.area.x, this.area.y + 10, 0xffffff));

        this.tiles = new GuiTileRegionListElement(mc, (list) -> this.fill(list.get(0)));
        this.tiles.flex().relative(this.sidebar).xy(10, 25).w(1F, -20).h(1F, -35);
        this.sidebar.add(drawable, this.tiles);

        this.editor = new GuiScrollElement(mc);
        this.editor.markContainer();
        this.editor.flex().relative(this).w(240).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.enabled = new GuiTextElement(mc, 1000, (text) -> this.tile.region.enabled = text);
        this.delay = new GuiTrackpadElement(mc, (value) -> this.tile.region.delay = value.intValue());
        this.onEnter = new GuiTriggerElement(mc);
        this.onExit = new GuiTriggerElement(mc);
        this.shape = new GuiCirculateElement(mc, this::changeShape);
        this.shape.flex().w(80);
        this.shape.addLabel(IKey.str("box"));
        this.shape.addLabel(IKey.str("sphere"));
        this.shape.addLabel(IKey.str("cylinder"));
        this.shapeEditor = new GuiShapeEditor(mc);

        this.editor.scroll.opposite = true;
        this.editor.add(Elements.label(IKey.str("Enabled expression")).background(0x88000000), this.enabled);
        this.editor.add(Elements.label(IKey.str("Trigger delay"), 20).anchor(0, 1F).background(0x88000000), this.delay);
        this.editor.add(Elements.label(IKey.str("On player enter trigger"), 26).anchor(0, 0.75F).background(0x88000000), this.onEnter);
        this.editor.add(Elements.label(IKey.str("On player exit trigger"), 26).anchor(0, 0.75F).background(0x88000000), this.onExit);
        this.editor.add(Elements.label(IKey.str("Shape"), 26).anchor(0, 0.75F).background(0x88000000), this.shape);
        this.editor.add(this.shapeEditor);

        this.add(this.sidebar, this.editor, this.toggleSidebar);

        this.keys().register(IKey.str("Toggle sidebar"), Keyboard.KEY_N, () -> this.toggleSidebar.clickItself(GuiBase.getCurrent())).category(GuiMappetDashboardPanel.KEYS_CATEGORY);

        this.fill(null);
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
            shape.copyFrom(this.tile.region.shape);
            this.shapeEditor.set(shape);

            this.tile.region.shape = shape;
        }
    }

    private void toggleSidebar()
    {
        this.sidebar.toggleVisible();
        this.toggleSidebar.both(this.sidebar.isVisible() ? Icons.RIGHTLOAD : Icons.LEFTLOAD);

        if (this.sidebar.isVisible())
        {
            this.toggleSidebar.flex().relative(this.sidebar).x(-20);
        }
        else
        {
            this.toggleSidebar.flex().relative(this).x(1F, -20);
        }

        this.resize();
    }

    public TileRegion getTile()
    {
        return this.tile;
    }

    public String getTitle()
    {
        return "Regions";
    }

    /* Data population */

    public void fill(TileRegion tile)
    {
        this.tile = tile;

        this.editor.setVisible(tile != null);
        this.tiles.setCurrentScroll(tile);

        if (tile != null)
        {
            this.enabled.setText(tile.region.enabled);
            this.delay.setValue(tile.region.delay);
            this.onEnter.set(tile.region.onEnter);
            this.onExit.set(tile.region.onExit);
            this.shape.setValue(tile.region.shape instanceof BoxShape ? 0 : (tile.region.shape instanceof CylinderShape ? 2 : 1));
            this.shapeEditor.set(tile.region.shape);
        }
    }

    public void fillTiles(List<TileRegion> regions)
    {
        this.tiles.clear();
        this.tiles.add(regions);
        this.tiles.setCurrentScroll(this.tile);
    }

    @Override
    public boolean needsBackground()
    {
        return false;
    }

    @Override
    public void open()
    {
        super.open();

        this.update = true;
    }

    @Override
    public void appear()
    {
        super.appear();

        if (this.tile != null && this.tile.isInvalid())
        {
            this.fill(null);
        }

        if (this.update)
        {
            TileRegionRenderer.cache = true;
        }
    }

    @Override
    public void close()
    {
        super.close();

        if (!this.update && this.tile != null)
        {
            Dispatcher.sendToServer(new PacketEditRegion(this.tile.getPos(), this.tile.region.serializeNBT()));
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.update && !TileRegionRenderer.regions.isEmpty())
        {
            this.fillTiles(TileRegionRenderer.regions);
            this.update = false;
        }

        if (this.editor.isVisible())
        {
            Gui.drawRect(this.editor.area.x, this.editor.area.y, this.editor.area.mx(), this.editor.area.ey(), 0xbb000000);
            GuiDraw.drawHorizontalGradientRect(this.editor.area.mx(), this.editor.area.y, this.editor.area.x(1.25F), this.editor.area.ey(), 0xbb000000, 0);
        }

        if (this.sidebar.isVisible())
        {
            this.sidebar.area.draw(0xdd000000);
        }

        super.draw(context);

        if (!this.editor.isVisible())
        {
            int w = (this.sidebar.isVisible() ? this.sidebar.area.x - this.area.x : this.area.w) / 2;
            int x = this.area.x + w / 2;

            GuiDraw.drawMultiText(this.font, "Select a region block in the list on the right, or interact with any region blocks in the world, to start editing...", x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }

    public static class GuiTileRegionListElement extends GuiListElement<TileRegion>
    {
        public GuiTileRegionListElement(Minecraft mc, Consumer<List<TileRegion>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected String elementToString(TileRegion element)
        {
            BlockPos pos = element.getPos();

            /* TODO: extract */
            return element.region.shape.getType() + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        }
    }
}