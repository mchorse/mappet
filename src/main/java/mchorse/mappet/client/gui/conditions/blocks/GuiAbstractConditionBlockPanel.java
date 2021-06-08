package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.conditions.blocks.AbstractConditionBlock;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiAbstractConditionBlockPanel <T extends AbstractConditionBlock> extends GuiElement
{
    public GuiElement icons;
    public GuiIconElement not;
    public GuiIconElement or;

    protected GuiConditionOverlayPanel overlay;
    protected T block;

    public GuiAbstractConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, T block)
    {
        super(mc);

        this.overlay = overlay;
        this.block = block;

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.condition_types." + CommonProxy.getConditionBlocks().getType(block)));

        this.not = new GuiIconElement(mc, Icons.EXCLAMATION, (b) -> this.block.not = !this.block.not);
        this.not.tooltip(IKey.lang("mappet.gui.conditions.not")).flex().wh(16, 16);
        this.or = new GuiIconElement(mc, Icons.REVERSE, (b) -> this.block.or = !this.block.or);
        this.or.tooltip(IKey.lang("mappet.gui.conditions.or")).flex().wh(16, 16);
        this.icons = new GuiElement(mc);
        this.icons.flex().relative(label).x(1F).y(-4).anchorX(1F).row(0).resize().reverse();
        this.icons.add(this.or, this.not);
        label.add(this.icons);

        this.flex().column(5).vertical().stretch();
        this.add(label);
    }

    @Override
    public void draw(GuiContext context)
    {
        int primary = McLib.primaryColor.get();

        if (this.block.not)
        {
            this.not.area.draw(0x88000000 + primary);
        }

        if (this.block.or)
        {
            this.or.area.draw(0x88000000 + primary);
        }

        super.draw(context);
    }
}