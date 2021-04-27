package mchorse.mappet.client.gui.factions;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.function.Consumer;

public class GuiFactionsOverlay extends GuiOverlayPanel
{
    public GuiFactionsOverlay(Minecraft mc, List<String> keys, Consumer<String> callback)
    {
        super(mc, IKey.str("Pick a faction..."));

        GuiStringListElement list = new GuiStringListElement(mc, null);
        GuiButtonElement button = new GuiButtonElement(mc, IKey.str("Add"), (b) ->
        {
            if (callback != null && !list.isDeselected())
            {
                callback.accept(list.getCurrentFirst());
            }

            this.close();
        });

        list.add(keys);
        list.flex().relative(this.content).w(1F).h(1F, -35);
        button.flex().relative(this.content).x(1F).y(1F, -10).w(0.4F).anchor(1F, 1F);

        this.content.add(list, button);
    }
}