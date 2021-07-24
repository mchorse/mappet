package mchorse.mappet.client.gui.utils;

import mchorse.mappet.api.utils.Target;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTargetElement extends GuiElement
{
    public Target target;

    private GuiCirculateElement mode;
    private GuiTextElement selector;

    public GuiTargetElement(Minecraft mc, Target target)
    {
        super(mc);

        this.mode = GuiMappetUtils.createTargetCirculate(mc, TargetMode.GLOBAL, this::toggleTarget);
        this.selector = new GuiTextElement(mc, 1000, (t) -> this.target.selector = t);

        this.flex().column(5).stretch().vertical();

        this.setTarget(target);
    }

    public void setTarget(Target target)
    {
        this.target = target;

        if (target != null)
        {
            this.mode.setValue(target.mode.ordinal());
            this.selector.setText(target.selector);
        }

        this.updateTarget();
    }

    public GuiTargetElement skipGlobal()
    {
        return this.skip(TargetMode.GLOBAL);
    }

    public GuiTargetElement skip(TargetMode target)
    {
        this.mode.disable(target.ordinal());

        return this;
    }

    private void toggleTarget(TargetMode target)
    {
        this.target.mode = target;

        this.updateTarget();
    }

    private void updateTarget()
    {
        if (this.target == null)
        {
            return;
        }

        this.removeAll();
        this.add(Elements.label(IKey.lang("mappet.gui.conditions.target")), this.mode);

        if (this.target.mode == TargetMode.SELECTOR)
        {
            this.add(Elements.label(IKey.lang("mappet.gui.conditions.selector")), this.selector);
        }

        GuiElement container = this.getParentContainer();

        if (container != null)
        {
            container.resize();
        }
    }
}