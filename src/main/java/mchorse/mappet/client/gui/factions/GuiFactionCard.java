package mchorse.mappet.client.gui.factions;

import mchorse.mappet.api.factions.Faction;
import mchorse.mappet.api.factions.FactionRelation;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.Interpolations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiFactionCard extends GuiElement
{
    private Faction faction;
    private double score;

    public GuiFactionCard(Minecraft mc, Faction faction, double score)
    {
        super(mc);

        this.faction = faction;
        this.score = score;

        this.flex().h(24);
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        GuiDraw.drawTextBackground(this.font, this.faction.title, this.area.x, this.area.y, this.faction.color, 0);
        FactionRelation.Threshold current = this.faction.ownRelation.get((int) this.score);
        FactionRelation.Threshold previous = null;

        int w = this.font.getStringWidth(current.title);
        this.font.drawStringWithShadow(current.title, this.area.ex() - w, this.area.y, 0xffffff);

        for (int i = 0, c = this.faction.ownRelation.thresholds.size(); i < c; i++)
        {
            FactionRelation.Threshold threshold = this.faction.ownRelation.thresholds.get(i);
            float a = i / (float) c;
            float b = (i + 1) / (float) c;

            if (threshold == current)
            {
                double anchor = i != 0 && i < c - 1 ? (this.score - previous.score) / (float) (threshold.score - previous.score) : 1;

                int x = this.area.x(b);

                if (anchor < 1)
                {
                    x = this.area.x((float) Interpolations.lerp(a, b, anchor));

                    GuiDraw.drawVerticalGradientRect(this.area.x(a), this.area.y + 12, x, this.area.y + 24, 0xff000000 + threshold.color, 0xff000000 + ColorUtils.multiplyColor(threshold.color, 0.7F));
                    GuiDraw.drawVerticalGradientRect(x, this.area.y + 14, this.area.x(b), this.area.y + 24, threshold.color, ColorUtils.HALF_BLACK + threshold.color);
                }
                else
                {
                    GuiDraw.drawVerticalGradientRect(this.area.x(a), this.area.y + 12, x, this.area.y + 24, 0xff000000 + threshold.color, 0xff000000 + ColorUtils.multiplyColor(threshold.color, 0.7F));
                }

                Gui.drawRect(this.area.x(a) - 1, this.area.y + 24, this.area.x(b) + 1, this.area.y + 25, 0xffffffff);
            }
            else
            {
                GuiDraw.drawVerticalGradientRect(this.area.x(a), this.area.y + 14, this.area.x(b), this.area.y + 24, threshold.color, ColorUtils.HALF_BLACK + threshold.color);
            }

            previous = threshold;
        }

        String label = String.valueOf((int) this.score);
        w = this.font.getStringWidth(label);

        GuiDraw.drawTextBackground(this.font, label, this.area.mx() - w / 2, this.area.y, 0xffffff, ColorUtils.HALF_BLACK, 2);
    }
}