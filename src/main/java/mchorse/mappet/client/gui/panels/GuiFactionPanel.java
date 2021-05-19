package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.factions.FactionAttitude;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.factions.GuiFactionRelationOverlayPanel;
import mchorse.mappet.client.gui.factions.GuiFactions;
import mchorse.mappet.client.gui.factions.GuiFactionsOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiColorElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GuiFactionPanel extends GuiMappetDashboardPanel<Faction>
{
    public GuiTextElement title;
    public GuiTextElement visible;
    public GuiColorElement color;
    public GuiTrackpadElement score;

    public GuiCirculateElement playerAttitude;
    public GuiCirculateElement othersAttitude;

    public GuiButtonElement openOwnRelation;
    public GuiFactions relations;

    public static GuiCirculateElement createButton(Minecraft mc, Consumer<FactionAttitude> callback)
    {
        GuiCirculateElement element = new GuiCirculateElement(mc, (b) ->
        {
            callback.accept(FactionAttitude.values()[((GuiCirculateElement) b).getValue()]);
        });

        for (FactionAttitude attitude : FactionAttitude.values())
        {
            element.addLabel(IKey.lang("mappet.gui.faction_attitudes." + attitude.name().toLowerCase()));
        }

        return element;
    }

    public static void setValue(GuiCirculateElement element, FactionAttitude attitude)
    {
        element.setValue(attitude.ordinal());
    }

    public GuiFactionPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);

        this.title = new GuiTextElement(mc, 1000, (t) -> this.data.title = t);
        this.visible = new GuiTextElement(mc, 1000, (t) -> this.data.visible = t);
        this.color = new GuiColorElement(mc, (c) -> this.data.color = c);
        this.score = new GuiTrackpadElement(mc, (v) -> this.data.score = v.intValue());
        this.score.limit(0).integer();

        this.playerAttitude = createButton(mc, (a) -> this.data.playerAttitude = a);
        this.othersAttitude = createButton(mc, (a) -> this.data.othersAttitude = a);
        this.openOwnRelation = new GuiButtonElement(mc, IKey.lang("mappet.gui.factions.relations.open"), (b) -> this.openRelation());
        this.relations = new GuiFactions(mc);

        GuiElement a = new GuiElement(mc);
        a.flex().column(4).vertical().stretch();
        a.add(Elements.label(IKey.lang("mappet.gui.factions.title")), this.title);

        GuiElement b = new GuiElement(mc);
        b.flex().w(140).column(4).vertical().stretch();
        b.add(Elements.label(IKey.lang("mappet.gui.factions.color")), this.color);

        GuiElement c = new GuiElement(mc);
        c.flex().column(4).vertical().stretch();
        c.add(Elements.label(IKey.lang("mappet.gui.factions.others_attitude")), this.othersAttitude);

        GuiElement d = new GuiElement(mc);
        d.flex().column(4).vertical().stretch();
        d.add(Elements.label(IKey.lang("mappet.gui.factions.player_attitude")), this.playerAttitude);

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.factions.relations.label")).background();
        GuiIconElement add = new GuiIconElement(mc, Icons.ADD, (button) ->
        {
            List<String> keys = new ArrayList<String>(this.names.list.getList());

            keys.removeIf((key) -> this.data.relations.containsKey(key));
            keys.remove(this.id);

            if (!keys.isEmpty())
            {
                GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiFactionsOverlayPanel(this.mc, keys, this::addRelation), 200, 140);
            }
        });

        add.flex().relative(label).xy(1F, 0.5F).w(10).anchor(1F, 0.5F);
        label.add(add);

        this.editor.flex().column(0).padding(10);
        this.editor.add(Elements.row(mc, 5, a, b));
        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.visible")).marginTop(12), this.visible);
        this.editor.add(Elements.label(IKey.lang("mappet.gui.factions.score")).marginTop(12), this.score);
        this.editor.add(Elements.row(mc, 5, c, d).marginTop(12));
        this.editor.add(label.marginTop(12), this.relations);
        this.editor.add(this.openOwnRelation);
        this.editor.scroll.opposite = true;

        this.fill("", null);
    }

    private void addRelation(String string)
    {
        this.relations.addRelation(string, FactionAttitude.PASSIVE, true);
    }

    private void openRelation()
    {
        GuiFactionRelationOverlayPanel overlay = new GuiFactionRelationOverlayPanel(this.mc, this.data.ownRelation);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
    }

    @Override
    public ContentType getType()
    {
        return ContentType.FACTION;
    }

    @Override
    public String getTitle()
    {
        return "mappet.gui.panels.factions";
    }

    @Override
    public void fill(String id, Faction data)
    {
        super.fill(id, data);

        this.editor.setVisible(data != null);

        if (data != null)
        {
            this.title.setText(data.title);
            this.visible.setText(data.visible);
            this.color.picker.setColor(data.color);
            this.score.setValue(data.score);

            setValue(this.playerAttitude, data.playerAttitude);
            setValue(this.othersAttitude, data.othersAttitude);

            this.relations.set(data.relations);
        }

        this.resize();
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (!this.editor.isVisible())
        {
            int w = this.editor.area.w / 2;
            int x = this.editor.area.mx() - w / 2;

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.factions.info.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}