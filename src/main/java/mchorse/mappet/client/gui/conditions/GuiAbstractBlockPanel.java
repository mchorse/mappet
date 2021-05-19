package mchorse.mappet.client.gui.conditions;

import mchorse.mappet.api.conditions.blocks.AbstractBlock;
import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiAbstractBlockPanel <T extends AbstractBlock> extends GuiElement
{
    public GuiElement icons;
    public GuiIconElement not;
    public GuiIconElement or;

    protected T block;

    public GuiAbstractBlockPanel(Minecraft mc, T block)
    {
        super(mc);

        this.block = block;

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.condition_types." + AbstractBlock.FACTORY.getType(block)));

        this.not = new GuiIconElement(mc, Icons.EXCLAMATION, (b) -> this.block.not = !this.block.not);
        this.not.flex().wh(16, 16);
        this.or = new GuiIconElement(mc, Icons.REVERSE, (b) -> this.block.or = !this.block.or);
        this.or.flex().wh(16, 16);
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