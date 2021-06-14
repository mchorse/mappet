package mchorse.mappet.client.gui.quests.objectives;

import mchorse.mappet.api.quests.objectives.CollectObjective;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCollectObjective extends GuiObjective<CollectObjective>
{
    public GuiSlotElement stack;

    public GuiCollectObjective(Minecraft mc, CollectObjective objective)
    {
        super(mc, objective);

        this.stack = new GuiSlotElement(mc, 0, (stack) -> this.objective.stack = stack.copy());

        this.stack.setStack(objective.stack);
        this.stack.flex().relative(this).x(1F, -24).y(1F).anchorY(1F);
        this.stack.tooltip(IKey.lang("mappet.gui.quests.objective_collect.title"));

        this.message.flex().relative(this).y(1F).w(1F, -29).h(24).anchorY(1F);

        this.flex().h(36);

        this.add(this.stack, this.message);
    }

    @Override
    public IKey getMessageTooltip()
    {
        return IKey.lang("mappet.gui.quests.objective_collect.message_tooltip");
    }
}