package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.list.GuiLabelSearchListElement;
import mchorse.mclib.client.gui.utils.Label;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.util.Set;
import java.util.function.Consumer;

public class GuiLabelOverlayPanel <T> extends GuiOverlayPanel
{
    public GuiLabelSearchListElement<T> labels;

    private Consumer<Label<T>> callback;

    public GuiLabelOverlayPanel(Minecraft mc, IKey title, Set<Label<T>> keys, Consumer<Label<T>> callback)
    {
        super(mc, title);

        this.callback = callback;

        this.labels = new GuiLabelSearchListElement<T>(mc, (list) -> this.accept(list.get(0)));
        this.labels.label = IKey.lang("mappet.gui.search");
        this.labels.flex().relative(this.content).wh(1F, 1F);

        for (Label<T> location : keys)
        {
            this.labels.list.add(location);
        }

        this.labels.list.sort();

        this.labels.list.update();
        this.labels.list.scroll.scrollSpeed *= 3;

        this.content.add(this.labels);
    }

    public GuiLabelOverlayPanel<T> set(T value)
    {
        this.labels.filter("", true);

        for (Label<T> label : this.labels.list.getList())
        {
            if (label.value.equals(value))
            {
                this.labels.list.setCurrentScroll(label);

                break;
            }
        }

        return this;
    }

    private void accept(Label<T> label)
    {
        if (this.callback != null)
        {
            this.callback.accept(label);
        }
    }
}
