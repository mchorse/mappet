package mchorse.mappet.client.gui.quests.objectives;

import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mappet.client.gui.utils.overlays.GuiEntityOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.client.gui.utils.overlays.GuiResourceLocationOverlayPanel;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiKillObjective extends GuiObjective<KillObjective>
{
    public GuiButtonElement entity;
    public GuiTrackpadElement count;

    public GuiKillObjective(Minecraft mc, KillObjective objective)
    {
        super(mc, objective);

        this.entity = new GuiButtonElement(mc, IKey.str("Pick entity..."), (b) -> this.openPickEntityOverlay());
        this.entity.flex().relative(this).y(1F, -20).w(0.5F, -3);

        this.count = new GuiTrackpadElement(mc, (value) -> this.objective.count = value.intValue());
        this.count.integer().limit(0).setValue(objective.count);
        this.count.flex().relative(this).x(1F).y(1F, -20).w(0.5F, -2).anchorX(1F);

        this.flex().h(32);

        this.add(this.entity, this.count);
    }

    private void openPickEntityOverlay()
    {
        GuiResourceLocationOverlayPanel overlay = new GuiEntityOverlayPanel(this.mc, (rl) -> this.objective.entity = rl).set(this.objective.entity);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.6F);
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        this.font.drawStringWithShadow("Entity", this.entity.area.x, this.entity.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow("Count", this.count.area.x, this.count.area.y - 12, 0xffffff);
    }
}