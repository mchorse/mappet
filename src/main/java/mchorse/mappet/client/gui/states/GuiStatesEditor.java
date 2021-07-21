package mchorse.mappet.client.gui.states;

import mchorse.mappet.api.states.States;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import net.minecraft.client.Minecraft;

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
}