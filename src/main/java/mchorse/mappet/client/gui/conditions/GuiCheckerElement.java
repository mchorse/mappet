package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import net.minecraft.client.Minecraft;

public class GuiCheckerElement extends GuiElement
{
    public GuiTextElement expression;
    public GuiIconElement toggle;
    public GuiButtonElement condition;

    private Checker checker;

    public GuiCheckerElement(Minecraft mc)
    {
        this(mc, null);
    }

    public GuiCheckerElement(Minecraft mc, Checker checker)
    {
        super(mc);

        this.expression = GuiMappetUtils.fullWindowContext(
            new GuiTextElement(mc, 10000, (text) -> this.checker.expression = text),
            IKey.lang("mappet.gui.checker.expression")
        );
        this.toggle = new GuiIconElement(mc, Icons.REFRESH, this::toggleMode);
        this.toggle.tooltip(IKey.lang("mappet.gui.checker.toggle"), 300, Direction.BOTTOM);
        this.condition = new GuiButtonElement(mc, IKey.lang("mappet.gui.checker.edit"), this::openConditionEditor);

        this.flex().h(20).row(0);

        this.set(checker);
    }

    private void toggleMode(GuiIconElement b)
    {
        this.checker.mode = this.checker.mode == Checker.Mode.CONDITION ? Checker.Mode.EXPRESSION : Checker.Mode.CONDITION;

        this.updateFields();
    }

    private void updateFields()
    {
        this.removeAll();
        this.add(this.checker.mode == Checker.Mode.CONDITION ? this.condition : this.expression, this.toggle);

        GuiElement element = this.getParentContainer();

        if (element != null)
        {
            element.resize();
        }
    }

    private void openConditionEditor(GuiButtonElement b)
    {
        GuiConditionOverlayPanel panel = new GuiConditionOverlayPanel(this.mc, this.checker.condition);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.6F, 0.8F);
    }

    public Checker get()
    {
        return this.checker;
    }

    public void set(Checker checker)
    {
        this.checker = checker;

        if (checker != null)
        {
            this.expression.setText(checker.expression);
            this.updateFields();
        }
    }
}