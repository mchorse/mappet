package mchorse.mappet.client.gui.conditions.utils;

import mchorse.mappet.api.conditions.blocks.TargetConditionBlock;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTargetBlockElement <T extends TargetConditionBlock> extends GuiElement
{
    public T block;

    public GuiElement targeter;
    private GuiCirculateElement target;
    private GuiTextElement selector;

    public GuiTargetBlockElement(Minecraft mc, T block)
    {
        super(mc);

        this.block = block;

        this.targeter = new GuiElement(mc);
        this.target = GuiMappetUtils.createTargetCirculate(mc, block.target, this::toggleTarget);

        this.selector = new GuiTextElement(mc, 1000, (t) -> this.block.selector = t);
        this.selector.setText(block.selector);

        this.targeter.flex().column(5).stretch().vertical();
        this.updateTarget();
    }

    public GuiTargetBlockElement<T> skipGlobal()
    {
        return this.skip(Target.GLOBAL);
    }

    public GuiTargetBlockElement<T> skip(Target target)
    {
        this.target.disable(target.ordinal());

        return this;
    }

    private void toggleTarget(Target target)
    {
        this.block.target = target;

        this.updateTarget();
    }

    private void updateTarget()
    {
        this.targeter.removeAll();
        this.targeter.add(Elements.label(IKey.lang("mappet.gui.conditions.target")), this.target);

        if (this.block.target == Target.SELECTOR)
        {
            this.targeter.add(Elements.label(IKey.lang("mappet.gui.conditions.selector")), this.selector);
        }

        if (this.targeter.hasParent())
        {
            this.targeter.getParentContainer().resize();
        }
    }
}