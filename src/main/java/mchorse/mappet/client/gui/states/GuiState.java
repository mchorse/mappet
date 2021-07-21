package mchorse.mappet.client.gui.states;

import mchorse.mappet.api.states.States;
import mchorse.mappet.utils.Colors;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Icons;
import net.minecraft.client.Minecraft;

public class GuiState extends GuiElement
{
    public GuiTextElement id;
    public GuiIconElement convert;
    public GuiElement value;
    public GuiIconElement remove;

    private String key;
    private States states;

    public GuiState(Minecraft mc, String key, States states)
    {
        super(mc);

        this.key = key;
        this.states = states;

        this.id = new GuiTextElement(mc, 1000, this::rename);
        this.id.flex().w(120);
        this.id.setText(key);
        this.convert = new GuiIconElement(mc, Icons.REFRESH, this::convert);
        this.remove = new GuiIconElement(mc, Icons.REMOVE, this::removeState);

        this.flex().row(0).preferred(2);
        this.updateValue();
    }

    public String getKey()
    {
        return this.key;
    }

    private void rename(String key)
    {
        if (this.states.values.containsKey(key) || key.isEmpty())
        {
            this.id.field.setTextColor(Colors.NEGATIVE);

            return;
        }

        this.id.field.setTextColor(0xffffff);

        Object value = this.states.values.remove(this.key);

        this.states.values.put(key, value);
        this.key = key;
    }

    private void convert(GuiIconElement element)
    {
        Object object = this.states.values.get(this.key);

        if (object instanceof String)
        {
            this.states.values.put(this.key, 0);
        }
        else
        {
            this.states.values.put(this.key, "");
        }

        this.updateValue();
    }

    private void removeState(GuiIconElement element)
    {
        this.states.values.remove(this.key);

        GuiElement parent = this.getParentContainer();

        this.removeFromParent();
        parent.resize();
    }

    private void updateValue()
    {
        Object object = this.states.values.get(this.key);

        if (object instanceof String)
        {
            GuiTextElement element = new GuiTextElement(this.mc, 10000, this::updateString);

            element.setText((String) object);
            this.value = element;
        }
        else
        {
            GuiTrackpadElement element = new GuiTrackpadElement(this.mc, this::updateNumber);

            element.setValue(((Number) object).doubleValue());
            this.value = element;
        }

        this.removeAll();
        this.add(this.id, this.convert, this.value, this.remove);

        if (this.hasParent())
        {
            this.getParentContainer().resize();
        }
    }

    private void updateString(String s)
    {
        this.states.values.put(this.key, s);
    }

    private void updateNumber(double v)
    {
        this.states.values.put(this.key, v);
    }
}