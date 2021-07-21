package mchorse.mappet.client.gui.states;

import mchorse.mappet.api.states.States;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.util.Comparator;

public class GuiStatesEditor extends GuiScrollElement
{
    private States states;

    public GuiStatesEditor(Minecraft mc)
    {
        super(mc);

        this.flex().column(5).vertical().stretch().scroll().padding(10);
    }

    public States get()
    {
        return this.states;
    }

    public GuiStatesEditor set(States states)
    {
        this.states = states;

        this.removeAll();

        if (states != null)
        {
            for (String key : states.values.keySet())
            {
                this.add(new GuiState(this.mc, key, states));
            }
        }

        this.sortElements();
        this.resize();

        return this;
    }

    private void sortElements()
    {
        this.getChildren().sort(Comparator.comparing(a -> ((GuiState) a).getKey()));
    }

    public void addNew()
    {
        if (this.states == null)
        {
            return;
        }

        int index = this.states.values.size() + 1;
        String key = "state_" + index;

        while (this.states.values.containsKey(key))
        {
            index += 1;
            key = "state_" + index;
        }

        this.states.values.put(key, 0);
        this.add(new GuiState(this.mc, key, this.states));

        this.sortElements();

        this.getParentContainer().resize();
    }

    @Override
    public void draw(GuiContext context)
    {
        super.draw(context);

        if (this.states != null && this.states.values.isEmpty())
        {
            int w = this.area.w / 2;
            int x = this.area.mx(w);

            GuiDraw.drawMultiText(this.font, I18n.format("mappet.gui.states.empty"), x, this.area.my(), 0xffffff, w, 12, 0.5F, 0.5F);
        }
    }
}