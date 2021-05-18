package mchorse.mappet.client.gui.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiNpcMetaPanel extends GuiNpcPanel
{
    public GuiTextElement id;
    public GuiToggleElement unique;
    public GuiTrackpadElement pathDistance;

    public GuiNpcMetaPanel(Minecraft mc, boolean id)
    {
        super(mc);

        this.id = new GuiTextElement(mc, 1000, (t) -> this.state.id = t);
        this.unique = new GuiToggleElement(mc, IKey.lang("mappet.gui.npcs.meta.unique"), (b) -> this.state.unique = b.isToggled());
        this.pathDistance = new GuiTrackpadElement(mc, (v) -> this.state.pathDistance = v.floatValue());

        if (id)
        {
            this.add(Elements.label(IKey.lang("mappet.gui.npcs.meta.id")), this.id);
        }

        this.add(this.unique);
        this.add(Elements.label(IKey.lang("mappet.gui.npcs.meta.path_distance")), this.pathDistance);
    }

    @Override
    public void set(NpcState state)
    {
        super.set(state);

        this.id.setText(state.id);
        this.unique.toggled(state.unique);
        this.pathDistance.setValue(state.pathDistance);
    }
}