package mchorse.mappet.client.gui.states;

import mchorse.mappet.api.states.States;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiStatesOverlay extends GuiOverlayPanel
{
    public GuiIconElement add;
    public GuiStatesEditor states;

    public GuiStatesOverlay(Minecraft mc, IKey title, States states)
    {
        super(mc, title);

        this.add = new GuiIconElement(mc, Icons.ADD, (b) -> this.states.addNew());
        this.add.flex().wh(16, 16);

        this.states = new GuiStatesEditor(mc);
        this.states.set(states);
        this.states.flex().relative(this.content).x(-10).y(-5).w(1F, 20).h(1F, 5);

        this.icons.add(this.add.marginRight(4));
        this.content.add(this.states);
    }
}