package mchorse.mappet.client.gui.panels;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.regions.GuiRegionEditor;
import mchorse.mappet.client.renders.tile.TileRegionRenderer;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDrawable;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
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
    public GuiRegionEditor region;

    protected boolean update;
    protected TileRegion tile;

    public GuiRegionPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.sidebar = new GuiElement(mc);
        this.sidebar.flex().relative(this).x(1F).w(200).h(1F).anchorX(1F);

        this.toggleSidebar = new GuiIconElement(mc, Icons.RIGHTLOAD, (element) -> this.toggleSidebar());
        this.toggleSidebar.flex().relative(this.sidebar).x(-20);

        GuiDrawable drawable = new GuiDrawable((context) -> this.font.drawStringWithShadow(I18n.format(this.getTitle()), this.tiles.area.x, this.area.y + 10, 0xffffff));

        this.tiles = new GuiTileRegionListElement(mc, (list) -> this.fill(list.get(0)));
        this.tiles.flex().relative(this.sidebar).xy(10, 25).w(1F, -20).h(1F, -35);
        this.sidebar.add(drawable, this.tiles);

        this.editor = new GuiScrollElement(mc);
        this.editor.markContainer();
        this.editor.flex().relative(this).w(240).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.region = new GuiRegionEditor(mc);

        this.editor.scroll.opposite = true;
        this.editor.add(this.region);

        this.add(this.sidebar, this.editor, this.toggleSidebar);

        this.keys().register(IKey.lang("mappet.gui.panels.keys.toggle_sidebar"), Keyboard.KEY_N, () -> this.toggleSidebar.clickItself(GuiBase.getCurrent())).category(GuiMappetDashboardPanel.KEYS_CATEGORY);

        this.fill(null);
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
        return "mappet.gui.panels.regions";
    }

    /* Data population */

    public void fill(TileRegion tile)
    {
        if (tile != null && tile.isInvalid())
        {
            tile = null;
        }

        this.tile = tile;

        this.editor.setVisible(tile != null);
        this.tiles.setCurrentScroll(tile);

        if (tile != null)
        {
            this.region.set(tile.region);
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
        this.tiles.clear();
        TileRegionRenderer.regions.clear();
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

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.region.info.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
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
            String first = element.region.shapes.isEmpty() ? "" : I18n.format("mappet.gui.shapes."+ element.region.shapes.get(0).getType()) + " ";

            return first + "(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        }
    }
}