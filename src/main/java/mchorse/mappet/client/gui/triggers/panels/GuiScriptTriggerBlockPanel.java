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
        code.background();

        this.function = new GuiTextElement(mc, 100, (text) -> this.block.function = text);
        this.function.setText(block.function);
        this.function.tooltip(IKey.lang("mappet.gui.triggers.script.function_tooltip"));

        this.standartLayout = new GuiElement(mc);
        GuiElement standartElements = Elements.column(mc, 5,
                this.picker,
                Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.data,
                Elements.label(IKey.lang("mappet.gui.triggers.function")).marginTop(12), this.function);
        this.standartLayout.add(standartElements);
        this.standartLayout.flex().relative(this).w(1F).h(122);
        standartElements.flex().relative(this.standartLayout).wh(1F,1F);

        this.inlineLayout = new GuiElement(mc);
        GuiElement inlineElements = Elements.column(mc, 5, code);
        this.inlineLayout.add(inlineElements);
        this.inlineLayout.flex().relative(this).w(1F).h(140);
        inlineElements.flex().relative(this.inlineLayout).wh(1F,1F);
        code.flex().relative(inlineElements).wh(1F, 1F);


        this.add(inline, standartLayout, inlineLayout);
        this.toggleInline(inline.isToggled());
        code.resize();
        this.addDelay();
    }


    public void toggleInline(boolean state)
    {
        this.block.inline = state;
        this.standartLayout.setVisible(!state);
        int size = 140;
        this.standartLayout.flex().h(state ? 0 : size);
        this.inlineLayout.setVisible(state);
        this.inlineLayout.flex().h(state ? size : 0);
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