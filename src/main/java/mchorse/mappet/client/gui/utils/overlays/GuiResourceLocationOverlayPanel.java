package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.function.Consumer;

public abstract class GuiResourceLocationOverlayPanel extends GuiOverlayPanel
{
    public GuiStringSearchListElement sounds;

    private Consumer<ResourceLocation> callback;

    public GuiResourceLocationOverlayPanel(Minecraft mc, IKey title, Set<ResourceLocation> keys, Consumer<ResourceLocation> callback)
    {
        super(mc, title);

        this.callback = callback;

        this.sounds = new GuiStringSearchListElement(mc, (list) -> this.accept(list.get(0)));
        this.sounds.label = IKey.lang("mappet.gui.search");
        this.sounds.flex().relative(this.content).wh(1F, 1F);

        for (ResourceLocation location : keys)
        {
            this.sounds.list.add(location.toString());
        }

        this.sounds.list.sort();

        this.content.add(this.sounds);
    }

    public GuiResourceLocationOverlayPanel set(ResourceLocation rl)
    {
        return this.set(rl.toString());
    }

    public GuiResourceLocationOverlayPanel set(String rl)
    {
        this.sounds.filter("", true);
        this.sounds.list.setCurrentScroll(rl);

        return this;
    }

    private void accept(String string)
    {
        if (this.callback != null)
        {
            this.callback.accept(new ResourceLocation(string));
        }
    }
}
