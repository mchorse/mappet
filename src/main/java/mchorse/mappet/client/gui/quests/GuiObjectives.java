package mchorse.mappet.client.gui.quests;

import mchorse.mappet.api.conditions.blocks.DialogueConditionBlock;
import mchorse.mappet.api.quests.objectives.AbstractObjective;
import mchorse.mappet.api.quests.objectives.CollectObjective;
import mchorse.mappet.api.quests.objectives.KillObjective;
import mchorse.mappet.api.quests.objectives.StateObjective;
import mchorse.mappet.client.gui.quests.objectives.GuiCollectObjective;
import mchorse.mappet.client.gui.quests.objectives.GuiKillObjective;
import mchorse.mappet.client.gui.quests.objectives.GuiObjective;
import mchorse.mappet.client.gui.quests.objectives.GuiStateObjective;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.context.GuiSimpleContextMenu;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.List;

public class GuiObjectives extends GuiElement
{
    public List<AbstractObjective> objectives;

    public GuiObjectives(Minecraft mc)
    {
        super(mc);

        this.flex().column(20).vertical().stretch();
    }

    public GuiSimpleContextMenu getAdds()
    {
        return new GuiSimpleContextMenu(Minecraft.getMinecraft())
            .action(Icons.ADD, IKey.lang("mappet.gui.quests.objectives.context.add_kill"), () -> this.addObjective(new KillObjective(), true))
            .action(Icons.ADD, IKey.lang("mappet.gui.quests.objectives.context.add_collect"), () -> this.addObjective(new CollectObjective(), true))
            .action(Icons.ADD, IKey.lang("mappet.gui.quests.objectives.context.add_state"), () -> this.addObjective(new StateObjective(), true))
            .action(Icons.ADD, IKey.lang("mappet.gui.quests.objectives.context.add_dialogue_read"), () -> this.addObjective(this.createDialogueReadObjective(), true));
    }

    private AbstractObjective createDialogueReadObjective()
    {
        StateObjective objective = new StateObjective();
        DialogueConditionBlock block = new DialogueConditionBlock();

        objective.expression.condition.blocks.add(block);
        objective.expression.expression = "dialogue_read(\"...\", subject)";
        objective.message = I18n.format("mappet.gui.quests.objective_state.dialogue");

        return objective;
    }

    private void addObjective(AbstractObjective objective, boolean add)
    {
        GuiObjective element = null;

        if (objective instanceof KillObjective)
        {
            element = new GuiKillObjective(this.mc, (KillObjective) objective);
        }
        else if (objective instanceof CollectObjective)
        {
            element = new GuiCollectObjective(this.mc, (CollectObjective) objective);
        }
        else if (objective instanceof StateObjective)
        {
            element = new GuiStateObjective(this.mc, (StateObjective) objective);
        }

        if (element != null)
        {
            this.add(element);

            final GuiObjective finalElement = element;

            element.context(() -> new GuiSimpleContextMenu(Minecraft.getMinecraft())
                .action(Icons.REMOVE, IKey.lang("mappet.gui.quests.objectives.context.remove"), () -> this.removeObjective(finalElement), Colors.NEGATIVE));

            if (add)
            {
                this.objectives.add(objective);
                this.getParentContainer().resize();
            }
        }
    }

    private void removeObjective(GuiObjective element)
    {
        if (this.objectives.remove(element.objective))
        {
            element.removeFromParent();
            this.getParentContainer().resize();
        }
    }

    public void set(List<AbstractObjective> objectives)
    {
        this.objectives = objectives;

        this.removeAll();

        for (AbstractObjective objective : this.objectives)
        {
            this.addObjective(objective, false);
        }

        this.getParentContainer().resize();
    }
}