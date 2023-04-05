package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiScriptTriggerBlockPanel extends GuiDataTriggerBlockPanel<ScriptTriggerBlock>
{
    public GuiTextElement function;

    public GuiElement standartLayout;
    public GuiElement inlineLayout;

    public GuiScriptTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, ScriptTriggerBlock block)
    {
        super(mc, overlay, block);

        GuiToggleElement inline = new GuiToggleElement(mc, IKey.lang("mappet.gui.triggers.script.inline"), this.block.inline, (b) -> this.toggleInline(b.isToggled()));
        GuiTextEditor code = new GuiTextEditor(mc, (s) -> this.block.code = s);
        code.setText(this.block.code);

        this.function = new GuiTextElement(mc, 100, (text) -> this.block.function = text);
        this.function.setText(block.function);
        this.function.tooltip(IKey.lang("mappet.gui.triggers.script.function_tooltip"));

        this.add(inline);

        GuiElement layouts = new GuiElement(mc);
        layouts.flex().relative(this).wh(1F, 0.7F);

        this.standartLayout = Elements.column(mc, 5,
                this.picker,
                Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.data,
                Elements.label(IKey.lang("mappet.gui.triggers.function")).marginTop(12), this.function);

        this.standartLayout.setVisible(!this.block.inline);
        this.standartLayout.flex().relative(layouts).wh(1F,1F);

        this.inlineLayout = Elements.column(mc, 5, code);

        this.inlineLayout.setVisible(this.block.inline);
        this.inlineLayout.flex().relative(layouts).wh(1F,1F);

        code.flex().relative(this.inlineLayout).wh(1F, 1.1F);
        code.background();

        layouts.add(standartLayout, inlineLayout);
        this.add(inline, layouts);

        this.addDelay();
    }


    public void toggleInline(boolean state)
    {
        this.block.inline = state;
        this.standartLayout.setVisible(!state);
        this.inlineLayout.setVisible(state);
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.script");
    }

    @Override
    protected ContentType getType()
    {
        return ContentType.SCRIPTS;
    }
}