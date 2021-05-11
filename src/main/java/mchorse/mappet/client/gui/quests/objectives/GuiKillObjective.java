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
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class GuiKillObjective extends GuiObjective<KillObjective>
{
    public GuiButtonElement entity;
    public GuiTrackpadElement count;
    public GuiTextElement tag;

    public GuiKillObjective(Minecraft mc, KillObjective objective)
    {
        super(mc, objective);

        this.entity = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.entities.main"), (b) -> this.openPickEntityOverlay());
        this.entity.flex().relative(this).y(12).w(0.5F, -3);

        this.count = new GuiTrackpadElement(mc, (value) -> this.objective.count = value.intValue());
        this.count.integer().limit(0).setValue(objective.count);
        this.count.flex().relative(this).x(1F).y(12).w(0.5F, -2).anchorX(1F);

        this.tag = new GuiTextElement(mc, 10000, this::parseTag);
        this.tag.flex().relative(this).y(49).w(1F);
        this.tag.setText(objective.tag == null ? "" : this.tag.toString());

        this.flex().h(69);

        this.add(this.entity, this.count, this.tag);
    }

    private void parseTag(String tag)
    {
        NBTTagCompound nbt = null;

        try
        {
            nbt = JsonToNBT.getTagFromJson(tag);
        }
        catch (Exception e)
        {}

        this.objective.tag = nbt;
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

        this.font.drawStringWithShadow(I18n.format("mappet.gui.quests.objective_kill.entity"), this.entity.area.x, this.entity.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow(I18n.format("mappet.gui.quests.objective_kill.count"), this.count.area.x, this.count.area.y - 12, 0xffffff);
        this.font.drawStringWithShadow(I18n.format("mappet.gui.quests.objective_kill.nbt"), this.tag.area.x, this.tag.area.y - 12, 0xffffff);
    }
}