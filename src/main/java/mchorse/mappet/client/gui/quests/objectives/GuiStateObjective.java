package mchorse.mappet.client.gui.quests.objectives;

import mchorse.mappet.api.quests.objectives.StateObjective;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiStateObjective extends GuiObjective<StateObjective>
{
    public GuiTextElement expression;

    public GuiStateObjective(Minecraft mc, StateObjective objective)
    {
        super(mc, objective);

        this.expression = new GuiTextElement(mc, (t) -> this.objective.expression = t);
        this.expression.flex().relative(this).y(12).w(1F);
        this.expression.setText(objective.expression);

        this.message.flex().relative(this).y(1F).w(1F).anchorY(1F);

        this.flex().h(69);

        this.add(this.expression, this.message);
    }

    @Override
    public IKey getMessageTooltip()
    {
        return IKey.EMPTY;
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        this.font.drawStringWithShadow(I18n.format("mappet.gui.quests.objective_state.expression"), this.expression.area.x, this.expression.area.y - 12, 0xffffff);
    }
}