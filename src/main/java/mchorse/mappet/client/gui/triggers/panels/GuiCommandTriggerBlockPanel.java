package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCommandTriggerBlockPanel extends GuiAbstractTriggerBlockPanel<CommandTriggerBlock>
{
    public GuiTextElement command;

    public GuiCommandTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, CommandTriggerBlock block)
    {
        super(mc, overlay, block);

        this.command = GuiMappetUtils.fullWindowContext(
                new GuiTextElement(mc, 10000, (text) -> this.block.string = text),
                IKey.lang("mappet.gui.trigger_types.command")
        );
        this.command.setText(block.string);

        this.add(this.command);
        this.addDelay();
    }
}