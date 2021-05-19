package mchorse.mappet.client.gui.factions;

import mchorse.mappet.api.factions.FactionRelation;
import mchorse.mappet.client.gui.panels.GuiFactionPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.framework.elements.input.GuiColorElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.List;
import java.util.function.Consumer;

public class GuiFactionRelationOverlayPanel extends GuiOverlayPanel
{
    public GuiThresholdListElement list;

    public GuiScrollElement editor;
    public GuiTextElement title;
    public GuiColorElement color;
    public GuiTrackpadElement score;
    public GuiCirculateElement attitude;

    private FactionRelation relation;
    private FactionRelation.Threshold threshold;

    public GuiFactionRelationOverlayPanel(Minecraft mc, FactionRelation relation)
    {
        super(mc, IKey.lang("mappet.gui.factions.overlay.main"));

        this.relation = relation;

        this.list = new GuiThresholdListElement(mc, (l) -> this.pickThreshold(l.get(0), false));
        this.list.sorting().setList(relation.thresholds);
        this.list.context(() ->
        {
            GuiSimpleContextMenu menu = new GuiSimpleContextMenu(this.mc).action(Icons.ADD, IKey.lang("mappet.gui.factions.overlay.context.add"), this::addRelation);

            if (this.relation.thresholds.size() > 1)
            {
                menu.action(Icons.REMOVE, IKey.lang("mappet.gui.factions.overlay.context.remove"), this::removeThreshold);
            }

            return menu;
        });

        this.editor = new GuiScrollElement(mc);
        this.title = new GuiTextElement(mc, 1000, (t) -> this.threshold.title = t);
        this.color = new GuiColorElement(mc, (c) -> this.threshold.color = c);
        this.score = new GuiTrackpadElement(mc, (v) -> this.threshold.score = v.intValue());
        this.score.integer();
        this.attitude = GuiFactionPanel.createButton(mc, (a) -> this.threshold.attitude = a);

        this.list.flex().relative(this.content).w(120).h(1F);
        this.editor.flex().relative(this.content).x(120).w(1F, -120).h(1F).column(5).vertical().stretch().scroll().padding(10);

        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.overlay.title")), this.title);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.overlay.color")).marginTop(12), this.color);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.overlay.score")).marginTop(12), this.score);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.overlay.attitude")).marginTop(12), this.attitude);

        this.content.add(this.editor, this.list);

        this.pickThreshold(relation.thresholds.isEmpty() ? null : relation.thresholds.get(0), true);
    }

    private void addRelation()
    {
        FactionRelation.Threshold threshold = new FactionRelation.Threshold();

        this.relation.thresholds.add(threshold);
        this.pickThreshold(threshold, true);
        this.list.update();
    }

    private void removeThreshold()
    {
        int index = this.list.getIndex();

        this.relation.thresholds.remove(index);
        this.pickThreshold(this.relation.thresholds.get(Math.max(index - 1, 0)), true);
        this.list.update();
    }

    private void pickThreshold(FactionRelation.Threshold threshold, boolean select)
    {
        this.threshold = threshold;

        this.editor.setVisible(threshold != null);

        if (threshold != null)
        {
            this.title.setText(threshold.title);
            this.color.picker.setColor(threshold.color);
            this.score.setValue(threshold.score);
            GuiFactionPanel.setValue(this.attitude, threshold.attitude);

            if (select)
            {
                this.list.setCurrentScroll(threshold);
            }
        }
    }

    public static class GuiThresholdListElement extends GuiListElement<FactionRelation.Threshold>
    {
        public GuiThresholdListElement(Minecraft mc, Consumer<List<FactionRelation.Threshold>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected void drawElementPart(FactionRelation.Threshold element, int i, int x, int y, boolean hover, boolean selected)
        {
            Gui.drawRect(x, y, x + 4, y + this.scroll.scrollItemSize, 0xff000000 + element.color);
            GuiDraw.drawHorizontalGradientRect(x + 4, y, x + 24, y + this.scroll.scrollItemSize, 0x44000000 + element.color, element.color);

            super.drawElementPart(element, i, x + 4, y, hover, selected);
        }

        @Override
        protected String elementToString(FactionRelation.Threshold element)
        {
            return element.score + " " + element.title;
        }
    }
}