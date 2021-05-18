package mchorse.mappet.client.gui.utils.overlays;

import mchorse.mclib.client.gui.framework.elements.list.GuiStringSearchListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class GuiStringOverlayPanel extends GuiOverlayPanel
{
    public GuiStringSearchListElement strings;

    private Consumer<String> callback;

    public GuiStringOverlayPanel(Minecraft mc, IKey title, Collection<String> strings, Consumer<String> callback)
    {
        super(mc, title);

        this.callback = callback;

        this.strings = new GuiStringSearchListElement(mc, (list) -> this.accept(list.get(0)));
        this.strings.label = IKey.lang("mappet.gui.search");
        this.strings.flex().relative(this.content).wh(1F, 1F);

        this.strings.list.add(strings);
        this.strings.list.sort();
        this.strings.list.scroll.scrollSpeed *= 2;

        this.strings.list.getList().add(0, I18n.format("mappet.gui.none"));
        this.strings.list.update();

        this.content.add(this.strings);
    }

    public GuiStringOverlayPanel set(String string)
    {
        this.strings.filter("", true);
        this.strings.list.setCurrentScroll(string);

        if (this.strings.list.isDeselected())
        {
            this.strings.list.setIndex(0);
        }

        return this;
    }

    private void accept(String string)
    {
        if (this.callback != null)
        {
            this.callback.accept(this.getValue(string));
        }
    }

    protected String getValue()
    {
        return this.getValue(this.strings.list.getCurrentFirst());
    }

    protected String getValue(String string)
    {
        return this.strings.list.getIndex() == 0 ? "" : string;
    }
}
