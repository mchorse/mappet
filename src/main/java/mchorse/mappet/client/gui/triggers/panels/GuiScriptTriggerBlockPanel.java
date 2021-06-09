package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiScriptTriggerBlockPanel extends GuiStringTriggerBlockPanel<ScriptTriggerBlock>
{
    public GuiTextElement function;

    public GuiScriptTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, ScriptTriggerBlock block)
    {
        super(mc, overlay, block);

        this.function = new GuiTextElement(mc, 100, (text) -> this.block.function = text);
        this.function.setText(block.function);
        this.function.tooltip(IKey.lang("mappet.gui.triggers.script.function_tooltip"));

        this.add(Elements.label(IKey.lang("mappet.gui.triggers.function")).marginTop(12), this.function);
        this.addDelay();
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