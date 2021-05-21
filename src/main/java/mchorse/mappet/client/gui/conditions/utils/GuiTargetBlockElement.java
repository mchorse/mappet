package mchorse.mappet.client.gui.conditions.utils;

import mchorse.mappet.api.conditions.blocks.TargetBlock;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiTargetBlockElement <T extends TargetBlock> extends GuiElement
{
    public T block;

    public GuiElement targeter;
    private GuiCirculateElement target;
    private GuiTextElement selector;

    private boolean skipGlobal;

    public GuiTargetBlockElement(Minecraft mc, T block)
    {
        super(mc);

        this.block = block;

        this.targeter = new GuiElement(mc);
        this.target = new GuiCirculateElement(mc, this::toggleTarget);

        for (Target target : Target.values())
        {
            this.target.addLabel(IKey.lang("mappet.gui.conditions.targets." + target.name().toLowerCase()));
        }

        this.target.setValue(block.target.ordinal());

        this.selector = new GuiTextElement(mc, 1000, (t) -> this.block.selector = t);
        this.selector.setText(block.selector);

        this.targeter.flex().column(5).stretch().vertical();
        this.updateTarget();
    }

    public GuiTargetBlockElement<T> skipGlobal()
    {
        this.skipGlobal = true;

        return this;
    }

    private void toggleTarget(GuiButtonElement b)
    {
        Target target = Target.values()[this.target.getValue()];

        /* Some blocks don't support  */
        if (this.skipGlobal && target == Target.GLOBAL)
        {
            target = this.block.target == Target.SUBJECT ? Target.SELECTOR : Target.SUBJECT;
            this.target.setValue(target.ordinal());
        }

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