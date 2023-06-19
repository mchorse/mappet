package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.panels.GuiScriptPanel;
import mchorse.mappet.client.gui.scripts.GuiTextEditor;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.List;

public class GuiScriptTriggerBlockPanel extends GuiDataTriggerBlockPanel<ScriptTriggerBlock>
{
    public GuiTextElement function;

    public GuiToggleElement inline;

    public GuiTextEditor code;

    private List<GuiElement> allElements;

    private GuiElement elements;

    public GuiScriptTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, ScriptTriggerBlock block)
    {
        super(mc, overlay, block);

        this.inline = new GuiToggleElement(mc, IKey.lang("mappet.gui.triggers.script.inline"), this.block.inline, (b) ->
        {
            this.block.inline = b.isToggled();

            this.updateFields();
        });

        this.code = new GuiTextEditor(mc, (s) -> this.block.code = s);
        this.code.context(() -> GuiScriptPanel.createScriptContextMenu(this.mc, code));
        this.code.setText(this.block.code);
        this.code.background().flex().h(160);

        this.function = new GuiTextElement(mc, 100, (text) -> this.block.function = text);
        this.function.setText(block.function);
        this.function.tooltip(IKey.lang("mappet.gui.triggers.script.function_tooltip"));

        this.elements = Elements.column(mc, 5);

        this.add(this.picker);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.data);
        this.add(Elements.label(IKey.lang("mappet.gui.triggers.function")).marginTop(12), this.function);

        this.allElements = this.getChildren(GuiElement.class);
        this.allElements.remove(0);

        for (GuiElement element : this.allElements)
        {
            element.removeFromParent();
            this.elements.add(element);
        }

        this.add(this.elements);
        this.updateFields();
        this.addDelay();
    }

    private void updateFields()
    {
        this.elements.removeAll();
        this.elements.add(this.inline);

        if (this.block.inline)
        {
            this.elements.add(this.code);
        }
        else
        {
            for (GuiElement element : this.allElements)
            {
                this.elements.add(element);
            }
        }

        if (this.hasParent())
        {
            this.getParentContainer().resize();
        }
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