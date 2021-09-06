package mchorse.mappet.client.gui.quests.objectives;

import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public abstract class GuiObjective <T extends AbstractObjective> extends GuiElement
{
    public GuiTextElement message;

    public T objective;

    public GuiObjective(Minecraft mc, T objective)
    {
        super(mc);

        this.objective = objective;

        this.message = new GuiTextElement(mc, 1000, (t) -> this.objective.message = t);
        this.message.tooltip(IKey.comp(IKey.lang("mappet.gui.quests.objectives.message_tooltip"), this.getMessageTooltip()));
        this.message.setText(objective.message);
    }

    public abstract IKey getMessageTooltip();

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.message.hasParent())
        {
            this.font.drawStringWithShadow(I18n.format("mappet.gui.quests.objectives.message"), this.message.area.x, this.message.area.y - 12, 0xffffff);
        }
    }
}
