package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
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
            IKey.lang("mappet.gui.nodes.event.condition")
        );
        this.toggle = new GuiIconElement(mc, Icons.REVERSE, this::toggleMode);
        this.condition = new GuiButtonElement(mc, IKey.lang("mappet.gui.nodes.event.edit_condition"), this::openConditionEditor);

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

        if (this.hasParent())
        {
            this.getParent().resize();
        }
    }

    private void openConditionEditor(GuiButtonElement b)
    {
        GuiConditionOverlayPanel panel = new GuiConditionOverlayPanel(this.mc, this.checker.condition);

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.6F, 0.6F);
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