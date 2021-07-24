package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.ScriptNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiScriptNodePanel extends GuiDataNodePanel<ScriptNode>
{
    public GuiTextElement function;

    public GuiScriptNodePanel(Minecraft mc)
    {
        super(mc);

        this.function = new GuiTextElement(mc, 100, (text) -> this.node.function = text);
        this.function.tooltip(IKey.lang("mappet.gui.triggers.script.function_tooltip"));

        this.binary.removeFromParent();
        this.add(Elements.label(IKey.lang("mappet.gui.triggers.function")).marginTop(12), this.function);
        this.add(this.binary);
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

    @Override
    public void set(ScriptNode node)
    {
        super.set(node);

        this.function.setText(node.function);
    }
}