package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.function.Consumer;

public abstract class GuiResourceLocationOverlayPanel extends GuiOverlayPanel
{
    public GuiStringSearchListElement rls;

    private Consumer<ResourceLocation> callback;

    public GuiResourceLocationOverlayPanel(Minecraft mc, IKey title, Set<ResourceLocation> keys, Consumer<ResourceLocation> callback)
    {
        super(mc, title);

        this.callback = callback;

        this.rls = new GuiStringSearchListElement(mc, (list) -> this.accept(list.get(0)));
        this.rls.label = IKey.lang("mappet.gui.search");
        this.rls.flex().relative(this.content).wh(1F, 1F);

        for (ResourceLocation location : keys)
        {
            this.rls.list.add(location.toString());
        }

        this.rls.list.sort();

        this.rls.list.getList().add(0, I18n.format("mappet.gui.none"));
        this.rls.list.update();
        this.rls.list.scroll.scrollSpeed *= 3;

        this.content.add(this.rls);
    }

    public GuiResourceLocationOverlayPanel set(ResourceLocation rl)
    {
        return this.set(rl == null ? "" : rl.toString());
    }

    public GuiResourceLocationOverlayPanel set(String rl)
    {
        this.rls.filter("", true);
        this.rls.list.setCurrentScroll(rl);

        if (this.rls.list.isDeselected())
        {
            this.rls.list.setIndex(0);
        }

        return this;
    }

    private void accept(String string)
    {
        if (this.callback != null)
        {
            this.callback.accept(this.rls.list.getIndex() == 0 ? null : new ResourceLocation(string));
        }
    }
}
