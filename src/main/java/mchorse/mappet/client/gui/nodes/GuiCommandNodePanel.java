package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCommandNodePanel extends GuiNodePanel<CommandNode>
{
    public GuiTextElement command;

    public GuiCommandNodePanel(Minecraft mc)
    {
        super(mc);

        this.command = new GuiTextElement(mc, 10000, (text) -> this.node.command = text);

        this.add(Elements.label(IKey.str("Command")), this.command, this.binary);
    }

    @Override
    public void set(CommandNode node)
    {
        super.set(node);

        this.command.setText(node.command);
    }
}