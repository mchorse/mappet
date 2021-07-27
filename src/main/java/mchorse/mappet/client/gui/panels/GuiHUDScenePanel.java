package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.huds.HUDMorph;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.api.huds.HUDStage;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.huds.GuiHUDMorphListElement;
import mchorse.mappet.client.gui.huds.GuiHUDMorphTransformations;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mchorse.metamorph.client.gui.creative.GuiNestedEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public class GuiHUDScenePanel extends GuiMappetDashboardPanel<HUDScene>
{
    public GuiHUDMorphListElement morphs;
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

        this.morphs = new GuiHUDMorphListElement(mc, (l) -> this.pickMorph(l.get(0), false));
        this.morphs.background().sorting().context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(mc);

            menu.action(Icons.ADD, IKey.lang("mappet.gui.huds.context.add"), this::addMorph);

            if (!this.morphs.isDeselected())
            {
                menu.action(Icons.COPY, IKey.lang("mappet.gui.huds.context.copy"), this::copyMorph);

                try
                {
                    NBTTagCompound tag = JsonToNBT.getTagFromJson(GuiScreen.getClipboardString());

                    if (tag.hasKey("_HUDMorphCopy"))
                    {
                        HUDMorph morph = new HUDMorph();

                        morph.deserializeNBT(tag);
                        menu.action(Icons.PASTE, IKey.lang("mappet.gui.huds.context.paste"), () -> this.addMorph(morph));
                    }
                }
                catch (Exception e)
                {}

                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.huds.context.remove"), this::removeMorph, Colors.NEGATIVE);
            }

            return menu;
        });
        this.morph = new GuiNestedEdit(mc, this::openMorphMenu);
        this.ortho = new GuiToggleElement(mc, IKey.lang("mappet.gui.huds.ortho"), (b) -> this.current.ortho = b.isToggled());
        this.orthoX = new GuiTrackpadElement(mc, (v) -> this.current.orthoX = v.floatValue());
        this.orthoY = new GuiTrackpadElement(mc, (v) -> this.current.orthoY = v.floatValue());
        this.expire = new GuiTrackpadElement(mc, (v) -> this.current.expire = v.intValue());
        this.expire.limit(0).integer();
        this.transformations = new GuiHUDMorphTransformations(mc);
        this.column = Elements.column(mc, 5, 10);

        this.fov = new GuiTrackpadElement(mc, (v) -> this.data.fov = v.floatValue());
        this.fov.limit(0, 180);
        this.hide = new GuiToggleElement(mc, IKey.lang("mappet.gui.huds.hide"), (b) -> this.data.hide = b.isToggled());

        this.column.flex().relative(this.editor).y(1F).w(130).anchorY(1F);
        this.morphs.flex().relative(this.column).w(1F).h(100).anchorY(1F);
        this.transformations.flex().relative(this.editor).x(0.5F).y(1F, -10).wh(190, 70).anchor(0.5F, 1F);

        this.column.add(this.morph, this.ortho, this.orthoX, this.orthoY, Elements.label(IKey.lang("mappet.gui.huds.expire")).marginTop(12), this.expire);
        this.column.add(Elements.label(IKey.lang("mappet.gui.huds.fov")).marginTop(12), this.fov, this.hide);
        this.editor.add(this.column, this.morphs, this.transformations);

        this.fill(null);
    }

    private void addMorph()
    {
        this.addMorph(new HUDMorph());
    }

    private void addMorph(HUDMorph morph)
    {
        this.data.morphs.add(morph);
        this.morphs.update();

        this.pickMorph(morph, true);
    }

    private void copyMorph()
    {
        NBTTagCompound tag = this.current.serializeNBT();
        tag.setBoolean("_HUDMorphCopy", true);

        GuiScreen.setClipboardString(tag.toString());
    }

    private void removeMorph()
    {
        int index = this.morphs.getIndex();

        this.data.morphs.remove(this.morphs.getIndex());
        this.morphs.update();
        this.morphs.setIndex(index < 1 ? 0 : index - 1);

        this.pickMorph(this.morphs.getCurrentFirst(), true);
    }

    private void openMorphMenu(boolean editing)
    {
        GuiMappetDashboard.get(this.mc).openMorphMenu(this.getParentContainer(), editing, this.current.morph, this::setMorph);
    }

    private void setMorph(AbstractMorph morph)
    {
        this.current.morph = morph;
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

            this.morphs.setList(data.morphs);
            this.morphs.setIndex(0);
            this.fov.setValue(data.fov);
            this.hide.toggled(data.hide);

            this.pickMorph(this.morphs.getCurrentFirst(), true);
        }
    }

    private void pickMorph(HUDMorph current, boolean select)
    {
        this.current = current;

        this.column.setVisible(current != null);
        this.transformations.setVisible(current != null);

        if (current != null)
        {
            this.morph.setMorph(current.morph);
            this.ortho.toggled(current.ortho);
            this.orthoX.setValue(current.orthoX);
            this.orthoY.setValue(current.orthoY);
            this.expire.setValue(current.expire);
            this.transformations.setMorph(current);

            if (select)
            {
                this.morphs.setCurrentScroll(current);
            }
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
        if (this.lastTick < context.tick)
        {
            this.stage.update(false);

            this.lastTick = context.tick;
        }

        super.draw(context);
    }
}