package mchorse.mappet.client.gui.quests.objectives;

import mchorse.mappet.api.quests.objectives.IObjective;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import net.minecraft.client.Minecraft;

public class GuiObjective <T extends IObjective> extends GuiElement
{
    public T objective;

    public GuiObjective(Minecraft mc, T objective)
    {
        super(mc);

        this.objective = objective;
    }
}
