package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.huds.HUDMorph;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.api.huds.HUDStage;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.huds.GuiHUDMorphTransformations;
import mchorse.mappet.client.gui.huds.GuiHUDMorphsOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;

public class GuiHUDScenePanel extends GuiMappetDashboardPanel<HUDScene>
{
    public GuiIconElement morphs;
    public GuiElement column;
    public GuiNestedEdit morph;
    public GuiToggleElement ortho;
    public GuiTrackpadElement orthoX;
    public GuiTrackpadElement orthoY;
    public GuiTrackpadElement expire;
    public GuiHUDMorphTransformations transformations;

    public GuiTrackpadElement fov;
    public GuiToggleElement hide;

    private HUDStage stage = new HUDStage(true);
    private HUDMorph current;
    private long lastTick;

    public GuiHUDScenePanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.morphs = new GuiIconElement(mc, Icons.MORE, (b) -> this.openMorphs());
        this.morph = new GuiNestedEdit(mc, this::openMorphMenu);
        this.ortho = new GuiToggleElement(mc, IKey.lang("mappet.gui.huds.ortho"), (b) -> this.current.ortho = b.isToggled());
        this.orthoX = new GuiTrackpadElement(mc, (v) -> this.current.orthoX = v.floatValue());
        this.orthoX.limit(0, 1).metric().strong = 0.25D;
        this.orthoY = new GuiTrackpadElement(mc, (v) -> this.current.orthoY = v.floatValue());
        this.orthoY.limit(0, 1).metric().strong = 0.25D;
        this.expire = new GuiTrackpadElement(mc, (v) -> this.current.expire = v.intValue());
        this.expire.limit(0).integer();
        this.transformations = new GuiHUDMorphTransformations(mc);
        this.column = Elements.column(mc, 5, 10);

        this.fov = new GuiTrackpadElement(mc, (v) -> this.data.fov = v.floatValue());
        this.fov.limit(0, 180);
        this.hide = new GuiToggleElement(mc, IKey.lang("mappet.gui.huds.hide"), (b) -> this.data.hide = b.isToggled());

        this.morphs.flex().relative(this.editor);
        this.column.flex().relative(this.editor).y(1F).w(130).anchorY(1F);
        this.transformations.flex().relative(dashboard.root).x(0.5F).y(1F, -10).wh(190, 70).anchor(0.5F, 1F);

        this.column.add(this.morph, this.ortho, this.orthoX, this.orthoY, Elements.label(IKey.lang("mappet.gui.huds.expire")).marginTop(12), this.expire);
        this.column.add(Elements.label(IKey.lang("mappet.gui.huds.fov")).marginTop(12), this.fov, this.hide);
        this.editor.add(this.column, this.transformations, this.morphs);

        this.fill(null);
    }

    private void openMorphs()
    {
        GuiHUDMorphsOverlayPanel overlay = new GuiHUDMorphsOverlayPanel(this.mc, this.data, this::pickMorph);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay.set(this.current), 0.4F, 0.6F);
    }

    private void openMorphMenu(boolean editing)
    {
        GuiMappetDashboard.get(this.mc).openMorphMenu(this.getParentContainer(), editing, this.current.morph.get(), this::setMorph);
    }

    private void setMorph(AbstractMorph morph)
    {
        this.current.morph.setDirect(morph);
        this.morph.setMorph(morph);
    }

    @Override
    public boolean needsBackground()
    {
        return false;
    }

    @Override
    public ContentType getType()
    {
        return ContentType.HUDS;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.huds";
    }

    @Override
    public void fill(HUDScene data, boolean allowed)
    {
        super.fill(data, allowed);

        this.editor.setVisible(data != null);

        if (data != null)
        {
            this.stage.reset();
            this.stage.scenes.put(data.getId(), data);

            this.fov.setValue(data.fov);
            this.hide.toggled(data.hide);

            this.pickMorph(this.data.morphs.isEmpty() ? null : this.data.morphs.get(0));
        }
    }

    private void pickMorph(HUDMorph current)
    {
        this.current = current;

        this.column.setVisible(current != null);
        this.transformations.setVisible(current != null);

        if (current != null)
        {
            this.morph.setMorph(current.morph.get());
            this.ortho.toggled(current.ortho);
            this.orthoX.setValue(current.orthoX);
            this.orthoY.setValue(current.orthoY);
            this.expire.setValue(current.expire);
            this.transformations.setMorph(current);
        }
    }

    @Override
    public void appear()
    {
        super.appear();

        RenderingHandler.currentStage = this.stage;
    }

    @Override
    public void disappear()
    {
        super.disappear();

        RenderingHandler.currentStage = null;
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.editor.isVisible() && this.current != null && !this.current.morph.isEmpty())
        {
            GuiDraw.drawTextBackground(this.font, this.current.morph.get().getDisplayName(), this.morphs.area.ex() + 3, this.morphs.area.my() - 4, 0xffffff, ColorUtils.HALF_BLACK, 2);
        }

        super.draw(context);
    }
}